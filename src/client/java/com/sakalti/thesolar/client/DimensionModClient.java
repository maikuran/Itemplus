package com.sakalti.thesolar.client;

import com.sakalti.thesolar.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public final class DimensionModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SolarMobLayers.register();
        SolarQuestKeybinds.register();
        EntityRenderers.register(ModEntities.LUNAR_CRAWLER, c -> new SolarMobRenderer(c, "lunar_crawler", .45f));
        EntityRenderers.register(ModEntities.MOON_STALKER, c -> new SolarMobRenderer(c, "moon_stalker", .5f));
        EntityRenderers.register(ModEntities.CRYSTAL_GOLEM, c -> new SolarMobRenderer(c, "crystal_golem", .8f));
        EntityRenderers.register(ModEntities.LUNAR_SENTINEL, c -> new SolarMobRenderer(c, "lunar_sentinel", .6f));
        EntityRenderers.register(ModEntities.ASTRAL_WISP, c -> new SolarMobRenderer(c, "astral_wisp", .25f));
        EntityRenderers.register(ModEntities.MARTIAN_SCOUT, c -> new SolarMobRenderer(c, "martian_scout", .5f));
        EntityRenderers.register(ModEntities.RED_STALKER, c -> new SolarMobRenderer(c, "red_stalker", .55f));
        EntityRenderers.register(ModEntities.ARES_SOLDIER, c -> new SolarMobRenderer(c, "ares_soldier", .55f));
        EntityRenderers.register(ModEntities.MARTIAN_BRUTE, c -> new SolarMobRenderer(c, "martian_brute", .9f));
        EntityRenderers.register(ModEntities.ANCIENT_AUTOMATON, c -> new SolarMobRenderer(c, "ancient_automaton", .8f));
        EntityRenderers.register(ModEntities.CRIMSON_MAGE, c -> new SolarMobRenderer(c, "crimson_mage", .5f));
        EntityRenderers.register(ModEntities.MOON_BEAST, c -> new SolarMobRenderer(c, "moon_beast", 1.0f));
        EntityRenderers.register(ModEntities.BLOOD_WYRM, c -> new SolarMobRenderer(c, "blood_wyrm", 1.0f));
        EntityRenderers.register(ModEntities.MARTIAN_BEAST, c -> new SolarMobRenderer(c, "martian_beast", 1.0f));
        EntityRenderers.register(ModEntities.SELENE, c -> new SolarMobRenderer(c, "selene", 1.0f));
        EntityRenderers.register(ModEntities.ARES, c -> new SolarMobRenderer(c, "ares", 1.1f));
    }
}
