package com.leo.leos_spells.impl.event;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.impl.client.ModClientData;
import com.leo.leos_spells.impl.client.entity.model.SpellBaseModel;
import com.leo.leos_spells.impl.client.entity.render.SpellBaseRender;
import com.leo.leos_spells.impl.client.screen.WandScreen;
import com.leo.leos_spells.impl.client.tooltip.ClientWandTooltipComponent;
import com.leo.leos_spells.impl.init.LSDataComponents;
import com.leo.leos_spells.impl.init.LSEntityTypes;
import com.leo.leos_spells.impl.init.LSItems;
import com.leo.leos_spells.impl.init.LSMenuTypes;
import com.leo.leos_spells.api.spell.SpellHolder;
import com.leo.leos_spells.api.spell.WandProperties;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.List;
import java.util.Objects;

@EventBusSubscriber(modid = LeosSpells.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBusClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(LSEntityTypes.SPELL_BASE.get(), SpellBaseRender::new);
    }

    @SubscribeEvent
    public static void registerLayersDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SpellBaseModel.LAYER_LOCATION, SpellBaseModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ItemProperties.register(
            LSItems.BASIC_WAND.get(),
            ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "cooldown"),
            (wand, level, entity, integer) -> {
                if(!wand.has(LSDataComponents.STACK_COOLDOWN)) {
                    return 0;
                }

                return Objects.requireNonNullElse(wand.get(LSDataComponents.STACK_COOLDOWN), 0) > 0? 1: 0;
            }
        );
    }

    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register(
            (stack, index) -> {
                if(index != 1) return -1;

                List<SpellHolder> holders = stack.get(LSDataComponents.SPELL_HOLDER);
                WandProperties properties = stack.get(LSDataComponents.WAND_PROPERTIES);

                if(properties != null || holders == null || holders.isEmpty()) return -1;
                if(!ModClientData.clientCache.containsKey(holders.getFirst().spellId())) return -1;

                return ModClientData.clientCache.get(holders.getFirst().spellId()).color();
            },
            LSItems.SPELL_SCROLL.get()
        );
    }

    @SubscribeEvent
    public static void registerTooltip(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(WandProperties.class, ClientWandTooltipComponent::new);
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(LSMenuTypes.WAND_MENU.get(), WandScreen::new);
    }
}
