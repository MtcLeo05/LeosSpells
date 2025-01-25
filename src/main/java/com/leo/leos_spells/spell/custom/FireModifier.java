package com.leo.leos_spells.spell.custom;

import com.leo.leos_spells.entity.SpellBaseEntity;
import com.leo.leos_spells.spell.SpellHolder;
import com.leo.leos_spells.spell.SpellType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FireModifier extends SpellType {

    public static final MapCodec<FireModifier> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.FLOAT.fieldOf("duration").forGetter((t) -> t.duration),
            Codec.FLOAT.fieldOf("fireDamage").forGetter((t) -> t.fireDamage),
            Codec.BOOL.fieldOf("persistent").forGetter(t -> t.persistent),
            ResourceLocation.CODEC.fieldOf("sprite").forGetter(t -> t.sprite)
        ).apply(inst, FireModifier::new)
    );

    private final float duration, fireDamage;
    private final boolean persistent;

    public FireModifier(float duration, float fireDamage, boolean persistent, ResourceLocation sprite) {
        super(0xFFFF0000, 0, 0, 0, sprite);
        this.duration = duration;
        this.fireDamage = fireDamage;
        this.persistent = persistent;
    }

    @Override
    public MapCodec<? extends SpellType> type() {
        return CODEC;
    }

    @Override
    public SpellBaseEntity cast(ItemStack wand, ServerPlayer player, SpellHolder spellHolder, @Nullable SpellBaseEntity previousSpell, @Nullable SpellType nextSpell) {
        if(previousSpell == null) return null;

        previousSpell.addSpell(spellHolder);
        previousSpell.setColor(color);
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
                ParticleTypes.FLAME,
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

    @Override
    public void entityHit(ServerPlayer player, ItemStack wand, LivingEntity entity, SpellBaseEntity self) {
        if(entity.fireImmune()) return;

        entity.igniteForSeconds(duration);

        entity.hurt(player.serverLevel().damageSources().inFire(), this.fireDamage);
    }

    @Override
    public void blockHit(ServerPlayer player, ItemStack wand, BlockHitResult block, SpellBaseEntity self) {
        if(!persistent) return;

        BlockPos above = block.getBlockPos().above();
        ServerLevel level = player.serverLevel();

        BlockState aboveState = level.getBlockState(above);

        if(aboveState.isAir() || aboveState.canBeReplaced()) {
            level.setBlock(above, Blocks.FIRE.defaultBlockState(), Block.UPDATE_ALL);
        }
    }
}
