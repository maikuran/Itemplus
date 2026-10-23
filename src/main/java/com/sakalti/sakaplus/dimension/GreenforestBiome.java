package com.sakalti.sakaplus.dimension;

import com.sakalti.sakaplus.world.GreenyFeatures;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.gen.GenerationStep;

public class GreenforestBiome {

    public static Biome createGreenforest() {
        BiomeEffects effects = new BiomeEffects.Builder()
                .skyColor(0x78A7FF)
                .fogColor(0xC0D8B0)
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .build();

        // モブスポーン設定
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        spawnBuilder.spawn(
                SpawnGroup.CREATURE,
                new SpawnSettings.SpawnEntry(EntityType.COW, 8, 2, 4)
        );
        spawnBuilder.spawn(
                SpawnGroup.CREATURE,
                new SpawnSettings.SpawnEntry(EntityType.SHEEP, 10, 2, 4)
        );
        spawnBuilder.spawn(
                SpawnGroup.CREATURE,
                new SpawnSettings.SpawnEntry(EntityType.PIG, 8, 2, 4)
        );
        spawnBuilder.spawn(
                SpawnGroup.CREATURE,
                new SpawnSettings.SpawnEntry(EntityType.CHICKEN, 10, 2, 4)
        );
        spawnBuilder.spawn(
                SpawnGroup.CREATURE,
                new SpawnSettings.SpawnEntry(EntityType.RABBIT, 4, 2, 3)
        );

        // 地形・植生・鉱石の生成設定
        GenerationSettings.Builder genBuilder = new GenerationSettings.Builder();

        // Greenyの木を生成
        genBuilder.feature(
    　　   net.minecraft.world.gen.GenerationStep.Feature.VEGETAL_DECORATION, 
          GreenyFeatures.getPlacedTreeEntry() // ヘルパーからRegistryEntryを取得して渡す
        );

        return new Biome.Builder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(0.7F)
                .downfall(0.8F)
                .effects(effects)
                .spawnSettings(spawnBuilder.build())
                .generationSettings(genBuilder.build())
                .build();
    }
}
