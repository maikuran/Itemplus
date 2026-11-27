package com.sakalti.thesolar;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

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
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        if (level.isClientSide() || !(entity instanceof ServerPlayer player) || player.isRemoved()) return;
        String destination = level.dimension().location().toString().equals(targetDimension)
                ? "minecraft:overworld" : targetDimension;
        int y = destination.equals("minecraft:overworld") ? 80 : destinationY;
        String command = "execute in " + destination + " run tp @s 0 " + y + " 0";
        player.getServer().getCommands().performPrefixedCommand(
                player.createCommandSourceStack().withPermission(4).withSuppressedOutput(), command);
    }
}
