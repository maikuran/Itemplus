package com.sakalti.sakaplus.mixin;

import net.minecraft.entity.attribute.EntityAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAttribute.class) // 親クラスである EntityAttribute をターゲットにする
public class EntityAttributeMixin {

    /**
     * 値を制限する clamp メソッドの処理を完全にバイパスし、
     * 入力された値をそのまま（制限せずに）返します。
     */
    @Inject(method = "clamp(D)D", at = @At("HEAD"), cancellable = true)
    private void onClamp(double value, CallbackInfoReturnable<Double> cir) {
        // 全ての属性（体力、攻撃力、速度など）のクランプ処理をバイパスして生の値を返す
        cir.setReturnValue(value);
    }
}
