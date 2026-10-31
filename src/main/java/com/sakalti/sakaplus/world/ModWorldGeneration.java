package com.sakalti.sakaplus.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class ModWorldGeneration {

    public static void register() {
        // 1. Dandelite Ore の Placed Feature キー
        RegistryKey<PlacedFeature> dandeliteOreKey = RegistryKey.of(
            Registry.PLACED_FEATURE_KEY,
            new Identifier("sakaplus", "dandelite_ore")
        );

        // 2. Rosite Ore の Placed Feature キー
        RegistryKey<PlacedFeature> rositeOreKey = RegistryKey.of(
            Registry.PLACED_FEATURE_KEY,
            new Identifier("sakaplus", "rosite_ore")
        );

        // Dandelite Ore をオーバーワールドの全バイオームの地下に生成
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(), 
            GenerationStep.Feature.UNDERGROUND_ORES, 
            dandeliteOreKey
        );

        // Rosite Ore をオーバーワールドの全バイオームの地下に生成
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(), 
            GenerationStep.Feature.UNDERGROUND_ORES, 
            rositeOreKey
        );
    }
}
