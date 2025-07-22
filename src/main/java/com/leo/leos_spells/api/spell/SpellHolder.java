package com.leo.leos_spells.api.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record SpellHolder(ResourceLocation spellId) {

    public static final EntityDataSerializer<SpellHolder> SPELL_HOLDER = new EntityDataSerializer<>() {
        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, SpellHolder> codec() {
            return STREAM_CODEC;
        }

        @Override
        public SpellHolder copy(SpellHolder value) {
            return SpellHolder.copy(value);
        }
    };

    public static final EntityDataSerializer<List<SpellHolder>> SPELL_HOLDER_LIST = new EntityDataSerializer<>() {
        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, List<SpellHolder>> codec() {
            return STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new));
        }

        @Override
        public List<SpellHolder> copy(List<SpellHolder> value) {
            return new ArrayList<>(value);
        }
    };

    public static final SpellHolder EMPTY_SPELL = new SpellHolder(ResourceLocation.withDefaultNamespace("empty"));

    public static final Codec<SpellHolder> CODEC = RecordCodecBuilder.create(inst ->
        inst.group(
            ResourceLocation.CODEC.fieldOf("spell_id").forGetter(SpellHolder::spellId)
        ).apply(inst, SpellHolder::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellHolder> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC,
        SpellHolder::spellId,
        SpellHolder::new
    );

    public static SpellHolder copy(SpellHolder original) {
        return new SpellHolder(
            original.spellId()
        );
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();

        tag.putString("spellId", spellId.toString());
        return tag;
    }

    public static SpellHolder deserialize(CompoundTag tag) {
        ResourceLocation spellId = ResourceLocation.parse(tag.getString("spellId"));

        return new SpellHolder(spellId);
    }
}
