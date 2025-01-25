package com.leo.leos_spells.spell.custom;

import com.leo.leos_spells.entity.SpellBaseEntity;
import com.leo.leos_spells.spell.SpellHolder;
import com.leo.leos_spells.spell.SpellType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class EffectModifier extends SpellType {
    public static final MapCodec<EffectModifier> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(t -> t.effect),
            Codec.INT.fieldOf("duration").forGetter(t -> t.duration),
            Codec.INT.fieldOf("amplifier").forGetter(t -> t.amplifier),
            Codec.BOOL.fieldOf("cloud").forGetter(t -> t.cloud),
            ResourceLocation.CODEC.fieldOf("sprite").forGetter(t -> t.sprite)
        ).apply(inst, EffectModifier::new)
    );

    private final Holder<MobEffect> effect;
    private final int duration, amplifier;
    private final boolean cloud;

    public EffectModifier(Holder<MobEffect> effect, int duration, int amplifier, boolean cloud, ResourceLocation sprite) {
        super(-1, 0, 0, 0, sprite);
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
        this.cloud = cloud;

        this.effectInstance = new MobEffectInstance(effect, duration, amplifier);
    }

    @Override
    public MapCodec<? extends SpellType> type() {
        return CODEC;
    }

    @Override
    public SpellBaseEntity cast(ItemStack wand, ServerPlayer player, SpellHolder spellHolder, @Nullable SpellBaseEntity previousSpell, @Nullable SpellType nextSpell) {
        if(previousSpell == null) return null;

        previousSpell.addSpell(spellHolder);
        previousSpell.setColor(effect.value().getColor());
        return previousSpell;
    }

    @Override
    public float collectMana(float previousMana, ServerPlayer player) {
        return previousMana;
    }

    @Override
    public void onSpellEquip(ItemStack wand) {

    }

    @Override
    public void onSpellUnequip(ItemStack wand) {

    }

    @Override
    public void onSpellTick(ServerPlayer player, ItemStack wand, SpellBaseEntity self) {
        ServerLevel level = player.serverLevel();
        RandomSource random = level.random;

        float randomX, randomY, randomZ;
        randomX = (random.nextFloat() * 1) - 0.5f;
        randomY = (random.nextFloat() * 1) - 0.5f;
        randomZ = (random.nextFloat() * 1) - 0.5f;

        int interval = 2;

        if(self.tickCount % (interval) == 0) {
            level.sendParticles(
                effect.value().createParticleOptions(new MobEffectInstance(effect, duration, amplifier)),
                self.getX(),
                self.getY() ,
                self.getZ(),
                random.nextIntBetweenInclusive(2, 3),
                randomX,
                randomY,
                randomZ,
                0
            );
        }
    }

    private final MobEffectInstance effectInstance;

    @Override
    public void entityHit(ServerPlayer player, ItemStack wand, LivingEntity entity, SpellBaseEntity self) {
        entity.addEffect(effectInstance);
    }

    @Override
    public void blockHit(ServerPlayer player, ItemStack wand, BlockHitResult block, SpellBaseEntity self) {
        if(!cloud) return;

        ServerLevel level = player.serverLevel();

        Vec3 location = block.getLocation();
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(level, location.x, location.y + 0.25f, location.z);

        areaeffectcloud.setOwner(self);
        areaeffectcloud.setRadius(3.0F);
        areaeffectcloud.setRadiusOnUse(-0.5F);
        areaeffectcloud.setWaitTime(10);
        areaeffectcloud.setDuration(duration);
        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());

        PotionContents contents = new PotionContents(
            Optional.empty(),
            Optional.empty(),
            List.of(effectInstance)
        );

        areaeffectcloud.setPotionContents(contents);

        level.addFreshEntity(areaeffectcloud);
    }
}
