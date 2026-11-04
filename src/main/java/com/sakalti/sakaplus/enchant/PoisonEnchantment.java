package com.sakalti.sakaplus.enchant; // フォルダ構造に合わせて適宜調整してください

import com.sakalti.sakaplus.item.Gun;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;

public class PoisonEnchantment extends Enchantment {

    public PoisonEnchantment() {
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
        return 2;
    }

    public int getMinPower(int level) {
        return 5 + (level - 1) * 15;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 30;
    }

    // ★引数の型を ItemStack に修正し、本(Items.BOOK)への付与も許可
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
