package com.sakalti.sakaplus.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import com.sakalti.sakaplus.item.IceGreatblade;
import com.sakalti.sakaplus.item.LightningSpark;
import com.sakalti.sakaplus.item.BoneScytheItem;

public class SweepBurstEnchantment extends Enchantment {
    public SweepBurstEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    // 3つの大剣・大鎌アイテムにだけ付与できるように制限
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof IceGreatblade 
            || stack.getItem() instanceof LightningSpark 
            || stack.getItem() instanceof BoneScytheItem;
    }
}
