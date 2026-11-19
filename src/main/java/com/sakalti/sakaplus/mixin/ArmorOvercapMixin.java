package com.sakalti.sakaplus.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ArmorOvercapMixin {

    @Shadow
    public abstract int getArmor();

    @Inject(
            method = "applyArmorToDamage",
            at = @At("RETURN"),
            cancellable = true
    )
    private void sakaplus$applyOvercapArmorResistance(
            DamageSource source,
            float amount,
            CallbackInfoReturnable<Float> cir
    ) {
        if (source.bypassesArmor()) {
            return;
        }

        int armor = this.getArmor();
        if (armor > 20) {
            LivingEntity living = (LivingEntity) (Object) this;
            float toughness = (float) living.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
            float originalDamage = cir.getReturnValue();

            int overcap = armor - 20;

            // バニラと同じ計算式で「実効超過防御値」を算出（大ダメージほど減衰する）
            // 最低でも本来の20%（7%の1/5 = 1.4%）の効果は保証する
            float effectiveOvercap = Math.max(
                    overcap * 0.2F,
                    overcap - amount / (2.0F + toughness / 4.0F)
            );

            float reduction = Math.min(1.0F, effectiveOvercap * 0.07F);
            cir.setReturnValue(originalDamage * (1.0F - reduction));
        }
    }
}
