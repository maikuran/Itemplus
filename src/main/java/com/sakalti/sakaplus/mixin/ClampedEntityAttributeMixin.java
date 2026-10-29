package com.sakalti.sakaplus.mixin;

import net.minecraft.entity.attribute.ClampedEntityAttribute;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClampedEntityAttribute.class)
public abstract class ClampedEntityAttributeMixin {

    /**
     * 属性値の制限を完全解除
     */
    @Overwrite
    public double clamp(double value) {
        return value;
    }

    /**
     * 最大値制限解除
     */
    @Overwrite
    public double getMaxValue() {
        return Double.MAX_VALUE;
    }

    /**
     * 最小値制限解除
     */
    @Overwrite
    public double getMinValue() {
        return -Double.MAX_VALUE;
    }
}
