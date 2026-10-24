package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.attribute.ModAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class AdaptationMixin {

    /**
     * 適応属性によってダメージ量を軽減する。
     *
     * 元のdamage()を再呼び出しせず、
     * 引数amountそのものを変更する。
     */
    @ModifyVariable(
            method = "damage",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float sakaplus$applyAdaptation(float amount) {
        LivingEntity living = (LivingEntity) (Object) this;

        double adaptation = living.getAttributeValue(
                ModAttributes.ADAPTATION
        );

        float reduced = (float) (amount - adaptation);

        return Math.max(0.0F, reduced);
    }

    /**
     * 適応によってダメージが完全に無効化された場合、
     * 元のダメージ処理を実行しない。
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
        LivingEntity living = (LivingEntity) (Object) this;

        double adaptation = living.getAttributeValue(
                ModAttributes.ADAPTATION
        );

        if (amount - adaptation <= 0.0F) {
            cir.setReturnValue(false);
        }
    }
}
