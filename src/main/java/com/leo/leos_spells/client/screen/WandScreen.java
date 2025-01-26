package com.leo.leos_spells.client.screen;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.client.ModClientData;
import com.leo.leos_spells.init.ModDataComponents;
import com.leo.leos_spells.menu.WandMenu;
import com.leo.leos_spells.spell.SpellHolder;
import com.leo.leos_spells.spell.WandProperties;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WandScreen extends AbstractContainerScreen<WandMenu> {

    public static ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "textures/gui/wand_screen.png");
    public static ResourceLocation SLOT = ResourceLocation.withDefaultNamespace("textures/gui/sprites/container/slot.png");

    public WandScreen(WandMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        inventoryLabelY = 10000;
        titleLabelY = 10000;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderSlotContents(GuiGraphics guiGraphics, ItemStack stack, Slot slot, @Nullable String countString) {
        List<SpellHolder> holders = stack.get(ModDataComponents.SPELL_HOLDER);
        WandProperties properties = stack.get(ModDataComponents.WAND_PROPERTIES);

        if(properties != null || holders == null || !Screen.hasShiftDown() || !ModClientData.clientCache.containsKey(holders.getFirst().spellId())) {
            super.renderSlotContents(guiGraphics, stack, slot, countString);
            return;
        }

        SpellHolder holder = holders.getFirst();
        ResourceLocation sprite = ModClientData.clientCache.get(holder.spellId()).sprite();

        if(sprite == null) {
            super.renderSlotContents(guiGraphics, stack, slot, countString);
            return;
        }

        guiGraphics.blit(
            sprite,
            slot.x,
            slot.y,
            16,
            16,
            0,
            0,
            16,
            16,
            16,
            16
        );

        super.renderSlotContents(guiGraphics, stack, slot, countString);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(
                BACKGROUND,
                x,
                y,
                0,
                0,
                176,
                166,
                256,
                256
        );

        for (int i = 0; i < menu.fakeInventory.getContainerSize(); i++) {
            guiGraphics.blit(
                SLOT,
                leftPos + 7 + (i % 10) * 18,
                topPos + 7 + (18 + (i / 10)),
                0,
                0,
                18,
                18,
                18,
                18
            );
        }
    }
}
