package com.leo.leos_spells.item;

import com.leo.leos_spells.init.ModDataComponents;
import com.leo.leos_spells.spell.SpellHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ScrollItem extends Item {
    public ScrollItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        if(!stack.has(ModDataComponents.SPELL_HOLDER)) return super.getName(stack);

        List<SpellHolder> spellHolders = stack.get(ModDataComponents.SPELL_HOLDER);
        if(spellHolders.isEmpty()) return super.getName(stack);

        return super.getName(stack).copy().append(": ").append(Component.translatable(spellHolders.getFirst().spellId().toLanguageKey()));
    }
}
