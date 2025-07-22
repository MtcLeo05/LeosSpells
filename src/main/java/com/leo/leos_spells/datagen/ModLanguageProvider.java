package com.leo.leos_spells.datagen;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.impl.init.LSItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, LeosSpells.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        this.add(LeosSpells.MODID + ".itemGroup.items", "Leo's Spells");

        this.add(LSItems.BASIC_WAND.get(), "Basic Wand");
        this.add(LSItems.SPELL_SCROLL.get(), "Spell Scroll");

        this.add("tooltip." + LeosSpells.MODID + ".wand.shiftInfo", "---Press shift for Spell info---");
        this.add("tooltip." + LeosSpells.MODID + ".wand.emptySpells", "No spells inserted!");
        this.add("tooltip." + LeosSpells.MODID + ".wand.cd", "CD: %dt");

        this.add("minecraft.empty", "Empty");
        this.add(LeosSpells.MODID + ".mana_bolt", "Mana Bolt");
        this.add(LeosSpells.MODID + ".cooldown_20", "Cooldown Reduction 20%");
        this.add(LeosSpells.MODID + ".fire_modifier", "Fire Modifier");
        this.add(LeosSpells.MODID + ".poison_modifier", "Poison Modifier");
        this.add(LeosSpells.MODID + ".seething_poison_modifier", "Seething Poison Modifier");
        this.add(LeosSpells.MODID + ".max_mana_25", "Max Mana Increase: +25");

        this.add("gui." + LeosSpells.MODID + ".wand", "Wand Editing");
    }


    /**
     * Capitalizes first letter of a string
     *
     * @param input the string to capitalize e.g. "alpha"
     * @return the string capitalized e.g. "Alpha"
     */
    public static String cFL(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}