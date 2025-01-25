package com.leo.leos_spells.init;

import com.leo.leos_spells.LeosSpells;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LeosSpells.MODID);

    public static final Supplier<CreativeModeTab> LEOS_SPELLS = CREATIVE_MODE_TAB.register(LeosSpells.MODID + "_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BASIC_WAND.get()))
                .title(Component.translatable(LeosSpells.MODID + ".itemGroup.items"))
                .displayItems((itemDisplayParameters, output) -> {
                    output.accept(ModItems.BASIC_WAND.get().getDefaultInstance());
                    output.accept(ModItems.SPELL_SCROLL.get().getDefaultInstance());
                }).build());
}
