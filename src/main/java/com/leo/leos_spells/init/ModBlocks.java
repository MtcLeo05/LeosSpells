package com.leo.leos_spells.init;

import com.leo.leos_spells.LeosSpells;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(LeosSpells.MODID);



    public static DeferredHolder<Block, Block> registerBlock(String name, Supplier<Block> block) {
        DeferredHolder<Block, Block> ret = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () ->
            new BlockItem(
                ret.get(),
                new Item.Properties()
            )
        );
        return ret;
    }
}
