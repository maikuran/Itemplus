package com.sakalti.thesolar;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
public final class SolarOreWorldgen {
    private SolarOreWorldgen() {}
    public static void register() {
        for (String n : new String[]{"cobalt","limonite","jade","ruby","krillul"})
            BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),GenerationStep.Decoration.UNDERGROUND_ORES,ResourceKey.create(Registries.PLACED_FEATURE,DimensionMod.id(n+"_ore")));
        for (String n : new String[]{"sitherian","hellphyros_remnantius_forgotina"})
            BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(),GenerationStep.Decoration.UNDERGROUND_ORES,ResourceKey.create(Registries.PLACED_FEATURE,DimensionMod.id(n+"_ore")));
    }
}
