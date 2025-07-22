package com.leo.leos_spells.impl.client.tooltip;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.api.spell.WandProperties;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.text.DecimalFormat;

public class ClientWandTooltipComponent implements ClientTooltipComponent {
    private final WandProperties properties;

    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "textures/tooltip/wand_tooltip.png");

    public ClientWandTooltipComponent(WandProperties properties) {
        this.properties = properties;
    }

    @Override
    public int getWidth(Font font) {
        return 80;
    }


    @Override
    public int getHeight() {
        return 44;
    }

    protected static final DecimalFormat DF = new DecimalFormat();

    static {
        DF.setMaximumFractionDigits(0);
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        PoseStack ps = guiGraphics.pose();

        guiGraphics.blit(
            BACKGROUND,
            x,
            y,
            128,
            128,
            0,
            0,
            256,
            256,
            256,
            256
        );

        //ps.scale(0.5f, 0.5f, 1f);

        guiGraphics.drawCenteredString(
            font,
            DF.format(properties.mana()) + " / " + DF.format(properties.maxMana()),
            x + 48,
            y + 1,
            0x00def2
        );

        guiGraphics.drawCenteredString(
            font,
            "+" + DF.format(properties.manaRegen()) + "/t",
            x + 48,
            y + 1 + 15,
            0x00def2
        );
        guiGraphics.drawCenteredString(
            font,
            Component.translatable("tooltip.leos_spells.wand.cd", properties.cooldown()),
            x + 48,
            y + 1 + 30,
            0x00def2
        );

    }
}
