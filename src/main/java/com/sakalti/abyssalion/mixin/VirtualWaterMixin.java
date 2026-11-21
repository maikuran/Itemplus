
package com.sakalti.abyssalion.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class VirtualWaterMixin {

    private static final int ABYSSALION_WATER_LEVEL = 384;

    @Inject(
        method = "getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void abyssalion$virtualWater(
        BlockPos pos,
        CallbackInfoReturnable<FluidState> cir
    ) {

        Level level = (Level)(Object)this;

        if (level.isClientSide() && level.dimension() != Level.OVERWORLD) {
            return;
        }

        if (level.dimension() != Level.OVERWORLD) {
            return;
        }

        if (pos.getY() > ABYSSALION_WATER_LEVEL) {
            return;
        }

        BlockState state = level.getBlockState(pos);

        // 既存の水・その他の流体状態を尊重
        FluidState existing = state.getFluidState();

        if (!existing.isEmpty()) {
            return;
        }

        // 空気も通常ブロックも仮想的な水没状態にする
        cir.setReturnValue(Fluids.WATER.getSource(false));
    }
}
