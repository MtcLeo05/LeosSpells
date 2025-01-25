package com.leo.leos_spells.event;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.entity.SpellBaseEntity;
import com.leo.leos_spells.init.ModEntityTypes;
import com.leo.leos_spells.init.ModSpells;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = LeosSpells.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(ModSpells.REGISTRY);
    }

    @SubscribeEvent
    public static void entityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.SPELL_BASE.get(), SpellBaseEntity.setAttributes());
    }
}
