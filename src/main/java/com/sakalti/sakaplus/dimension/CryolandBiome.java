package com.sakalti.sakaplus.dimension;

import com.sakalti.sakaplus.registry.ModEntities333; // 指定されたレジストリクラスをインポート

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.entity.SpawnGroup;

public class CryolandBiome {

    public static Biome createCryoland() {
        // 最初のJSONデータに基づいた環境色設定
        BiomeEffects effects = new BiomeEffects.Builder()
                .skyColor(10534911)      // 0xA0C0FF 相当
                .fogColor(12638463)      // 0xC0D0FF 相当
                .waterColor(4159204)     // 0x3F76E4 相当
                .waterFogColor(329011)   // 0x050533 相当
                .build();

        // モブスポーン設定
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        // 動物（CREATURE）はスポーンせず、モンスター（MONSTER）としてsakaplusの固有モブを設定
        spawnBuilder.spawn(
                SpawnGroup.MONSTER,
                new SpawnSettings.SpawnEntry(ModEntities333.CRYOWARRIOR, 20, 2, 4)
        );
        spawnBuilder.spawn(
                SpawnGroup.MONSTER,
                new SpawnSettings.SpawnEntry(ModEntities333.CRYOFREEZER, 10, 1, 3)
        );
        spawnBuilder.spawn(
                SpawnGroup.MONSTER,
                new SpawnSettings.SpawnEntry(ModEntities333.CRYOMAGE, 5, 1, 2)
        );

        // 空の地形生成設定（JSON通り、装飾や木などのFeaturesは追加しない）
        GenerationSettings.Builder genBuilder = new GenerationSettings.Builder();

        return new Biome.Builder()
                .precipitation(Biome.Precipitation.NONE) // 降水なし
                .temperature(0.3F)                       // 気温 0.3
                .downfall(0.0F)                          // 湿度 0.0
                .effects(effects)
                .spawnSettings(spawnBuilder.build())
                .generationSettings(genBuilder.build())
                .build();
    }
}
