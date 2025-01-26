package com.leo.leos_spells.client;

import com.leo.leos_spells.LeosSpells;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModClientData {
    public static final Map<ResourceLocation, ClientSpellData> clientCache = new HashMap<>();

    static {
        clientCache.put(ResourceLocation.withDefaultNamespace("empty"), new ClientSpellData(ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "textures/spell/sprites/empty.png"), -1));
    }
}
