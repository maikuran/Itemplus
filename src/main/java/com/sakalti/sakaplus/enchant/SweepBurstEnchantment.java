
package com.sakalti.sakaplus.enchant;

import com.sakalti.sakaplus.item.IceGreatblade;
import com.sakalti.sakaplus.item.LightningSpark;
import com.sakalti.sakaplus.item.BoneScytheItem;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.EnchantmentHelper;
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
            EnchantmentTarget.WEAPON,
            new EquipmentSlot[]{EquipmentSlot.MAINHAND}
        );
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof IceGreatblade
            || stack.getItem() instanceof LightningSpark
            || stack.getItem() instanceof BoneScytheItem
            || stack.isOf(Items.BOOK);
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
