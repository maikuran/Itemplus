package com.sakalti.sakaplus.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;

public class ParalyzeEffect extends StatusEffect {
    public ParalyzeEffect() {
        super(StatusEffectCategory.HARMFUL, 0xAAAAFF); // 色は適宜
    }
    @Override
    public String getTranslationKey() {
        // 追加: 翻訳キーを明示的に固定して unregistered_sad_surface になるバグを回避
        return "effect.sakaplus.paralyze";
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.getWorld().isClient) {
            entity.setVelocity(0, entity.getVelocity().y, 0);
            entity.setJumping(false);
            entity.setMovementSpeed(0.0F); // 無理やり止める場合
            entity.velocityDirty = true;
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
