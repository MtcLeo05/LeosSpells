package com.leo.leos_spells.client;

import com.leo.leos_spells.LeosSpells;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModClientData {

    public static final Map<ResourceLocation, ResourceLocation> spriteCache = new HashMap<>();

    static {
        spriteCache.put(ResourceLocation.withDefaultNamespace("empty"), ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "textures/spell/sprites/empty.png"));
    }
}
