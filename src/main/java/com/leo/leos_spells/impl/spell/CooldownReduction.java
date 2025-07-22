package com.leo.leos_spells.impl.spell;

import com.leo.leos_spells.impl.entity.SpellBaseEntity;
import com.leo.leos_spells.impl.init.LSDataComponents;
import com.leo.leos_spells.api.spell.SpellHolder;
import com.leo.leos_spells.api.spell.SpellType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CooldownReduction extends SpellType {
    public static final MapCodec<CooldownReduction> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.FLOAT.fieldOf("cooldown_reduction").forGetter(t -> t.multiplier),
            ResourceLocation.CODEC.fieldOf("sprite").forGetter(t -> t.sprite)
        ).apply(inst, CooldownReduction::new)
    );

    private final float multiplier;

    private float multiplier() {
        return multiplier;
    }

    public CooldownReduction(float multiplier, ResourceLocation sprite) {
        super(0xFFC29B00, 0, 0, 0, sprite);
        this.multiplier = multiplier;
    }

    @Override
    public MapCodec<? extends SpellType> type() {
        return CODEC;
    }

    @Override
    public SpellBaseEntity cast(ItemStack wand, ServerPlayer player, SpellHolder spellHolder, @Nullable SpellBaseEntity entity, @Nullable SpellType nextSpell) {
        int cooldown = wand.get(LSDataComponents.STACK_COOLDOWN);
        wand.set(LSDataComponents.STACK_COOLDOWN, (int) (cooldown - (cooldown * multiplier())));
        return null;
    }

    @Override
    public float collectMana(float previousMana, ServerPlayer player) {
        return previousMana + manaCost;
    }

    @Override
    public void onSpellEquip(ItemStack wand) {}

    @Override
    public void onSpellUnequip(ItemStack wand) {}

    @Override
    public void onSpellTick(ServerPlayer player, ItemStack wand, SpellBaseEntity self) {

    }

    @Override
    public void entityHit(ServerPlayer player, ItemStack wand, LivingEntity entity, SpellBaseEntity self) {}

    @Override
    public void blockHit(ServerPlayer player, ItemStack wand, BlockHitResult block, SpellBaseEntity self) {}
}
