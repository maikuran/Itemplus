package com.sakalti.sakaplus.enchant;

import com.sakalti.sakaplus.item.GreatswordItem;
import com.sakalti.sakaplus.mixin.IModEnchantmentTarget; // Mixinの代わりに中継インターフェースをインポート

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
            // 直接Mixinの変数を叩かず、インターフェース経由で安全に取得
            IModEnchantmentTarget.getGreatbladeTarget(), 
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
        return true; 
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }
}
