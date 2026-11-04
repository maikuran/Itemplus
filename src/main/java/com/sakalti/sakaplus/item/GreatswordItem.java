package com.sakalti.sakaplus.item;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;

public class GreatswordItem extends SwordItem {

    public GreatswordItem(
            ToolMaterial material,
            int attackDamage,
            float attackSpeed,
            Item.Settings settings
    ) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(
            ItemStack stack,
            LivingEntity target,
            LivingEntity attacker
    ) {
        stack.damage(1, attacker,
                entity -> entity.sendToolBreakStatus(attacker.getActiveHand()));

        return true;
    }
}
