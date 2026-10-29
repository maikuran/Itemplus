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
public abstract class ClampedEntityAttributeMixin {

    // @Shadow, @Final, @Mutable をセットで使うことで、
    // 本来変更できない再代入不可（final）のフィールドを強制書き換え可能にします。
    @Shadow @Final @Mutable private double minValue;
    @Shadow @Final @Mutable private double maxValue;

    /**
     * コンストラクタの処理直後（TAIL）を狙い、
     * 設定された最小値・最大値を限界値へ上書きする
     */
    @Inject(
        method = "<init>(Ljava/lang/String;DDD)V", // コンストラクタを指定
        at = @At("TAIL")
    )
    private void sakaplus$overrideMinMaxFields(
            String translationKey, 
            double defaultValue, 
            double min, 
            double max, 
            CallbackInfo ci
    ) {
        // フィールドの値を直接最大・最小に書き換える
        // (これにより clamp() メソッドや、getMin/Max メソッドのすべてが自動的に無制限になります)
        this.minValue = -Double.MAX_VALUE;
        this.maxValue = Double.MAX_VALUE;
    }
}
