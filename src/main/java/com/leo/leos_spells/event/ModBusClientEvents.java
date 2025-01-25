package com.leo.leos_spells.event;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.client.entity.model.SpellBaseModel;
import com.leo.leos_spells.client.entity.render.SpellBaseRender;
import com.leo.leos_spells.client.screen.WandScreen;
import com.leo.leos_spells.client.tooltip.ClientWandTooltipComponent;
import com.leo.leos_spells.init.ModDataComponents;
import com.leo.leos_spells.init.ModEntityTypes;
import com.leo.leos_spells.init.ModItems;
import com.leo.leos_spells.init.ModMenuTypes;
import com.leo.leos_spells.spell.WandProperties;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.Objects;

@EventBusSubscriber(modid = LeosSpells.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBusClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.SPELL_BASE.get(), SpellBaseRender::new);
    }

    @SubscribeEvent
    public static void registerLayersDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SpellBaseModel.LAYER_LOCATION, SpellBaseModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ItemProperties.register(
            ModItems.BASIC_WAND.get(),
            ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "cooldown"),
            (wand, level, entity, integer) -> {
                if(!wand.has(ModDataComponents.STACK_COOLDOWN)) {
                    return 0;
                }

                return Objects.requireNonNullElse(wand.get(ModDataComponents.STACK_COOLDOWN), 0) > 0? 1: 0;
            }
        );
    }

    @SubscribeEvent
    public static void registerTooltip(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(WandProperties.class, ClientWandTooltipComponent::new);
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.WAND_MENU.get(), WandScreen::new);
    }
}
