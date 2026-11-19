package com.sakalti.sakaplus.mixin;

import net.minecraft.entity.LivingEntity;
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

    /**
     * 防御力が20を超えている場合、超過分1につき7%の追加ダメージ軽減を適用する。
     * 例: 防御25の場合 -> (25 - 20) * 7% = 35%の追加軽減
     */
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
        // 防御無視ダメージ（落下、魔法、奈落など）には適用しない
        if (source.bypassesArmor()) {
            return;
        }

        int armor = this.getArmor();
        if (armor > 20) {
            int overcap = armor - 20;
            // 1あたり7% (0.07f) 軽減。100% (1.0f) を超えないように制限
            float reduction = Math.min(1.0F, overcap * 0.07F);
            float originalDamage = cir.getReturnValue();

            cir.setReturnValue(originalDamage * (1.0F - reduction));
        }
    }
}
