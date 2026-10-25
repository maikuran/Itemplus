package com.sakalti.sakaplus.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.LivingEntity;

public class LightBlockEffect extends StatusEffect {
    public LightBlockEffect() {
        super(StatusEffectCategory.HARMFUL, 0xFFFFFF); // 白色
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        // ここでは特別なtick処理は不要
    }
    @Override
    public String getTranslationKey() {
        // 追加: 翻訳キーを明示的に固定して unregistered_sad_surface になるバグを回避
        return "effect.sakaplus.light_block";
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return false;
    }
}
