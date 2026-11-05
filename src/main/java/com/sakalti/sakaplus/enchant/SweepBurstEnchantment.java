package com.sakalti.sakaplus.enchant;

import com.sakalti.sakaplus.enchantment.EnchantmentTargets;
import com.sakalti.sakaplus.item.GreatswordItem;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;

public class SweepBurstEnchantment extends Enchantment {

    public SweepBurstEnchantment() {
        super(
            Rarity.RARE,
            // 💡 ここを WEAPON から、大剣のみを許可するカスタムターゲットに変更します
            CUSTOM_GREATSWORD_TARGET,
            new EquipmentSlot[]{EquipmentSlot.MAINHAND}
        );
    }

    // 💡 大剣と本だけを許可するカスタムEnchantmentTargetの定義
    private static final EnchantmentTarget CUSTOM_GREATSWORD_TARGET = EnchantmentTargets.create("GREATSWORD", item -> item instanceof GreatswordItem);

    @Override
    public int getMaxLevel() {
        return 5;
    }

    // かなどこ等での最終チェック（本への付与もここで明示的に許可）
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof GreatswordItem || stack.isOf(Items.BOOK);
    }

    // レベルごとに攻撃力 +1.5
    @Override
    public float getAttackDamage(int level, EntityGroup group) {
        return level * 1.5F;
    }

    // Sharpnessとの競合
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
