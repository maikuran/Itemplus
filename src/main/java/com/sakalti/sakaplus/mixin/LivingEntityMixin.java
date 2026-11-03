package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.enchant.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "damage", at = @At("TAIL"))
    private void applyPoisonEnchantmentEffect(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // ダメージが正常に通らなかった（無効だった）場合は何もしない
        if (!cir.getReturnValue()) return;

        // 攻撃を仕掛けた本人がLivingEntity（プレイヤーやMob）であるかチェック
        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity livingAttacker) {
            
            // 攻撃側のメインハンドの持ち物に付与されている「毒エンチャント」のレベルを取得
            int level = EnchantmentHelper.getLevel(ModEnchantments.POISON, livingAttacker.getMainHandStack());
            
            if (level > 0) {
                LivingEntity target = (LivingEntity) (Object) this;
                
                // レベルに応じて効果時間(秒)と強さを計算
                int duration = 40 + (level * 40); // Lv1=4秒(80ticks), Lv2=6秒(120ticks)
                int amplifier = level - 1;       // Lv1=毒I(0), Lv2=毒II(1)
                
                // ターゲットに毒（POISON）エフェクトを付与
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, duration, amplifier));
            }
        }
    }
}
