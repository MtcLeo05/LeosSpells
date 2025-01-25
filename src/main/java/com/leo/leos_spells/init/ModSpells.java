package com.leo.leos_spells.init;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.spell.SpellType;
import com.leo.leos_spells.spell.custom.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Function;

public class ModSpells {
    public static final ResourceKey<Registry<MapCodec<? extends SpellType>>> REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "spell_types"));

    public static final DeferredRegister<MapCodec<? extends SpellType>> SPELLS = DeferredRegister.create(REGISTRY_KEY, LeosSpells.MODID);
    public static final Registry<MapCodec<? extends SpellType>> REGISTRY = new RegistryBuilder<>(REGISTRY_KEY)
        .create();

    public static final DeferredHolder<MapCodec<? extends SpellType>, MapCodec<ManaBolt>> MANA_BOLT = SPELLS.register("mana_bolt", () -> ManaBolt.CODEC);
    public static final DeferredHolder<MapCodec<? extends SpellType>, MapCodec<CooldownReduction>> COOLDOWN_REDUCTION = SPELLS.register("cooldown_reduction", () -> CooldownReduction.CODEC);
    public static final DeferredHolder<MapCodec<? extends SpellType>, MapCodec<FireModifier>> FIRE_MODIFIER = SPELLS.register("fire_modifier", () -> FireModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends SpellType>, MapCodec<EffectModifier>> EFFECT_MODIFIER = SPELLS.register("effect_modifier", () -> EffectModifier.CODEC);
    public static final DeferredHolder<MapCodec<? extends SpellType>, MapCodec<MaxManaIncrease>> MAX_MANA = SPELLS.register("max_mana", () -> MaxManaIncrease.CODEC);

    public static Codec<? extends SpellType> CODEC = REGISTRY.byNameCodec().dispatch(
            SpellType::type,
            Function.identity()
    );
}
