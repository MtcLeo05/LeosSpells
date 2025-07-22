package com.leo.leos_spells.impl.init;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.api.spell.SpellHolder;
import com.leo.leos_spells.api.spell.WandProperties;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class LSDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, LeosSpells.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<SpellHolder>>> SPELL_HOLDER = DATA_COMPONENTS.register("spell_holder",
        () -> DataComponentType.<List<SpellHolder>>
            builder()
            .persistent(SpellHolder.CODEC.listOf())
            .networkSynchronized(ByteBufCodecs.collection(ArrayList::new, SpellHolder.STREAM_CODEC))
            .build()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WandProperties>> WAND_PROPERTIES = DATA_COMPONENTS.register("wand_properties",
        () -> DataComponentType.<WandProperties>
                builder()
            .persistent(WandProperties.CODEC)
            .networkSynchronized(WandProperties.STREAM_CODEC)
            .build()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> STACK_COOLDOWN = DATA_COMPONENTS.register("stack_cooldown",
        () -> DataComponentType.<Integer>
                builder()
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.INT)
            .build()
    );
}
