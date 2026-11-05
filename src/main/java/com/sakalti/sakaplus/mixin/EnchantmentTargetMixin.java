package com.sakalti.sakaplus.mixin;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Predicate;

@Mixin(EnchantmentTarget.class)
public interface EnchantmentTargetMixin {
    // 1.19.2のenumコンストラクターは name と ordinal を受け取る
    @Invoker("<init>")
    static EnchantmentTarget sakaplus$create(String name, int ordinal, Predicate<Item> predicate) {
        throw new AssertionError();
    }

    // Enumに新しい要素を動的に追加する
    static EnchantmentTarget create(String name, Predicate<Item> predicate) {
        return sakaplus$create(name, EnchantmentTarget.values().length, predicate);
    }
}
