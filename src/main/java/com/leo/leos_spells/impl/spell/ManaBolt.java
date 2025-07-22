package com.leo.leos_spells.impl.spell;

import com.leo.leos_spells.impl.entity.SpellBaseEntity;
import com.leo.leos_spells.impl.init.LSEntityTypes;
import com.leo.leos_spells.api.spell.SpellHolder;
import com.leo.leos_spells.api.spell.SpellType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ManaBolt extends SpellType {
    public static final MapCodec<ManaBolt> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.INT.fieldOf("color").forGetter((t) -> t.color),
            Codec.FLOAT.fieldOf("damage").forGetter((t) -> t.damage),
            Codec.FLOAT.fieldOf("speed").forGetter((t) -> t.speed),
            Codec.FLOAT.fieldOf("mana_cost").forGetter((t) -> t.manaCost),
            ResourceLocation.CODEC.fieldOf("sprite").forGetter(t -> t.sprite)
        ).apply(inst, ManaBolt::new)
    );

    public ManaBolt(int color, float damage, float speed, float manaCost, ResourceLocation sprite) {
        super(color, damage, speed, manaCost, sprite);
    }

    @Override
    public MapCodec<? extends SpellType> type() {
        return CODEC;
    }


    @Override
    public SpellBaseEntity cast(ItemStack wand, ServerPlayer player, SpellHolder spellHolder, @Nullable SpellBaseEntity previousSpell, @Nullable SpellType nextSpell) {
        if(previousSpell != null) return previousSpell;

        SpellBaseEntity spell = LSEntityTypes.SPELL_BASE.get().create(player.serverLevel());
        Vec3 offset = player.getEyePosition().add(0, -0.25, 0).add(player.getForward().normalize().scale(2));

        spell.setPos(spell.position().add(offset));
        spell.setXRot(-player.getXRot());
        spell.setYRot(player.getYRot());

        spell.shoot(player, this.speed);
        spell.setColor(this.color);
        spell.setOwner(player.getUUID());
        spell.addSpell(spellHolder);

        return spell;
    }


    @Override
    public float collectMana(float previousMana, ServerPlayer player) {
        return previousMana + manaCost;
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

        if (self.getMoveDirection() != null) {
            self.setDeltaMovement(self.getMoveDirection());
        }

        float randomX, randomY, randomZ;
        randomX = (random.nextFloat() * 1) - 0.5f;
        randomY = (random.nextFloat() * 1) - 0.5f;
        randomZ = (random.nextFloat() * 1) - 0.5f;

        int interval = 2;
        if(self.tickCount % (interval / speed) == 0) {
            level.sendParticles(
                getParticles(self),
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

        if (self.tickCount <= 20) return;
        if (self.getDeltaMovement().equals(Vec3.ZERO) || self.tickCount >= 100) self.discard();
    }

    @Override
    public void entityHit(ServerPlayer player, ItemStack wand, LivingEntity entity, SpellBaseEntity self) {
        ServerLevel level = player.serverLevel();
        RandomSource random = level.random;

        entity.hurt(level.damageSources().magic(), damage);

        float randomX, randomY, randomZ;
        randomX = (random.nextFloat() * 4) - 2;
        randomY = (random.nextFloat() * 2) - 1;
        randomZ = (random.nextFloat() * 4) - 2;

        level.sendParticles(
            getParticles(self),
            entity.getX(),
            entity.getY() + (entity.getEyeHeight() / 2),
            entity.getZ(),
            random.nextIntBetweenInclusive(4, 8),
            randomX,
            randomY,
            randomZ,
            0
        );

        self.discard();
    }


    @Override
    public void blockHit(ServerPlayer player, ItemStack wand, BlockHitResult block, SpellBaseEntity self) {
        ServerLevel level = player.serverLevel();
        RandomSource random = level.random;

        float randomX, randomY, randomZ;
        randomX = (random.nextFloat() * 4) - 2;
        randomY = (random.nextFloat() * 2) - 1;
        randomZ = (random.nextFloat() * 4) - 2;

        level.sendParticles(
            getParticles(self),
            block.getLocation().x,
            block.getLocation().y + 0.5f,
            block.getLocation().z,
            random.nextIntBetweenInclusive(4, 8),
            randomX,
            randomY,
            randomZ,
            0
        );

        self.discard();
    }

    private ParticleOptions getParticles(SpellBaseEntity self) {
        return new DustParticleOptions(
            new Vector3f(
                ((self.getColor() >> 16) & 0xFF) / 255f,
                ((self.getColor() >> 8) & 0xFF) / 255f,
                ((self.getColor()) & 0xFF) / 255f
            ),
            1.5f
        );
    }
}
