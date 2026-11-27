package com.sakalti.thesolar.client;

import com.sakalti.thesolar.DimensionMod;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SolarMobLayers {
    private static final String[] MODELS = {
            "lunar_crawler", "moon_stalker", "crystal_golem", "lunar_sentinel", "astral_wisp",
            "martian_scout", "red_stalker", "ares_soldier", "martian_brute", "ancient_automaton",
            "crimson_mage", "selene", "ares", "moon_beast", "blood_wyrm", "martian_beast"
    };
    private static final Map<String, ModelLayerLocation> LAYERS = new LinkedHashMap<>();

    static {
        for (String name : MODELS) {
            LAYERS.put(name, new ModelLayerLocation(DimensionMod.id(name), "main"));
        }
    }

    private SolarMobLayers() {}

    public static ModelLayerLocation layer(String name) {
        ModelLayerLocation layer = LAYERS.get(name);
        if (layer == null) throw new IllegalArgumentException("Unknown The Solar model: " + name);
        return layer;
    }

    public static void register() {
        LAYERS.forEach((name, layer) -> EntityModelLayerRegistry.registerModelLayer(layer, () -> SolarMobModel.createLayer(name)));
    }
}
