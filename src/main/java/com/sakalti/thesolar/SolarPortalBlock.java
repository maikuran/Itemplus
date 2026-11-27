package com.sakalti.thesolar;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.InsideBlockEffectApplier;

/** A placeable one-block gateway. Walking into it transfers players between dimensions. */
public final class SolarPortalBlock extends Block {
    private final String targetDimension;
    private final int destinationY;

    public SolarPortalBlock(Properties properties, String targetDimension, int destinationY) {
        super(properties);
        this.targetDimension = targetDimension;
        this.destinationY = destinationY;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity,
                                InsideBlockEffectApplier effectApplier, boolean fromAbove) {
        if (level.isClientSide() || !(entity instanceof ServerPlayer player) || player.isRemoved()) return;
        if (!(level instanceof ServerLevel sourceLevel)) return;

        var server = sourceLevel.getServer();
        var targetKey = sourceLevel.dimension().identifier().toString().equals(targetDimension)
                ? Level.OVERWORLD
                : targetDimension.equals("thesolar:moon") ? DimensionMod.MOON_DIMENSION : DimensionMod.MARS_DIMENSION;
        ServerLevel destination = server.getLevel(targetKey);
        if (destination == null || destination == sourceLevel) return;

        double x = destination == server.overworld() ? 0.5 : 0.5;
        double z = destination == server.overworld() ? 0.5 : 0.5;
        double y = destination == server.overworld() ? 80.0 : destinationY;
        player.teleportTo(destination, x, y, z, java.util.Set.of(), player.getYRot(), player.getXRot(), true);
    }
}
