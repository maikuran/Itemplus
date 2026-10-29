package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.attribute.ModAttributes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class AdaptationMixin {

    /**
     * 適応属性の値を取得する。
     * 属性が未初期化、またはエンティティに登録されていない場合は0。
     */
    private double sakaplus$getAdaptation() {
        LivingEntity living = (LivingEntity) (Object) this;

        if (ModAttributes.ADAPTATION == null) {
            return 0.0D;
        }

        EntityAttributeInstance instance =
                living.getAttributeInstance(ModAttributes.ADAPTATION);

        if (instance == null) {
            return 0.0D;
        }

        return instance.getValue();
    }

    /**
     * 適応属性によってダメージを軽減する。
     */
    @ModifyVariable(
            method = "damage",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float sakaplus$applyAdaptation(float amount) {
        double adaptation = sakaplus$getAdaptation();

        return Math.max(0.0F, amount - (float) adaptation);
    }

    /**
     * ダメージが完全に無効化された場合は処理を中止する。
     */
    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sakaplus$cancelFullyAdaptedDamage(
            DamageSource source,
            float amount,
            CallbackInfoReturnable<Boolean> cir
    ) {
        double adaptation = sakaplus$getAdaptation();

        if (amount > 0.0F && amount - adaptation <= 0.0D) {
            cir.setReturnValue(false);
        }
    }
}
