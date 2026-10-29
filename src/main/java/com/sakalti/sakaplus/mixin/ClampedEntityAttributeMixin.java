package com.sakalti.sakaplus.mixin;

import net.minecraft.entity.attribute.ClampedEntityAttribute;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClampedEntityAttribute.class)
public abstract class ClampedEntityAttributeMixin {

    /**
     * @author Sakaplus
     * @reason Remove attribute value clamping.
     */
    @Overwrite
    public double clamp(double value) {
        return value;
    }

    /**
     * @author Sakaplus
     * @reason Remove maximum attribute value restriction.
     */
    @Overwrite
    public double getMaxValue() {
        return Double.MAX_VALUE;
    }

    /**
     * @author Sakaplus
     * @reason Remove minimum attribute value restriction.
     */
    @Overwrite
    public double getMinValue() {
        return -Double.MAX_VALUE;
    }
}
