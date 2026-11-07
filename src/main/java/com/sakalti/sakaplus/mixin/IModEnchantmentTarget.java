package com.sakalti.sakaplus.mixin;

import net.minecraft.enchantment.EnchantmentTarget;
import com.sakalti.sakaplus.EnchantmentTargetMixin;

public interface IModEnchantmentTarget {
    // 外部の通常のクラスからは、ここを経由して安全にターゲットを取得します
    static EnchantmentTarget getGreatblade() {
        return EnchantmentTargetMixin.SAKAPLUS$GREATBLADE;
    }

    static EnchantmentTarget getGun() {
        return EnchantmentTargetMixin.SAKAPLUS$GUN;
    }
}
