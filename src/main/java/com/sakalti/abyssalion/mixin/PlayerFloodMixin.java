
package com.sakalti.abyssalion.mixin;

import com.sakalti.abyssalion.FloodUtil;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class PlayerFloodMixin {

    @Unique
    private int abyssalion$lastChunkX = Integer.MIN_VALUE;

    @Unique
    private int abyssalion$lastChunkZ = Integer.MIN_VALUE;

    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    private void abyssalion$playerEntered(CallbackInfo ci) {

        ServerPlayer player = (ServerPlayer)(Object)this;
        ServerLevel world = player.serverLevel();

        if (world.dimension() != Level.OVERWORLD) {
            return;
        }

        int chunkX = player.chunkPosition().x;
        int chunkZ = player.chunkPosition().z;

        if (chunkX == abyssalion$lastChunkX &&
            chunkZ == abyssalion$lastChunkZ) {
            return;
        }

        abyssalion$lastChunkX = chunkX;
        abyssalion$lastChunkZ = chunkZ;

        LevelChunk chunk = world.getChunk(chunkX, chunkZ);

        FloodUtil.floodChunk(world, chunk);
    }
}
