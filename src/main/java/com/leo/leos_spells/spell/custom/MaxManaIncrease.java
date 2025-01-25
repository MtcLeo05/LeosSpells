package com.leo.leos_spells.spell.custom;

import com.leo.leos_spells.entity.SpellBaseEntity;
import com.leo.leos_spells.init.ModDataComponents;
import com.leo.leos_spells.spell.SpellHolder;
import com.leo.leos_spells.spell.SpellType;
import com.leo.leos_spells.spell.WandProperties;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class MaxManaIncrease extends SpellType {
    public static final MapCodec<MaxManaIncrease> CODEC = RecordCodecBuilder.mapCodec(
        inst -> inst.group(
            Codec.FLOAT.fieldOf("increase").forGetter(t -> t.increase),
            ResourceLocation.CODEC.fieldOf("sprite").forGetter(t -> t.sprite)
        ).apply(inst, MaxManaIncrease::new)
    );

    private final float increase;

    public MaxManaIncrease(float increase, ResourceLocation sprite) {
        super(0, 0, 0, 0, sprite);
        this.increase = increase;
    }

    @Override
    public MapCodec<? extends SpellType> type() {
        return CODEC;
    }

    @Override
    public SpellBaseEntity cast(ItemStack wand, ServerPlayer player, SpellHolder spellHolder, @Nullable SpellBaseEntity entity, @Nullable SpellType nextSpell) {
        return entity;
    }

    @Override
    public float collectMana(float previousMana, ServerPlayer player) {
        return previousMana;
    }

    @Override
    public void onSpellEquip(ItemStack wand) {
        WandProperties wandProperties = wand.get(ModDataComponents.WAND_PROPERTIES);

        wandProperties = wandProperties.addMaxMana(increase);
        wand.set(ModDataComponents.WAND_PROPERTIES, wandProperties);
    }

    @Override
    public void onSpellUnequip(ItemStack wand) {
        WandProperties wandProperties = wand.get(ModDataComponents.WAND_PROPERTIES);

        wandProperties = wandProperties.removeMaxMana(increase);
        wand.set(ModDataComponents.WAND_PROPERTIES, wandProperties);
    }

    @Override
    public void onSpellTick(ServerPlayer player, ItemStack wand, SpellBaseEntity self) {

    }

    @Override
    public void entityHit(ServerPlayer player, ItemStack wand, LivingEntity entity, SpellBaseEntity self) {}

    @Override
    public void blockHit(ServerPlayer player, ItemStack wand, BlockHitResult block, SpellBaseEntity self) {}
}
