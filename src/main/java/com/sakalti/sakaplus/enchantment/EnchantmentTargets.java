package com.sakalti.sakaplus.enchantment;

import com.sakalti.sakaplus.mixin.EnchantmentTargetMixin;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;

import java.util.function.Predicate;

/** Factory for the custom enchantment targets added through the enum invoker. */
public final class EnchantmentTargets {
    private EnchantmentTargets() {
    }

    public static EnchantmentTarget create(String name, Predicate<Item> predicate) {
        return EnchantmentTargetMixin.sakaplus$create(name, EnchantmentTarget.values().length, predicate);
    }
}
