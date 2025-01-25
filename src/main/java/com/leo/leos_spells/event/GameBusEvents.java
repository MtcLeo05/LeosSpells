package com.leo.leos_spells.event;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.spell.SpellReloadListener;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@EventBusSubscriber(modid = LeosSpells.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameBusEvents {

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(SpellReloadListener.create());
    }

}
