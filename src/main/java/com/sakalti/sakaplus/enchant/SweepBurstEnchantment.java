package com.sakalti.sakaplus.enchant;

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
            // isAcceptableItem below narrows this vanilla category to greatswords (and books).
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
        return stack.getItem() instanceof GreatswordItem || stack.isOf(Items.BOOK);
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
