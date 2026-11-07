package com.sakalti.sakaplus.enchant;

import com.sakalti.sakaplus.item.GreatswordItem;
import com.sakalti.sakaplus.mixin.IModEnchantmentTarget;

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
            IModEnchantmentTarget.getGreatblade(), 
            new EquipmentSlot[]{EquipmentSlot.MAINHAND}
        );
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof GreatswordItem;
    }

    @Override
    public float getAttackDamage(int level, EntityGroup group) {
        return level * 1.5F;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return other != Enchantments.SHARPNESS && super.canAccept(other);
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
