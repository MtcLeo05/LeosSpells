package com.leo.leos_spells.impl.init;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.impl.item.ScrollItem;
import com.leo.leos_spells.impl.item.WandItem;
import com.leo.leos_spells.api.spell.WandProperties;
import com.leo.leos_spells.util.ListUtil;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LSItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(LeosSpells.MODID);

    // Give basic wand with a spell for testing purposes
    // /give Dev leos_spells:basic_wand[leos_spells:stack_cooldown=0, leos_spells:wand_properties={baseCooldown:20,mana:0,maxMana:200,manaRegen:2, slotCount:4}, leos_spells:spell_holder=[{spellId:"leos_spells:red_bolt"}, {spellId:"leos_spells:cd_red_70"}]]
    public static final DeferredHolder<Item, Item> BASIC_WAND = ITEMS.register("basic_wand",
        () -> new WandItem(
            new Item.Properties()
                .stacksTo(1)
                .component(LSDataComponents.WAND_PROPERTIES, new WandProperties(20, 0, 200, 2, 4))
                .component(LSDataComponents.STACK_COOLDOWN, 0)
                .component(LSDataComponents.SPELL_HOLDER, ListUtil.of())
        )
    );

    public static final DeferredHolder<Item, Item> SPELL_SCROLL = ITEMS.register("spell_scroll",
        () -> new ScrollItem(
            new Item.Properties()
                .stacksTo(8)
                .component(LSDataComponents.SPELL_HOLDER, ListUtil.of())
        )
    );
}
