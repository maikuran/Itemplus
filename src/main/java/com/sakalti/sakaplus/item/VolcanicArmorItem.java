package com.sakalti.sakaplus.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class VolcanicArmorItem extends ArmorItem {

    public VolcanicArmorItem(
        ArmorMaterial material,
        EquipmentSlot slot,
        Settings settings
    ) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(
        ItemStack stack,
        World world,
        Entity entity,
        int slot,
        boolean selected
    ) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (world.isClient || !(entity instanceof LivingEntity)) {
            return;
        }

        LivingEntity living = (LivingEntity) entity;

        boolean wearingVolcanic =
            isVolcanic(living.getEquippedStack(EquipmentSlot.HEAD)) ||
            isVolcanic(living.getEquippedStack(EquipmentSlot.CHEST)) ||
            isVolcanic(living.getEquippedStack(EquipmentSlot.LEGS)) ||
            isVolcanic(living.getEquippedStack(EquipmentSlot.FEET));

        if (wearingVolcanic) {
            living.addStatusEffect(
                new StatusEffectInstance(
                    StatusEffects.STRENGTH,
                    40,
                    0,
                    true,
                    false,
                    true
                )
            );
        }
    }

    private boolean isVolcanic(ItemStack stack) {
        return stack.getItem() instanceof VolcanicArmorItem;
    }
}
