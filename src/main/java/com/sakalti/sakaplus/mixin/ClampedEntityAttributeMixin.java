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
    private double minValue; // 属性の下限値フィールド

    @Shadow @Final @Mutable 
    private double maxValue; // 属性の上限値フィールド

    /**
     * すべての属性（Attributes）のインスタンス生成時に割り込み、
     * 下限値と上限値のロックを Java の限界値まで解放します。
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(String translationKey, double fallback, double min, double max, CallbackInfo ci) {
        // 下限値をマイナスの限界値まで拡張
        this.minValue = -Double.MAX_VALUE;
        
        // 上限値をプラスの限界値まで拡張（これで1024や2048の壁がすべて消滅します）
        this.maxValue = Double.MAX_VALUE;
    }
}
