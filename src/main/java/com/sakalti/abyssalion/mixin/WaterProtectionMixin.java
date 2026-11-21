
package com.sakalti.abyssalion.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class WaterProtectionMixin {

    @Inject(
        method = "canBeReplaced(Lnet/minecraft/world/level/material/Fluid;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void abyssalion$preventWaterDestruction(
        Fluid fluid,
        CallbackInfoReturnable<Boolean> cir
    ) {

        if (fluid != Fluids.WATER &&
            fluid != Fluids.FLOWING_WATER) {
            return;
        }

        BlockState state = (BlockState)(Object)this;

        // 空気への水の流入は許可
        if (state.isAir()) {
            return;
        }

        // 既存ブロックを水で置き換えることは禁止
        cir.setReturnValue(false);
    }
}
