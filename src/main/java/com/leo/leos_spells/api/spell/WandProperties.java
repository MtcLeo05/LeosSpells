package com.leo.leos_spells.api.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record WandProperties(int cooldown, float mana, float maxMana, float manaRegen, int slotCount) implements TooltipComponent {
    public static final Codec<WandProperties> CODEC = RecordCodecBuilder.create(
        inst -> inst.group(
            Codec.INT.fieldOf("cooldown").forGetter(WandProperties::cooldown),
            Codec.FLOAT.fieldOf("mana").forGetter(WandProperties::mana),
            Codec.FLOAT.fieldOf("maxMana").forGetter(WandProperties::maxMana),
            Codec.FLOAT.fieldOf("manaRegen").forGetter(WandProperties::manaRegen),
            Codec.INT.fieldOf("slotCount").forGetter(WandProperties::slotCount)
        ).apply(inst, WandProperties::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, WandProperties> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        WandProperties::cooldown,
        ByteBufCodecs.FLOAT,
        WandProperties::mana,
        ByteBufCodecs.FLOAT,
        WandProperties::maxMana,
        ByteBufCodecs.FLOAT,
        WandProperties::manaRegen,
        ByteBufCodecs.INT,
        WandProperties::slotCount,
        WandProperties::new
    );

    public WandProperties clampedAddMana(float mana) {
        return new WandProperties(
            cooldown,
            Math.min(this.mana + mana, maxMana),
            maxMana,
            manaRegen,
            slotCount
        );
    }

    public WandProperties addMaxMana(float maxMana) {
        return withMaxMana(this.maxMana + maxMana);
    }

    public WandProperties removeMaxMana(float maxMana) {
        float newMax = Math.max(this.maxMana - maxMana, 0);
        return new WandProperties(
            cooldown,
            Math.min(mana, newMax),
            newMax,
            manaRegen,
            slotCount
        );
    }

    public WandProperties uncheckedRemoveMana(float mana) {
        return new WandProperties(
            cooldown,
            Math.max(this.mana - mana, 0),
            maxMana,
            manaRegen,
            slotCount
        );
    }

    public WandProperties withCooldown(int cd) {
        return new WandProperties(
            cd,
            mana,
            maxMana,
            manaRegen,
            slotCount
        );
    }

    public WandProperties withMaxMana(float maxMana) {
        return new WandProperties(
            cooldown,
            mana,
            maxMana,
            manaRegen,
            slotCount
        );
    }

    public WandProperties withManaRegen(float regen) {
        return new WandProperties(
            cooldown,
            mana,
            maxMana,
            regen,
            slotCount
        );
    }

    public WandProperties withSlotCount(int slotCount) {
        return new WandProperties(
            cooldown,
            mana,
            maxMana,
            manaRegen,
            slotCount
        );
    }
}
