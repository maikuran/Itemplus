package com.sakalti.sakaplus.enchant;

import com.sakalti.sakaplus.item.Gun;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem; // 剣の判定用にインポートを追加
import net.minecraft.util.Rarity;

public class PoisonEnchantment extends Enchantment {

    public PoisonEnchantment() {
        super(
                Rarity.RARE,
                // ★BREAKABLE から WEAPON に変更（エンチャントテーブルで剣用の候補に出すため）
                EnchantmentTarget.WEAPON,
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

    // ★銃(Gun)・すべての剣(SwordItem)・本(Items.BOOK)への付与を許可
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof Gun 
            || stack.getItem() instanceof SwordItem 
            || stack.isOf(Items.BOOK);
    }

    public boolean isAvailableForRandomSelection() {
        return true;
    }

    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }
}
