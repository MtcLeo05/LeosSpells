package com.leo.leos_spells.impl.init;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.impl.entity.SpellBaseEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LSEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, LeosSpells.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<SpellBaseEntity>> SPELL_BASE = ENTITY_TYPES.register("spell_base",
        () -> EntityType.Builder.of(SpellBaseEntity::new, MobCategory.MISC)
            .sized(0.5f, 0.5f)
            .fireImmune()
            .build("spell_base")
    );

}
