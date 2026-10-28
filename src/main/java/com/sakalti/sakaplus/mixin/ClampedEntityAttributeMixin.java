
package com.sakalti.sakaplus.mixin;

import net.minecraft.entity.attribute.ClampedEntityAttribute;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClampedEntityAttribute.class)
public abstract class ClampedEntityAttributeMixin {

    /**
     * 属性値のクランプ制限を解除
     */
    @Inject(
        method = "clamp(D)D",
        at = @At("HEAD"),
        cancellable = true
    )
    private void sakaplus$unlimitedClamp(
            double value,
            CallbackInfoReturnable<Double> cir
    ) {
        cir.setReturnValue(value);
    }

    /**
     * 最大値を実質無制限にする
     */
    @Inject(
        method = "getMaxValue()D",
        at = @At("HEAD"),
        cancellable = true
    )
    private void sakaplus$unlimitedMax(
            CallbackInfoReturnable<Double> cir
    ) {
        cir.setReturnValue(Double.MAX_VALUE);
    }

    /**
     * 最小値を実質無制限にする
     */
    @Inject(
        method = "getMinValue()D",
        at = @At("HEAD"),
        cancellable = true
    )
    private void sakaplus$unlimitedMin(
            CallbackInfoReturnable<Double> cir
    ) {
        cir.setReturnValue(-Double.MAX_VALUE);
    }
}
