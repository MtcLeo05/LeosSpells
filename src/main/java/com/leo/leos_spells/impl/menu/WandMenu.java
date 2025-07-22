package com.leo.leos_spells.impl.menu;

import com.leo.leos_spells.impl.init.LSDataComponents;
import com.leo.leos_spells.impl.init.LSItems;
import com.leo.leos_spells.impl.init.LSMenuTypes;
import com.leo.leos_spells.api.spell.SpellHolder;
import com.leo.leos_spells.api.spell.SpellType;
import com.leo.leos_spells.api.spell.WandProperties;
import com.leo.leos_spells.util.ListUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class WandMenu extends AbstractContainerMenu {
    private final ItemStack wand;
    public final Container fakeInventory;
    private final WandProperties wandProperties;
    private final List<SpellHolder> spellSlots;

    public WandMenu(int pContainerId, Inventory inv, ItemStack wand){
        super(LSMenuTypes.WAND_MENU.get(), pContainerId);

        this.wand = wand;
        wandProperties = wand.get(LSDataComponents.WAND_PROPERTIES);
        spellSlots = ListUtil.mutable(wand.get(LSDataComponents.SPELL_HOLDER));

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        while(spellSlots.size() > wandProperties.slotCount()) {
            spellSlots.removeLast();
        }

        while(spellSlots.size() < wandProperties.slotCount()) {
            spellSlots.add(SpellHolder.EMPTY_SPELL);
        }

        fakeInventory = new SimpleContainer(wandProperties.slotCount());

        for (int i = 0; i < fakeInventory.getContainerSize(); i++) {
            if(!spellSlots.isEmpty() && i < spellSlots.size()) {
                SpellHolder holder = spellSlots.get(i);

                if(!holder.equals(SpellHolder.EMPTY_SPELL)) {
                    ItemStack scroll = LSItems.SPELL_SCROLL.get().getDefaultInstance();
                    scroll.set(LSDataComponents.SPELL_HOLDER, ListUtil.of(holder));

                    fakeInventory.setItem(i, scroll);
                }
            }

            int finalI = i;
            this.addSlot(new Slot(fakeInventory, finalI, 8 + (i % 10) * 18, 8 + (18 + (i / 10))) {
                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public ItemStack remove(int amount) {
                    ItemStack oldScroll = getItem();

                    if(oldScroll.has(LSDataComponents.SPELL_HOLDER)) {
                        var spell = oldScroll.get(LSDataComponents.SPELL_HOLDER).getFirst();
                        SpellType type = SpellType.fromHolder(spell);

                        if(type != null) type.onSpellUnequip(WandMenu.this.wand);

                        spellSlots.set(finalI, SpellHolder.EMPTY_SPELL);
                        WandMenu.this.wand.set(LSDataComponents.SPELL_HOLDER, spellSlots);
                    }

                    return super.remove(amount);
                }

                @Override
                public void set(ItemStack scroll) {
                    super.set(scroll);

                    if(!scroll.has(LSDataComponents.SPELL_HOLDER)) return;

                    var spell = scroll.get(LSDataComponents.SPELL_HOLDER).getFirst();
                    spellSlots.set(finalI, spell);

                    WandMenu.this.wand.set(LSDataComponents.SPELL_HOLDER, spellSlots);
                    SpellType type = SpellType.fromHolder(spell);

                    if(type != null) {
                        type.onSpellEquip(WandMenu.this.wand);
                    }
                }

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return super.mayPlace(stack) || ((stack.get(LSDataComponents.SPELL_HOLDER) != null) && stack.get(LSDataComponents.SPELL_HOLDER).getFirst().equals(SpellHolder.EMPTY_SPELL));
                }
            });
        }
    }

    public WandMenu(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
        this(pContainerId, inv, buf.readJsonWithCodec(ItemStack.CODEC));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
    
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {
        return ItemStack.EMPTY;
    }


    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return pPlayer.getInventory().contains(wand);
    }
}
