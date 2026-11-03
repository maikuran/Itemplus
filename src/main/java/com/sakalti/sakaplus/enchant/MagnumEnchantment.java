package com.sakalti.sakaplus.enchant;

import com.sakalti.sakaplus.item.Gun;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;

public class MagnumEnchantment extends Enchantment {

    public MagnumEnchantment() {
        super(
                Rarity.RARE,
                EnchantmentTarget.BREAKABLE,
                new EquipmentSlot[] {
                        EquipmentSlot.MAINHAND,
                        EquipmentSlot.OFFHAND
                }
        );
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinPower(int level) {
        return 1 + (level - 1) * 10;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 50;
    }

    // 銃以外には付与できない
    @Override
    public boolean isAcceptableItem(Item item) {
        return item instanceof Gun;
    }
}
