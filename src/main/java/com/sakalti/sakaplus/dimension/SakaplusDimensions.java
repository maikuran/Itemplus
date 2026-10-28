package com.sakalti.sakaplus.dimension;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SakaplusDimensions {

    public static final Logger LOGGER = LoggerFactory.getLogger("Sakaplus");
    public static final String MOD_ID = "sakaplus";

    // ==========================================
    // 1. ディメンション関連のキー
    // ==========================================
    public static final RegistryKey<World> KRUZIVILIM_DIMENSION_KEY = 
            registerWorldKey("kruzivilim");
    public static final RegistryKey<DimensionType> KRUZIVILIM_DIMENSION_TYPE_KEY = 
            registerDimensionTypeKey("kruzivilim");

    // クライオヴィリム用のディメンションキーを追加
    public static final RegistryKey<World> CRYOVILIM_DIMENSION_KEY = 
            registerWorldKey("cryovilim");
    public static final RegistryKey<DimensionType> CRYOVILIM_DIMENSION_TYPE_KEY = 
            registerDimensionTypeKey("cryovilim");

    // ==========================================
    // 2. バイオーム関連のキー
    // ==========================================
    public static final RegistryKey<Biome> KRUZIVILIM_BIOME_KEY = 
            registerBiomeKey("kruzivilim_biome");
    public static final RegistryKey<Biome> GREENFOREST_BIOME_KEY = 
            registerBiomeKey("greenforest");
    public static final RegistryKey<Biome> CRYOLAND_BIOME_KEY = 
            registerBiomeKey("cryoland");

    // ==========================================
    // 3. ノイズ設定（チャンク生成）用のキー
    // ==========================================
    public static final RegistryKey<ChunkGeneratorSettings> KRUZIVILIM_NOISE_SETTINGS_KEY = 
            registerNoiseSettingsKey("kruzivilim_noise");
    public static final RegistryKey<ChunkGeneratorSettings> CRYOLAND_NOISE_SETTINGS_KEY = 
            registerNoiseSettingsKey("cryoland_noise");

    // ==========================================
    // キー生成用の共通関数（メソッド）
    // ==========================================
    private static RegistryKey<World> registerWorldKey(String path) {
        return RegistryKey.of(Registry.WORLD_KEY, new Identifier(MOD_ID, path));
    }

    private static RegistryKey<DimensionType> registerDimensionTypeKey(String path) {
        return RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier(MOD_ID, path));
    }

    private static RegistryKey<Biome> registerBiomeKey(String path) {
        return RegistryKey.of(Registry.BIOME_KEY, new Identifier(MOD_ID, path));
    }

    private static RegistryKey<ChunkGeneratorSettings> registerNoiseSettingsKey(String path) {
        return RegistryKey.of(Registry.CHUNK_GENERATOR_SETTINGS_KEY, new Identifier(MOD_ID, path));
    }

    // ==========================================
    // 初期化処理
    // ==========================================
    public static void register() {
        LOGGER.info("Registering Sakaplus Dimension, Biome, and Noise Keys...");

        // クラスがロードされた時点で上の static フィールドが実行されるため、
        // ログ出力をして正常にキーが準備されたことを確認します。
        LOGGER.info("Sakaplus Dimensions initialized successfully.");
    }
}
