package com.sakalti.sakaplus.world;

import com.sakalti.sakaplus.block.ModBlocks2;

import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryEntry;

// 修正: ConstantInt から ConstantIntProvider へ変更
import net.minecraft.util.math.intprovider.ConstantIntProvider;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
// 修正: パラメータ名に Modifier が付く正しいクラス名へ変更
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
// 修正: PlacedFeatures の正しいパッケージパス
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public final class GreenyFeatures {

    private GreenyFeatures() {
    }

    public static final Identifier GREENY_TREE_ID =
            new Identifier("sakaplus", "greeny_tree");

    public static final Identifier GREENY_TREE_PLACED_ID =
            new Identifier("sakaplus", "greeny_tree_placed");

    public static final RegistryKey<PlacedFeature> GREENY_TREE_PLACED_KEY =
            RegistryKey.of(
                    Registry.PLACED_FEATURE_KEY,
                    GREENY_TREE_PLACED_ID
            );

    public static void register() {

        // 木の形状と使用ブロックを定義
        TreeFeatureConfig treeConfig =
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks2.GREENY_LOG),
                        new StraightTrunkPlacer(5, 2, 0),
                        BlockStateProvider.of(ModBlocks2.GREENY_LEAVES),
                        new BlobFoliagePlacer(
                                // 修正: ConstantIntProvider.create() を使用
                                ConstantIntProvider.create(2),
                                ConstantIntProvider.create(0),
                                3
                        ),
                        new TwoLayersFeatureSize(1, 0, 2)
                )
                .dirtProvider(
                        BlockStateProvider.of(Blocks.DIRT)
                )
                .ignoreVines()
                .build();

        // ConfiguredFeature 登録
        ConfiguredFeature<TreeFeatureConfig, ?> configuredTree =
                new ConfiguredFeature<>(Feature.TREE, treeConfig);

        Registry.register(
                BuiltinRegistries.CONFIGURED_FEATURE,
                GREENY_TREE_ID,
                configuredTree
        );

        // PlacedFeature 登録
        PlacedFeature placedTree = new PlacedFeature(
                RegistryEntry.of(configuredTree),
                List.of(
                        // 修正: 正しいクラス名に変更
                        CountPlacementModifier.of(4),
                        SquarePlacementModifier.spread(),
                        PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
                        BiomePlacementModifier.of()
                )
        );

        Registry.register(
                BuiltinRegistries.PLACED_FEATURE,
                GREENY_TREE_PLACED_ID,
                placedTree
        );
    }
}
