package com.sakalti.sakaplus.mixin;

import net.minecraft.enchantment.EnchantmentTarget;
import com.sakalti.sakaplus.mixin.EnchantmentTargetMixin;
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
            // 宣言されたprivateメソッドを取得する為に getDeclaredMethod を使用
            Method method = mixinClass.getDeclaredMethod(methodName);
            // private制限を解除してアクセス可能にする
            method.setAccessible(true);
            return (EnchantmentTarget) method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            return EnchantmentTarget.BREAKABLE; // 安全用の予備
        }
    }
}
