package com.leo.leos_spells.datagen;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.impl.init.LSDataComponents;
import com.leo.leos_spells.impl.init.LSItems;
import com.leo.leos_spells.api.spell.SpellHolder;
import com.leo.leos_spells.util.ListUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        super.buildRecipes(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LSItems.BASIC_WAND.get().getDefaultInstance())
            .pattern("  L")
            .pattern(" S ")
            .pattern("S  ")
            .define('L', Items.LAPIS_LAZULI)
            .define('S', Items.STICK)
            .unlockedBy("has_item", has(Items.LAPIS_LAZULI))
            .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LSItems.SPELL_SCROLL.get().getDefaultInstance().copyWithCount(4))
            .pattern(" L ")
            .pattern("LPL")
            .pattern(" L ")
            .define('L', Items.LAPIS_LAZULI)
            .define('P', Items.PAPER)
            .unlockedBy("has_item", has(Items.LAPIS_LAZULI))
            .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, LSItems.SPELL_SCROLL.get())
                .requires(LSItems.SPELL_SCROLL.get())
                .unlockedBy("has_item", has(LSItems.SPELL_SCROLL.get()))
                .save(recipeOutput, "leos_spells:scroll_empty");

        ItemStack manaBolt = LSItems.SPELL_SCROLL.get().getDefaultInstance();
        manaBolt.set(LSDataComponents.SPELL_HOLDER, ListUtil.of(new SpellHolder(ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "mana_bolt"))));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, manaBolt)
            .pattern("GLG")
            .pattern("LSL")
            .pattern("GLG")
            .define('L', Items.LAPIS_LAZULI)
            .define('S', LSItems.SPELL_SCROLL.get())
            .define('G', Items.GUNPOWDER)
            .unlockedBy("has_item", has(LSItems.SPELL_SCROLL.get()))
            .save(recipeOutput, "leos_spells:spell/mana_bolt");


        ItemStack cooldown_20 = LSItems.SPELL_SCROLL.get().getDefaultInstance();
        cooldown_20.set(LSDataComponents.SPELL_HOLDER, ListUtil.of(new SpellHolder(ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "cooldown_20"))));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, cooldown_20)
            .pattern("CLC")
            .pattern("LSL")
            .pattern("CLC")
            .define('L', Items.LAPIS_LAZULI)
            .define('S', LSItems.SPELL_SCROLL.get())
            .define('C', Items.CLOCK)
            .unlockedBy("has_item", has(LSItems.SPELL_SCROLL.get()))
            .save(recipeOutput, "leos_spells:spell/cooldown_20");

        ItemStack fire_modifier = LSItems.SPELL_SCROLL.get().getDefaultInstance();
        fire_modifier.set(LSDataComponents.SPELL_HOLDER, ListUtil.of(new SpellHolder(ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "fire_modifier"))));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, fire_modifier)
            .pattern("CLC")
            .pattern("LSL")
            .pattern("CLC")
            .define('L', Items.LAPIS_LAZULI)
            .define('S', LSItems.SPELL_SCROLL.get())
            .define('C', Items.FIRE_CHARGE)
            .unlockedBy("has_item", has(LSItems.SPELL_SCROLL.get()))
            .save(recipeOutput, "leos_spells:spell/fire_modifier");

        ItemStack poison_modifier = LSItems.SPELL_SCROLL.get().getDefaultInstance();
        poison_modifier.set(LSDataComponents.SPELL_HOLDER, ListUtil.of(new SpellHolder(ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "poison_modifier"))));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, poison_modifier)
            .pattern("FLF")
            .pattern("LSL")
            .pattern("FLF")
            .define('L', Items.LAPIS_LAZULI)
            .define('S', LSItems.SPELL_SCROLL.get())
            .define('F', Items.FERMENTED_SPIDER_EYE)
            .unlockedBy("has_item", has(LSItems.SPELL_SCROLL.get()))
            .save(recipeOutput, "leos_spells:spell/poison_modifier");

        ItemStack max_mana_25 = LSItems.SPELL_SCROLL.get().getDefaultInstance();
        max_mana_25.set(LSDataComponents.SPELL_HOLDER, ListUtil.of(new SpellHolder(ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "max_mana_25"))));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, max_mana_25)
            .pattern("ALA")
            .pattern("LSL")
            .pattern("ALA")
            .define('L', Items.LAPIS_LAZULI)
            .define('S', LSItems.SPELL_SCROLL.get())
            .define('A', Items.AMETHYST_SHARD)
            .unlockedBy("has_item", has(LSItems.SPELL_SCROLL.get()))
            .save(recipeOutput, "leos_spells:spell/max_mana_25");
    }
}
