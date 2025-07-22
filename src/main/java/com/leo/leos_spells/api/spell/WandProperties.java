package com.leo.leos_spells.api.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record WandProperties(int baseCooldown, float mana, float maxMana, float manaRegen, int slotCount) implements TooltipComponent {
    public static final Codec<WandProperties> CODEC = RecordCodecBuilder.create(
        inst -> inst.group(
            Codec.INT.fieldOf("base_cooldown").forGetter(WandProperties::baseCooldown),
            Codec.FLOAT.fieldOf("mana").forGetter(WandProperties::mana),
            Codec.FLOAT.fieldOf("max_mana").forGetter(WandProperties::maxMana),
            Codec.FLOAT.fieldOf("mana_regen").forGetter(WandProperties::manaRegen),
            Codec.INT.fieldOf("slot_count").forGetter(WandProperties::slotCount)
        ).apply(inst, WandProperties::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, WandProperties> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        WandProperties::baseCooldown,
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
        return new WandProperties(baseCooldown, Math.min(this.mana + mana, maxMana), maxMana, manaRegen, slotCount);
    }

    public WandProperties addMaxMana(float maxMana) {
        return withMaxMana(this.maxMana + maxMana);
    }

    public WandProperties removeMaxMana(float maxMana) {
        float newMax = Math.max(this.maxMana - maxMana, 0);
        return new WandProperties(baseCooldown, Math.min(mana, newMax), newMax, manaRegen, slotCount);
    }

    public WandProperties uncheckedRemoveMana(float mana) {
        return new WandProperties(baseCooldown, Math.max(this.mana - mana, 0), maxMana, manaRegen, slotCount);
    }

    public WandProperties withMaxMana(float maxMana) {
        return new WandProperties(baseCooldown, mana, maxMana, manaRegen, slotCount);
    }

    public WandProperties withManaRegen(float regen) {
        return new WandProperties(baseCooldown, mana, maxMana, regen, slotCount);
    }

    public WandProperties withSlotCount(int slotCount) {
        return new WandProperties(baseCooldown, mana, maxMana, manaRegen, slotCount);
    }
}
