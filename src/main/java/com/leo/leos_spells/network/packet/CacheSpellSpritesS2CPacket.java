package com.leo.leos_spells.network.packet;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.client.ClientSpellData;
import com.leo.leos_spells.client.ModClientData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record CacheSpellSpritesS2CPacket(Map<ResourceLocation, ClientSpellData> cache) implements CustomPacketPayload {
    public static final Type<CacheSpellSpritesS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "cache_spells"));

    public static final StreamCodec<FriendlyByteBuf, CacheSpellSpritesS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ClientSpellData.STREAM_CODEC),
        CacheSpellSpritesS2CPacket::cache,
        CacheSpellSpritesS2CPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleDataOnClient(final CacheSpellSpritesS2CPacket packet, final IPayloadContext context) {
        ModClientData.clientCache.putAll(packet.cache);
    }
}
