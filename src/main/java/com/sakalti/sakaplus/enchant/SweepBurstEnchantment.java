package com.sakalti.sakaplus.enchant;

import com.sakalti.sakaplus.item.GreatswordItem;
import com.sakalti.sakaplus.mixin.EnchantmentTargetMixin;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

public class SweepBurstEnchantment extends Enchantment {

    public SweepBurstEnchantment() {
        super(
            Rarity.RARE,
            // WEAPONだと剣が含まれるため、基本全般アイテム向けの BREAKABLE 等にするか
            // もしくは独自のカスタムTarget（Mixin拡張）を指定します。
            // ここではアイテム側の matches(ItemStack) 判定を主軸にするため BREAKABLE に変更
            EnchantmentTargetMixin.SAKAPLUS$GREATSWORD, 
            new EquipmentSlot[]{EquipmentSlot.MAINHAND}
        );
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        // ここで大剣インスタンスのみを厳密に指定
        return stack.getItem() instanceof GreatswordItem;
    }

    @Override
    public float getAttackDamage(int level, EntityGroup group) {
        return level * 1.5F;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        if (other == Enchantments.SHARPNESS) {
            return false;
        }
        return super.canAccept(other);
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        // BREAKABLEにしたことでエンチャントテーブルに出現しなくなるのを防ぐため、
        // テーブル出現率を維持したい場合は true のままでOKですが、大剣のみに適用させるため
        // 基本はエンチャント本経由（BookOffer）か、カスタムテーブル処理を推奨します。
        return true; 
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }
}
