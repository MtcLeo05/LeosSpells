package com.leo.leos_spells.impl.init;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.api.spell.SpellHolder;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class LSEntityDataSerializers {

    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, LeosSpells.MODID);

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<SpellHolder>> SPELL_HOLDER = ENTITY_DATA_SERIALIZERS.register(
        "spell_holder",
        () -> SpellHolder.SPELL_HOLDER
    );

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<List<SpellHolder>>> SPELL_HOLDER_LIST = ENTITY_DATA_SERIALIZERS.register(
        "spell_holder_list",
        () -> SpellHolder.SPELL_HOLDER_LIST
    );
}
