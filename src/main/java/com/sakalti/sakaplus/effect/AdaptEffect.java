package com.sakalti.sakaplus.effect;

import net.minecraft.entity.LivingEntity;
import com.sakalti.sakaplus.attribute.ModAttributes;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.UUID;

public class AdaptEffect extends StatusEffect {
    public static final UUID MUDO_UUID = UUID.fromString("b8db37c0-6722-4f92-98f5-c0a434c9ae32");

    public AdaptEffect() {
        super(StatusEffectCategory.HARMFUL, 0xaa0000);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getWorld().isClient()) return;

        EntityAttributeInstance attribute = entity.getAttributeInstance(ModAttributes.ADAPTATION);
        if (attribute == null) return;

        EntityAttributeModifier existing = attribute.getModifier(MUDO_UUID);
        int desiredReduction = amplifier + 1;

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
                "adaptim",
                -desiredReduction,
                EntityAttributeModifier.Operation.ADDITION
        );
        attribute.addPersistentModifier(modifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity.getWorld().isClient()) return;

        // 修正: 属性の取得先を MAX_HEALTH から正しく ModAttributes.ADAPTATION に変更
        EntityAttributeInstance attribute = attributes.getCustomInstance(ModAttributes.ADAPTATION);
        if (attribute == null) return;

        EntityAttributeModifier modifier = attribute.getModifier(MUDO_UUID);
        if (modifier != null) {
            attribute.removeModifier(modifier);
        }
    }

    @Override
    public String getTranslationKey() {
        // 追加: 翻訳キーを明示的に固定して unregistered_sad_surface になるバグを回避
        return "effect.sakaplus.adapt";
    }
}
