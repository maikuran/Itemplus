
package com.sakalti.abyssalion.mixin;

import com.sakalti.abyssalion.FloodUtil;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public abstract class WorldGenerationMixin {

    @Inject(
        method = "applyBiomeDecoration",
        at = @At("TAIL")
    )
    private void abyssalion$generateFloodedWorld(
        WorldGenLevel level,
        ChunkAccess chunk,
        StructureManager structures,
        CallbackInfo ci
    ) {
        if (level.getLevel().dimension() != Level.OVERWORLD) {
            return;
        }

        FloodUtil.floodChunk(level, chunk);
    }
}
