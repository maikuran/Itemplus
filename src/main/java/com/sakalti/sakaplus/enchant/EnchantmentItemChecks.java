package com.sakalti.sakaplus.enchant;

import com.sakalti.sakaplus.item.GreatswordItem;
import com.sakalti.sakaplus.item.Gun;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class EnchantmentItemChecks {

    private EnchantmentItemChecks() {}

    public static boolean isMagnumTarget(ItemStack stack) {
        return stack != null
            && !stack.isEmpty()
            && (stack.getItem() instanceof Gun || stack.isOf(Items.BOOK));
    }

    public static boolean isSweepBurstTarget(ItemStack stack) {
        return stack != null
            && !stack.isEmpty()
            && stack.getItem() instanceof GreatswordItem;
    }
}
