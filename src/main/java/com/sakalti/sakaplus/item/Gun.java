
package com.sakalti.sakaplus.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public abstract class Gun extends Item {

    public Gun(Settings settings, int durability) {
        super(settings.maxDamage(durability));
    }

    /**
     * 銃の修理素材。
     * デフォルトでは鉄インゴットで修理できます。
     */
    public boolean isRepairable(ItemStack stack) {
        return true;
    }

    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.IRON_INGOT);
    }
}
