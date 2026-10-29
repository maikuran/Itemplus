
package com.sakalti.sakaplus.mixin;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityAttributeInstance.class)
public abstract class EntityAttributeInstanceMixin {

    /**
     * 基本属性値のクランプを無効化する。
     */
    @Redirect(
        method = "setBaseValue(D)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/attribute/EntityAttribute;clamp(D)D"
        )
    )
    private double sakaplus$removeBaseValueClamp(
            EntityAttribute attribute,
            double value
    ) {
        return value;
    }

    /**
     * モディファイア適用後のクランプを無効化する。
     */
    @Redirect(
        method = "computeValue()D",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/attribute/EntityAttribute;clamp(D)D"
        )
    )
    private double sakaplus$removeComputedValueClamp(
            EntityAttribute attribute,
            double value
    ) {
        return value;
    }
}
