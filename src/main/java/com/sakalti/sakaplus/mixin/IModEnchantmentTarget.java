package com.sakalti.sakaplus.mixin;

import net.minecraft.enchantment.EnchantmentTarget;
import com.sakalti.sakaplus.mixin.EnchantmentTargetMixin;

public interface IModEnchantmentTarget {
    // 外部の通常のクラスからは、ここを経由して安全にターゲットを取得します
    static EnchantmentTarget getGreatsword() {
        return EnchantmentTargetMixin.SAKAPLUS$GREATSWORD;
    }

    static EnchantmentTarget getGun() {
        return EnchantmentTargetMixin.SAKAPLUS$GUN;
    }
}
