
package com.sakalti.sakaplus.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;

public class SulfurArmorItem extends ArmorItem {

    private static final float PROJECTILE_REDUCTION_PER_PIECE = 0.075F;

    public SulfurArmorItem(
        ArmorMaterial material,
        EquipmentSlot slot,
        Settings settings
    ) {
        super(material, slot, settings);
    }

    /**
     * 装備中のSulfur Armorの部位数を取得
     */
    public static int getSulfurArmorPieces(LivingEntity entity) {
        int pieces = 0;

        EquipmentSlot[] slots = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
        };

        for (EquipmentSlot slot : slots) {
            ItemStack stack = entity.getEquippedStack(slot);

            if (stack.getItem() instanceof SulfurArmorItem) {
                pieces++;
            }
        }

        return pieces;
    }

    /**
     * Projectile Damageの軽減後ダメージを計算
     */
    public static float reduceProjectileDamage(
        LivingEntity entity,
        DamageSource source,
        float damage
    ) {
        if (entity.getWorld().isClient || !source.isProjectile()) {
            return damage;
        }

        int pieces = getSulfurArmorPieces(entity);

        if (pieces <= 0) {
            return damage;
        }

        float reduction = PROJECTILE_REDUCTION_PER_PIECE * pieces;

        return damage * (1.0F - reduction);
    }
}
