package com.sakalti.sakaplus.mixin;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClampedEntityAttribute.class)
public class ClampedEntityAttributeMixin {

    @Shadow @Final @Mutable 
    private double maxValue; // 属性の最大値を保持する内部フィールド

    @Shadow @Final @Mutable 
    private double minValue; // 属性の最小値を保持する内部フィールド

    /**
     * クラスのインスタンスが生成された直後（RETURN）に、
     * 制限値を限界まで引き上げます。
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruct(String translationKey, double fallback, double min, double max, CallbackInfo ci) {
        // 最小値の制限をなくす（必要に応じて。例: 攻撃速度などをマイナスにしたい場合）
        if (this.minValue > -Double.MAX_VALUE) {
            this.minValue = -Double.MAX_VALUE;
        }
        // 最大値の制限を実質無限（Doubleの最大値）に上書き
        this.maxValue = Double.MAX_VALUE;
    }
}
