package com.leo.leos_spells.spell;

import com.google.common.collect.BiMap;
import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.entity.SpellBaseEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class SpellType {

    public static @Nullable SpellType fromHolder(SpellHolder holder) {
        Optional<BiMap<ResourceLocation, SpellType>> spellOptional = SpellReloadListener.getAllSpells();
        if (spellOptional.isEmpty()) {
            LeosSpells.LOGGER.error("Could not find any registered spells!");
            return null;
        }

        BiMap<ResourceLocation, SpellType> registeredSpells = spellOptional.get();
        return registeredSpells.get(holder.spellId());
    }

    public final int color;
    public final float damage;
    public final float speed;
    public final float manaCost;
    public final ResourceLocation sprite;

    public SpellType(int color, float damage, float speed, float manaCost, ResourceLocation sprite) {
        this.color = color;
        this.damage = damage;
        this.speed = speed;
        this.manaCost = manaCost;
        this.sprite = sprite;
    }

    public abstract MapCodec<? extends SpellType> type();

    /**
     * Called when right-clicking a wand
     * @param wand the wand item
     * @param player the caster
     * @param spellHolder the SpellHolder of the casting slot
     * @param previousSpell the last casted spell
     * @param nextSpell the next spell to be cast
     */
    public abstract SpellBaseEntity cast(ItemStack wand, ServerPlayer player, SpellHolder spellHolder, @Nullable SpellBaseEntity previousSpell, @Nullable SpellType nextSpell);

    /**
     * Called when looping over all spells to collect mana and apply mana cost modifiers
     * @param previousMana the mana collected from all previous spells
     * @param player the caster
     */
    public abstract float collectMana(float previousMana, ServerPlayer player);

    /**
     * Called when a spell is equipped in a wand's slot
     * @param wand the wand item
     */
    public abstract void onSpellEquip(ItemStack wand);

    /**
     * Called when a spell is removed from a wand's slot
     * @param wand the wand item
     */
    public abstract void onSpellUnequip(ItemStack wand);

    /**
     * Called each tick after the spell has been cast
     * @param player the caster
     * @param wand the wand item used to cast
     * @param self the spell entity itself
     */
    public abstract void onSpellTick(ServerPlayer player, ItemStack wand, SpellBaseEntity self);

    /**
     * Called when the spell entity hits a LivingEntity
     * @param player the caster
     * @param wand the wand item used to cast
     * @param entity the hit entity
     * @param self the spell entity itself
     */
    public abstract void entityHit(ServerPlayer player, ItemStack wand, LivingEntity entity, SpellBaseEntity self);

    /**
     * Called when the spell entity hits a block that's not air or has fluids
     * @param player the caster
     * @param wand the wand used to cast
     * @param block the block hit result
     * @param self the spell entity itself
     */
    public abstract void blockHit(ServerPlayer player, ItemStack wand, BlockHitResult block, SpellBaseEntity self);
}
