package com.leo.leos_spells.impl.item;

import com.leo.leos_spells.impl.entity.SpellBaseEntity;
import com.leo.leos_spells.impl.init.LSDataComponents;
import com.leo.leos_spells.impl.menu.WandMenu;
import com.leo.leos_spells.api.spell.SpellHolder;
import com.leo.leos_spells.api.spell.SpellType;
import com.leo.leos_spells.api.spell.WandProperties;
import com.leo.leos_spells.util.ListUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WandItem extends Item implements MenuProvider {
    public WandItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack wand, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(wand, level, entity, slotId, isSelected);

        if(level.isClientSide) return;

        if(!WandItem.hasNecessaryComponent(wand)) {
            return;
        }

        WandProperties wandProperties = wand.get(LSDataComponents.WAND_PROPERTIES);
        int cooldown = wand.get(LSDataComponents.STACK_COOLDOWN);

        if(cooldown >= 0) wand.set(LSDataComponents.STACK_COOLDOWN, --cooldown);

        if(wandProperties.mana() < wandProperties.maxMana()) {
            wandProperties = wandProperties.clampedAddMana(wandProperties.manaRegen());
            wand.set(LSDataComponents.WAND_PROPERTIES, wandProperties);
        }
    }

    @Override
    public boolean isBarVisible(ItemStack wand) {
        return wand.has(LSDataComponents.WAND_PROPERTIES);
    }

    @Override
    public int getBarColor(ItemStack wand) {
        return wand.has(LSDataComponents.WAND_PROPERTIES)? 0x00def2: 0x000000;
    }

    @Override
    public int getBarWidth(ItemStack wand) {
        if(!wand.has(LSDataComponents.WAND_PROPERTIES)) {
            return super.getBarWidth(wand);
        }

        WandProperties wandProperties = wand.get(LSDataComponents.WAND_PROPERTIES);

        return (int) ((wandProperties.mana() / wandProperties.maxMana()) * 13);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(usedHand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(player.getItemInHand(usedHand));

        ItemStack wand = player.getMainHandItem();

        if(player.isCrouching()) {
            player.openMenu(this, buf -> buf.writeJsonWithCodec(ItemStack.CODEC, wand));
            return InteractionResultHolder.consume(wand);
        }

        if(!(player instanceof ServerPlayer sPlayer)) return InteractionResultHolder.sidedSuccess(wand, level.isClientSide);

        if(!WandItem.hasNecessaryComponent(wand)) return InteractionResultHolder.fail(wand);

        if(wand.get(LSDataComponents.STACK_COOLDOWN) > 0) return InteractionResultHolder.fail(wand);


        List<SpellHolder> spells = ListUtil.mutable(wand.get(LSDataComponents.SPELL_HOLDER));
        List<SpellType> toCast = collectSpells(spells, wand);

        if(toCast.isEmpty()) return InteractionResultHolder.fail(wand);

        float cost = 0;
        for (SpellType spell : toCast) {
            cost = spell.collectMana(cost, sPlayer);
        }

        WandProperties wandProperties = wand.get(LSDataComponents.WAND_PROPERTIES);

        if(wandProperties.mana() < cost) {
            return InteractionResultHolder.fail(wand);
        }

        wandProperties = wandProperties.uncheckedRemoveMana(cost);

        wand.set(LSDataComponents.STACK_COOLDOWN, wandProperties.baseCooldown());
        wand.set(LSDataComponents.SPELL_HOLDER, spells);
        wand.set(LSDataComponents.WAND_PROPERTIES, wandProperties);

        SpellBaseEntity casted = null;

        for (int i = 0; i < toCast.size(); i++) {
            SpellType spell = toCast.get(i);
            SpellType next = null;

            if(toCast.size() < i + 1) {
                next = toCast.get(i + 1);
            }

            casted = spell.cast(wand, sPlayer, spells.get(i), casted, next);
        }

        if(casted != null) sPlayer.serverLevel().addFreshEntity(casted);

        return InteractionResultHolder.consume(wand);
    }

    protected List<SpellType> collectSpells(List<SpellHolder> spells, ItemStack wand) {
        List<SpellType> toCast = new ArrayList<>();
        List<SpellHolder> toRemove = new ArrayList<>();

        for (SpellHolder holder : spells) {
            SpellType spell = SpellType.fromHolder(holder);

            if(spell == null) {
                toRemove.add(holder);
                continue;
            }

            toCast.add(spell);
        }

        spells.removeAll(toRemove);

        wand.set(LSDataComponents.SPELL_HOLDER, spells);
        return toCast;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack wand, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(!wand.has(LSDataComponents.SPELL_HOLDER)) {
            return;
        }

        if(!Screen.hasShiftDown()) {
            tooltipComponents.add(
                Component.translatable("tooltip.leos_spells.wand.shiftInfo")
            );
            return;
        }

        List<SpellHolder> spells = wand.get(LSDataComponents.SPELL_HOLDER.get());

        if(spells.isEmpty()) {
            tooltipComponents.add(
                Component.translatable("tooltip.leos_spells.wand.emptySpells")
            );
            return;
        }

        for (SpellHolder spell : spells) {
            tooltipComponents.add(
                Component.translatable(spell.spellId().toLanguageKey())
            );
        }

        super.appendHoverText(wand, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack wand) {
        return Optional.ofNullable(wand.get(LSDataComponents.WAND_PROPERTIES));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.leos_spells.wand");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        ItemStack main = player.getItemInHand(InteractionHand.MAIN_HAND);

        return new WandMenu(containerId, playerInventory, main);
    }

    public static boolean hasNecessaryComponent(ItemStack wand) {
        return wand.has(LSDataComponents.SPELL_HOLDER) && wand.has(LSDataComponents.WAND_PROPERTIES) && wand.has(LSDataComponents.STACK_COOLDOWN);
    }
}
