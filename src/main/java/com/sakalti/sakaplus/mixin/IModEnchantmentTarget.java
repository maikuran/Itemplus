package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.mixin.EnchantmentTargetMixin;
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
            Class<?> mixinClass = Class.forName("com.sakalti.sakaplus.mixin.EnchantmentTargetMixin");
            Method method = mixinClass.getMethod(methodName);
            return (EnchantmentTarget) method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            return EnchantmentTarget.BREAKABLE; // 失敗時の安全フォールバック
        }
    }
}
