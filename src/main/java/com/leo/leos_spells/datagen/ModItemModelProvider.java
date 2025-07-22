package com.leo.leos_spells.datagen;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.impl.init.LSItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, LeosSpells.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(LSItems.BASIC_WAND)
            .override()
            .predicate(ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "cooldown"), 1)
            .model(simpleItemWithCross(LSItems.BASIC_WAND));

        withExistingParent(LSItems.SPELL_SCROLL.getId().getPath(), "item/generated")
            .texture("layer0", ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "item/scroll_base"))
            .texture("layer1", ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "item/scroll_writing"));
    }

    private ItemModelBuilder simpleItem(DeferredHolder<Item, Item> item) {
        return simpleItem(item.getId().getPath());
    }

    private ItemModelBuilder simpleItemWithCross(DeferredHolder<Item, Item> item) {
        return simpleItemWithCross(item.getId().getPath());
    }

    private ItemModelBuilder simpleItem(String name) {
        return withExistingParent(
            name,
            ResourceLocation.withDefaultNamespace("item/generated")
        ).texture(
            "layer0",
            ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "item/" + name)
        );
    }

    private ItemModelBuilder simpleItemWithCross(String name) {
        return withExistingParent(
            name + "_cross",
            ResourceLocation.withDefaultNamespace("item/generated")
        ).texture(
            "layer0",
            ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "item/" + name)
        ).texture(
            "layer1",
            ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "item/cross")
        );
    }
}
