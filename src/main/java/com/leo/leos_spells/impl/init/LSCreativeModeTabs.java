package com.leo.leos_spells.impl.init;

import com.google.common.collect.BiMap;
import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.api.spell.SpellHolder;
import com.leo.leos_spells.api.spell.SpellReloadListener;
import com.leo.leos_spells.api.spell.SpellType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LSCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LeosSpells.MODID);

    public static final Supplier<CreativeModeTab> LEOS_SPELLS = CREATIVE_MODE_TAB.register(LeosSpells.MODID + "_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(LSItems.BASIC_WAND.get()))
                .title(Component.translatable(LeosSpells.MODID + ".itemGroup.items"))
                .displayItems((itemDisplayParameters, output) -> {
                    output.accept(LSItems.BASIC_WAND.get().getDefaultInstance());
                    output.accept(LSItems.SPELL_SCROLL.get().getDefaultInstance());

                    if(SpellReloadListener.getAllSpells().isEmpty()) return;

                    SpellReloadListener.getAllSpells().get()
                        .keySet()
                        .forEach(t -> {
                            ItemStack item = new ItemStack(LSItems.SPELL_SCROLL);

                            item.set(LSDataComponents.SPELL_HOLDER, List.of(new SpellHolder(t)));

                            output.accept(item);
                        });

                }).build());
}
