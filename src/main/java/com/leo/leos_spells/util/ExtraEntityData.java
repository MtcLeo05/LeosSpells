package com.leo.leos_spells.util;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.util.ArrayList;
import java.util.List;

public class ExtraEntityData {

    public static final EntityDataSerializer<List<Integer>> INTEGER_LIST = new EntityDataSerializer<>() {
        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, List<Integer>> codec() {
            return ByteBufCodecs.INT.apply(ByteBufCodecs.collection(ArrayList::new));
        }

        @Override
        public List<Integer> copy(List<Integer> value) {
            return new ArrayList<>(value);
        }
    };

}
