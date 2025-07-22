package com.leo.leos_spells.api.spell;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.leo.leos_spells.impl.client.ClientSpellData;
import com.leo.leos_spells.impl.client.ModClientData;
import com.leo.leos_spells.impl.init.LSSpellTypes;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SpellReloadListener extends SimpleJsonResourceReloadListener {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Gson GSON_INSTANCE = new Gson();
    private static final String folder = "spells";
    private static SpellReloadListener INSTANCE;
    private BiMap<ResourceLocation, SpellType> spells = HashBiMap.create();

    private Map<ResourceLocation, ClientSpellData> spriteCache = new HashMap<>();

    private SpellReloadListener() {
        super(GSON_INSTANCE, folder);
    }

    public static SpellReloadListener create() {
        INSTANCE = new SpellReloadListener();
        return INSTANCE;
    }

    public static Optional<BiMap<ResourceLocation, SpellType>> getAllSpells() {
        if (INSTANCE == null) return Optional.empty();
        return Optional.of(INSTANCE.spells);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceList, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        BiMap<ResourceLocation, SpellType> partialMap = HashBiMap.create();

        for (ResourceLocation location : resourceList.keySet()) {
            JsonElement json = resourceList.get(location);
            LSSpellTypes.CODEC.parse(JsonOps.INSTANCE, json)
                // log error if parse fails
                .resultOrPartial(errorMsg -> LOGGER.warn("Could not decode spell with json id {} - error: {}", location, errorMsg))
                // add spell if parse succeeds
                .ifPresent(spell -> {
                    partialMap.put(location, spell);
                    spriteCache.put(location, new ClientSpellData(spell.sprite, spell.color));
                });
        }

        spells = partialMap;

        ModClientData.clientCache.putAll(spriteCache);
    }
}
