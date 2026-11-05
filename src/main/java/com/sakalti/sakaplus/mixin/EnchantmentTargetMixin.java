package com.sakalti.sakaplus.mixin;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Predicate;

@Mixin(EnchantmentTarget.class)
public abstract class EnchantmentTargetMixin {
    // 1.19.2のenumコンストラクターは name と ordinal を受け取る
    @Invoker("<init>")
    public static EnchantmentTarget sakaplus$create(String name, int ordinal, Predicate<Item> predicate) {
        throw new AssertionError();
    }
}
