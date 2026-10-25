package com.sakalti.sakaplus.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.UUID;

public class MudoStatusEffect extends StatusEffect {
    public static final UUID MUDO_UUID = UUID.fromString("b8db37c0-6722-4f92-98f5-c0a4c6c9ae32");

    public MudoStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x551A8B);
    }
    @Override
    public String getTranslationKey() {
        // 追加: 翻訳キーを明示的に固定して unregistered_sad_surface になるバグを回避
        return "effect.sakaplus.mudo";
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getWorld().isClient()) return;

        EntityAttributeInstance attribute = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (attribute == null) return;

        EntityAttributeModifier existing = attribute.getModifier(MUDO_UUID);
        int desiredReduction = Math.min(amplifier + 1, 18);

        // すでに同じ値なら何もしない
        if (existing != null && existing.getValue() == -desiredReduction) {
            return;
        }

        // 既存のデバフを削除
        if (existing != null) {
            attribute.removeModifier(existing);
        }

        // 新規デバフを追加
        EntityAttributeModifier modifier = new EntityAttributeModifier(
                MUDO_UUID,
                "mudo_hp_penalty",
                -desiredReduction,
                EntityAttributeModifier.Operation.ADDITION
        );
        attribute.addPersistentModifier(modifier);

        // 現在HPが最大値を超えないように調整
        if (entity.getHealth() > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth());
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity.getWorld().isClient()) return;

        EntityAttributeInstance attribute = attributes.getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (attribute == null) return;

        EntityAttributeModifier modifier = attribute.getModifier(MUDO_UUID);
        if (modifier != null) {
            attribute.removeModifier(modifier);
        }
    }
}
