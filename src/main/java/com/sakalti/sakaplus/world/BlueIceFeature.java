
package com.sakalti.sakaplus.world;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import net.minecraft.world.gen.placementmodifier.HeightmapPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

public class BlueIceFeature extends Feature<DefaultFeatureConfig> {

    private static final String MOD_ID = "sakaplus";

    private static final BlockState BLUE_ICE =
            Blocks.BLUE_ICE.getDefaultState();

    private final int minHeight;
    private final int maxHeight;

    private final int minRadius;
    private final int maxRadius;

    private final float irregularity;

    public BlueIceFeature(
            int minHeight,
            int maxHeight,
            int minRadius,
            int maxRadius,
            float irregularity
    ) {
        super(DefaultFeatureConfig.CODEC);

        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.irregularity = irregularity;
    }

    // =====================================================
    // GENERATION
    // =====================================================

    @Override
    public boolean generate(
            FeatureContext<DefaultFeatureConfig> context
    ) {

        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();

        BlockPos ground = origin.down();

        if (!world.getBlockState(ground)
                .getMaterial().blocksMovement()) {
            return false;
        }

        int height = minHeight +
                random.nextInt(maxHeight - minHeight + 1);

        int baseRadius = minRadius +
                random.nextInt(maxRadius - minRadius + 1);

        float ellipse = 0.80F + random.nextFloat() * 0.35F;

        int radiusX = baseRadius;
        int radiusZ = Math.max(
                2,
                Math.round(baseRadius * ellipse)
        );

        float leanX = random.nextFloat() * 2.0F - 1.0F;
        float leanZ = random.nextFloat() * 2.0F - 1.0F;

        float phase = random.nextFloat() * 6.283185F;

        if (origin.getY() + height >= world.getTopY()) {
            return false;
        }

        BlockPos.Mutable pos = new BlockPos.Mutable();

        for (int y = 0; y < height; y++) {

            float progress = y / (float) (height - 1);

            // Natural taper
            float taper = (float) Math.pow(
                    1.0F - progress,
                    0.76F
            );

            // Wider bottom
            float baseFlare =
                    1.0F + 0.13F *
                    (float) Math.exp(-y / 3.5F);

            float layerNoise =
                    (random.nextFloat() - 0.5F) *
                    irregularity;

            float currentRadiusX = Math.max(
                    0.55F,
                    radiusX * taper * baseFlare *
                    (1.0F + layerNoise)
            );

            float currentRadiusZ = Math.max(
                    0.55F,
                    radiusZ * taper * baseFlare *
                    (1.0F + layerNoise)
            );

            // Curved centerline
            float centerX = leanX * progress * progress;
            float centerZ = leanZ * progress * progress;

            int extentX = (int) Math.ceil(currentRadiusX + 1);
            int extentZ = (int) Math.ceil(currentRadiusZ + 1);

            for (int x = -extentX; x <= extentX; x++) {

                for (int z = -extentZ; z <= extentZ; z++) {

                    float dx = x - centerX;
                    float dz = z - centerZ;

                    float normalizedX = dx / currentRadiusX;
                    float normalizedZ = dz / currentRadiusZ;

                    float distance = (float) Math.sqrt(
                            normalizedX * normalizedX +
                            normalizedZ * normalizedZ
                    );

                    float angle = (float) Math.atan2(dz, dx);

                    float edgeNoise =
                            (float) Math.sin(angle * 3.0F + phase)
                                    * 0.075F
                            +
                            (float) Math.sin(angle * 7.0F - phase)
                                    * 0.045F
                            +
                            (float) Math.sin(
                                    angle * 11.0F + progress * 5.0F
                            ) * 0.025F;

                    if (distance > 1.0F + edgeNoise) {
                        continue;
                    }

                    pos.set(
                            origin.getX() + x,
                            origin.getY() + y,
                            origin.getZ() + z
                    );

                    BlockState existing = world.getBlockState(pos);

                    if (existing.isAir() ||
                            existing.getMaterial().isReplaceable()) {

                        world.setBlockState(
                                pos,
                                BLUE_ICE,
                                2
                        );
                    }
                }
            }
        }

        return true;
    }

    // =====================================================
    // REGISTRATION
    // =====================================================

    public static void register() {

        registerVariant(
                "blue_ice_pillar_low",
                8, 14,
                2, 4,
                0.22F,
                8
        );

        registerVariant(
                "blue_ice_pillar_medium",
                15, 24,
                4, 6,
                0.30F,
                25
        );

        registerVariant(
                "blue_ice_pillar_high",
                25, 35,
                5, 8,
                0.38F,
                70
        );
    }

    // =====================================================
    // CONFIGURED + PLACED FEATURE REGISTRATION
    // =====================================================

    private static void registerVariant(
            String name,
            int minHeight,
            int maxHeight,
            int minRadius,
            int maxRadius,
            float irregularity,
            int rarity
    ) {

        Identifier id = new Identifier(MOD_ID, name);

        // Register Feature
        BlueIceFeature feature = Registry.register(
                Registry.FEATURE,
                id,
                new BlueIceFeature(
                        minHeight,
                        maxHeight,
                        minRadius,
                        maxRadius,
                        irregularity
                )
        );

        // Configured Feature
        ConfiguredFeature<DefaultFeatureConfig, BlueIceFeature>
                configured = new ConfiguredFeature<>(
                        feature,
                        DefaultFeatureConfig.INSTANCE
                );

        RegistryEntry<ConfiguredFeature<?, ?>> configuredEntry =
                BuiltinRegistries.add(
                        BuiltinRegistries.CONFIGURED_FEATURE,
                        id,
                        (ConfiguredFeature<?, ?>) configured
                );

        // Placement modifiers
        List<PlacementModifier> modifiers = Arrays.asList(
                RarityFilterPlacementModifier.of(rarity),
                SquarePlacementModifier.of(),
                HeightmapPlacementModifier.of(
                        Heightmap.Type.WORLD_SURFACE_WG
                )
        );

        // Placed Feature
        PlacedFeature placed = new PlacedFeature(
                configuredEntry,
                modifiers
        );

        BuiltinRegistries.add(
                BuiltinRegistries.PLACED_FEATURE,
                id,
                placed
        );
    }
}
