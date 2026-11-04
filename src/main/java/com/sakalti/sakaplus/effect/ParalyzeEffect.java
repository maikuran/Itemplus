package com.sakalti.sakaplus.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.damage.DamageSource;

public class ParalyzeEffect extends StatusEffect {

    public ParalyzeEffect() {
        super(StatusEffectCategory.HARMFUL, 0xAAAAFF);
    }

    @Override
    public String getTranslationKey() {
        return "effect.sakaplus.paralyze";
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        if (!entity.getWorld().isClient) {

            // 移動を停止
            entity.setVelocity(0, entity.getVelocity().y, 0);
            entity.setJumping(false);
            entity.velocityDirty = true;

            // 20tickごとに1ダメージ
            if (entity.age % 20 == 0) {
                entity.damage(
                    DamageSource.MAGIC,
                    1.0F
                );
            }
        }

        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
