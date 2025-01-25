package com.leo.leos_spells.init;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.menu.WandMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, LeosSpells.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<WandMenu>> WAND_MENU = registerMenuType(WandMenu::new, "wand_menu");

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>,MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }
}
