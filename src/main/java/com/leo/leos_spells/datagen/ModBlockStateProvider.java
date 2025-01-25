package com.leo.leos_spells.datagen;

import com.leo.leos_spells.LeosSpells;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockStateProvider extends BlockStateProvider {

    ExistingFileHelper existingFileHelper;

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, LeosSpells.MODID, exFileHelper);
        this.existingFileHelper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {

    }


    private void simpleAllCubeWithItem(DeferredHolder<Block, Block> block, String name) {
        simpleBlockWithItem(block.get(), models().cubeAll(name(block.get()), stripSetName(block.getId()).withPrefix("block/" + name + "/")));
    }

    private void simpleAllCubeWithItem(DeferredHolder<Block, Block> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
    private void simpleBlockWithItem(DeferredHolder<Block, Block> block) {
        simpleBlockWithItem(block.get(), new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "block/" + block.getId().getPath())));
    }

    private void simpleBlockItem(DeferredHolder<Block, Block> block) {
        simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "block/" + block.getId().getPath())));
    }

    private static ResourceLocation texture(DeferredHolder<Block, Block> block) {
        return texture(block.getId().getPath());
    }

    private static ResourceLocation texture(DeferredHolder<Block, Block> block, String prefix) {
        return texture(stripSetName(block.getId()).withPrefix(prefix + "/").getPath());
    }

    private static ResourceLocation texture(String name) {
        return ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "block/" + name);
    }

    private static ModelFile model(DeferredHolder<Block, Block> block) {
        return model(texture(block));
    }

    private static ModelFile model(ResourceLocation model) {
        return new ModelFile.UncheckedModelFile(model);
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private static ResourceLocation stripSetName(ResourceLocation name) {
        int index = name.getPath().indexOf('_');

        if (index == -1) {
            return name;
        }

        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), name.getPath().substring(index + 1));
    }

}
