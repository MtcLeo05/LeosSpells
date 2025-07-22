package com.leo.leos_spells.impl.mixin;

import com.leo.leos_spells.impl.client.ModClientData;
import com.leo.leos_spells.impl.init.LSDataComponents;
import com.leo.leos_spells.api.spell.SpellHolder;
import com.leo.leos_spells.api.spell.WandProperties;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Redirect(method = "renderSlotContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V"))
    protected void leosSpells$changeStackForDisplay(GuiGraphics instance, ItemStack stack, int x, int y, int seed) {
        List<SpellHolder> holders = stack.get(LSDataComponents.SPELL_HOLDER);
        WandProperties properties = stack.get(LSDataComponents.WAND_PROPERTIES);

        if(properties != null || holders == null || holders.size() != 1 || !Screen.hasShiftDown() || !ModClientData.clientCache.containsKey(holders.getFirst().spellId())) {
            instance.renderItem(stack, x, y, seed);
            return;
        }

        instance.blit(ModClientData.clientCache.get(holders.getFirst().spellId()).sprite(), x, y, 16, 16, 0, 0, 16, 16, 16, 16);
    }
}
