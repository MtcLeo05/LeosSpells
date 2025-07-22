package com.leo.leos_spells.impl.network;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.impl.network.packet.CacheSpellSpritesS2CPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = LeosSpells.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetwork {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
            CacheSpellSpritesS2CPacket.TYPE,
            CacheSpellSpritesS2CPacket.STREAM_CODEC,
            CacheSpellSpritesS2CPacket::handleDataOnClient
        );
    }

}