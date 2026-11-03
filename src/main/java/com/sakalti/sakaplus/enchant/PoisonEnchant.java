package com.sakalti.sakaplus.enchant;

import com.sakalti.sakaplus.item.Gun;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;

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

    // 銃以外には付与できない
    
    public boolean isAcceptableItem(Item item) {
        return item instanceof Gun;
    }
}
