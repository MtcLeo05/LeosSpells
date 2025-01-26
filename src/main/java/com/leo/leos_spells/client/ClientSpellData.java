package com.leo.leos_spells.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ClientSpellData(ResourceLocation sprite, int color) {
    public static StreamCodec<FriendlyByteBuf, ClientSpellData> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC,
        ClientSpellData::sprite,
        ByteBufCodecs.INT,
        ClientSpellData::color,
        ClientSpellData::new
    );

}
