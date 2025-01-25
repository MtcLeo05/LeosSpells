package com.leo.leos_spells;

import com.leo.leos_spells.init.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(LeosSpells.MODID)
public class LeosSpells {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "leos_spells";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public LeosSpells(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_MODE_TAB.register(modEventBus);

        ModSpells.SPELLS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);

        ModMenuTypes.MENUS.register(modEventBus);
        ModEntityDataSerializers.ENTITY_DATA_SERIALIZERS.register(modEventBus);
    }
}
