package com.sakalti.sakaplus.mixin;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Predicate;

@Mixin(EnchantmentTarget.class)
public interface EnchantmentTargetMixin {
    // Enumに新しい要素を動的に追加するインボーカー
    @Invoker("<init>")
    static EnchantmentTarget create(String name, Predicate<Item> predicate) {
        throw new AssertionError();
    }
}
