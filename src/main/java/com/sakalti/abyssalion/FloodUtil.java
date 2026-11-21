
package com.sakalti.abyssalion;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;

public final class FloodUtil {

    public static final int WATER_LEVEL = 384;

    private FloodUtil() {}

    public static void floodChunk(
        LevelHeightAccessor world,
        ChunkAccess chunk
    ) {
        int minY = world.getMinY();
        int maxY = Math.min(
            WATER_LEVEL + 1,
            world.getMaxY()
        );

        int startX = chunk.getPos().getMinBlockX();
        int startZ = chunk.getPos().getMinBlockZ();

        BlockPos.MutableBlockPos pos =
            new BlockPos.MutableBlockPos();

        for (int x = startX; x < startX + 16; x++) {
            for (int z = startZ; z < startZ + 16; z++) {
                for (int y = minY; y < maxY; y++) {

                    pos.set(x, y, z);

                    if (chunk.getBlockState(pos).isAir()) {
                        chunk.setBlockState(
                            pos,
                            Blocks.WATER.defaultBlockState(),
                            3
                        );
                    }
                }
            }
        }
    }
}
