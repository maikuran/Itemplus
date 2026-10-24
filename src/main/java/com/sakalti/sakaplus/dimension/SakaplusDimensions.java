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

    // ディメンションキー
    public static final RegistryKey<World> KRUZIVILIM_DIMENSION_KEY = RegistryKey.of(
            Registry.WORLD_KEY,
            new Identifier("sakaplus", "kruzivilim")
    );

    // ディメンションタイプキー
    public static final RegistryKey<DimensionType> KRUZIVILIM_DIMENSION_TYPE_KEY = RegistryKey.of(
            Registry.DIMENSION_TYPE_KEY,
            new Identifier("sakaplus", "kruzivilim")
    );

    // バイオームキー
    public static final RegistryKey<Biome> KRUZIVILIM_BIOME_KEY = RegistryKey.of(
            Registry.BIOME_KEY,
            new Identifier("sakaplus", "kruzivilim_biome")
    );

    public static final RegistryKey<Biome> GREENFOREST_BIOME_KEY = RegistryKey.of(
            Registry.BIOME_KEY,
            new Identifier("sakaplus", "greenforest")
    );

    
    // ノイズ設定キー（チャンク生成用）
    public static final RegistryKey<ChunkGeneratorSettings> KRUZIVILIM_NOISE_SETTINGS_KEY = RegistryKey.of(
            Registry.CHUNK_GENERATOR_SETTINGS_KEY,
            new Identifier("sakaplus", "kruzivilim_noise")
    );

    // 初期化処理
    public static void register() {
        LOGGER.info("Registering Sakaplus Dimension Keys...");

        // 通常ここで `Registry.register()` などを行うが、
        // ディメンション定義は Datapack で行うため、キーの初期化のみ。
        
        // 将来的にバイオームなどをプログラム的に追加したい場合はここに処理を書く。

        LOGGER.info("Sakaplus Dimensions registered: " + KRUZIVILIM_DIMENSION_KEY.getValue());
    }
}
