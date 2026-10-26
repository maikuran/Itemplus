package com.sakalti.sakaplus.mixin;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClampedEntityAttribute.class)
public class ClampedEntityAttributeMixin {

    /**
     * 値を制限する clamp メソッドの処理を完全にバイパスし、
     * 入力された値をそのまま（制限せずに）返します。
     */
    @Inject(method = "clamp(D)D", at = @At("HEAD"), cancellable = true)
    private void onClamp(double value, CallbackInfoReturnable<Double> cir) {
        // 制限をかけずに、そのままの値を返して終了する
        cir.setReturnValue(value);
    }
}
