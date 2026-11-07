package com.sakalti.sakaplus.enchant;

import net.minecraft.enchantment.EnchantmentTarget;
import java.lang.reflect.Method;

public interface IModEnchantmentTarget {

    static EnchantmentTarget getGreatblade() {
        return fetchTarget("sakaplus$getGreatswordTarget");
    }

    static EnchantmentTarget getGun() {
        return fetchTarget("sakaplus$getGunTarget");
    }

    private static EnchantmentTarget fetchTarget(String methodName) {
        try {
            // 文字列参照にすることでコンパイル時・ロード時のクロス参照ロックを完全に回避
            Class<?> mixinClass = Class.forName("com.sakalti.sakaplus.mixin.EnchantmentTargetMixin");
            Method method = mixinClass.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return (EnchantmentTarget) method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            return EnchantmentTarget.BREAKABLE; // クラッシュ防止用予備フォールバック
        }
    }
}
