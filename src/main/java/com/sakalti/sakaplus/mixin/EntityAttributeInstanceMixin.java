
package com.sakalti.sakaplus.mixin;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(EntityAttributeInstance.class)
public abstract class EntityAttributeInstanceMixin {

    @Shadow
    private double baseValue;

    @Shadow
    protected abstract void onUpdate();

    @Shadow
    public abstract Set<EntityAttributeModifier> getModifiers(
        EntityAttributeModifier.Operation operation
    );

    /**
     * @author Sakaplus
     * @reason Remove the base attribute value clamp.
     */
    @Overwrite
    public void setBaseValue(double baseValue) {
        if (this.baseValue != baseValue) {
            this.baseValue = baseValue;
            this.onUpdate();
        }
    }

    /**
     * @author Sakaplus
     * @reason Remove the computed attribute value clamp.
     */
    @Overwrite
    private double computeValue() {

        // 1. ADDITION
        double value = this.baseValue;

        for (EntityAttributeModifier modifier :
                this.getModifiers(EntityAttributeModifier.Operation.ADDITION)) {
            value += modifier.getValue();
        }

        // 2. MULTIPLY_BASE
        double result = value;

        for (EntityAttributeModifier modifier :
                this.getModifiers(EntityAttributeModifier.Operation.MULTIPLY_BASE)) {
            result += this.baseValue * modifier.getValue();
        }

        // 3. MULTIPLY_TOTAL
        for (EntityAttributeModifier modifier :
                this.getModifiers(EntityAttributeModifier.Operation.MULTIPLY_TOTAL)) {
            result *= 1.0D + modifier.getValue();
        }

        // 属性のclamp()を一切呼び出さない
        return result;
    }
}
