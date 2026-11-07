package com.sakalti.sakaplus.enchant;

import com.sakalti.sakaplus.item.Gun;
import com.sakalti.sakaplus.mixin.IModEnchantmentTarget; // Mixinの代わりにインターフェースをインポート

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;

public class MagnumEnchantment extends Enchantment {

    public MagnumEnchantment() {
        super(
                Rarity.RARE,
                // 直接Mixinの変数を叩かず、インターフェース経由で安全に取得
                IModEnchantmentTarget.getGunTarget(),
                new EquipmentSlot[] {
                        EquipmentSlot.MAINHAND,
                        EquipmentSlot.OFFHAND
                }
        );
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinPower(int level) {
        return 1 + (level - 1) * 10;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 50;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof Gun || stack.isOf(Items.BOOK);
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return true;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }
}
