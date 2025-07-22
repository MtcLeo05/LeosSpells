package com.leo.leos_spells.impl.event;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.impl.entity.SpellBaseEntity;
import com.leo.leos_spells.impl.init.LSEntityTypes;
import com.leo.leos_spells.impl.init.LSSpellTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = LeosSpells.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(LSSpellTypes.REGISTRY);
    }

    @SubscribeEvent
    public static void entityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(LSEntityTypes.SPELL_BASE.get(), SpellBaseEntity.setAttributes());
    }
}
