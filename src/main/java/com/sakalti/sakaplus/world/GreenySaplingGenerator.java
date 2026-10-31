
package com.sakalti.sakaplus.world;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.block.sapling.SaplingGenerator;

import java.util.Random;

public class GreenySaplingGenerator extends SaplingGenerator {

    @Override
    protected RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(
            Random random,
            boolean bees
    ) {
        return GreenyFeatures.GREENY_TREE_KEY;
    }
}
