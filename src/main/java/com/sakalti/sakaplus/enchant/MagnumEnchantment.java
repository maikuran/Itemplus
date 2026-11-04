package com.sakalti.sakaplus.enchantment;

import com.sakalti.sakaplus.item.Gun;
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
                EnchantmentTarget.BREAKABLE,
                new EquipmentSlot[] {
                        EquipmentSlot.MAINHAND,
                        EquipmentSlot.OFFHAND
                }
        );
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getMinPower(int level) {
        return 1 + (level - 1) * 10;
    }

    public int getMaxPower(int level) {
        return getMinPower(level) + 50;
    }

    // ★型を ItemStack に修正し、本(Items.BOOK)も受け付けるように拡張
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof Gun || stack.isOf(Items.BOOK);
    }

    // ★エンチャントテーブルのランダム抽選プールに出現させるための設定
    public boolean isAvailableForRandomSelection() {
        return true;
    }

    // ★村人の交易やチェストから「エンチャント本」として出現させるための設定
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }
}
