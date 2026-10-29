
package com.sakalti.sakaplus.mixin;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
     * 基本値を直接保存し、バニラのクランプ処理を回避する。
     */
    @Inject(
        method = "setBaseValue(D)V",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void sakaplus$unlimitedBaseValue(
            double newValue,
            CallbackInfo ci
    ) {
        if (this.baseValue != newValue) {
            this.baseValue = newValue;
            this.onUpdate();
        }

        ci.cancel();
    }

    /**
     * 基本値を直接返す。
     */
    @Inject(
        method = "getBaseValue()D",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void sakaplus$getUnlimitedBaseValue(
            CallbackInfoReturnable<Double> cir
    ) {
        cir.setReturnValue(this.baseValue);
    }

    /**
     * 属性値を再計算し、クランプを通さず返す。
     */
    @Inject(
        method = "getValue()D",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void sakaplus$getUnlimitedValue(
            CallbackInfoReturnable<Double> cir
    ) {
        double base = this.baseValue;

        // ADDITION
        for (EntityAttributeModifier modifier :
                this.getModifiers(EntityAttributeModifier.Operation.ADDITION)) {
            base += modifier.getValue();
        }

        double result = base;

        // MULTIPLY_BASE
        for (EntityAttributeModifier modifier :
                this.getModifiers(EntityAttributeModifier.Operation.MULTIPLY_BASE)) {
            result += this.baseValue * modifier.getValue();
        }

        // MULTIPLY_TOTAL
        for (EntityAttributeModifier modifier :
                this.getModifiers(EntityAttributeModifier.Operation.MULTIPLY_TOTAL)) {
            result *= 1.0D + modifier.getValue();
        }

        cir.setReturnValue(result);
    }
}
