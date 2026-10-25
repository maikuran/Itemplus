package com.sakalti.sakaplus.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;

public class InfernalArmorItem extends ArmorItem {

    public InfernalArmorItem(
        ArmorMaterial material,
        EquipmentSlot slot,
        Settings settings
    ) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(
        ItemStack stack,
        net.minecraft.world.World world,
        net.minecraft.entity.Entity entity,
        int slot,
        boolean selected
    ) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (world.isClient || !(entity instanceof LivingEntity)) {
            return;
        }

        LivingEntity living = (LivingEntity) entity;

        boolean wearingInfernal =
            isInfernal(living.getEquippedStack(EquipmentSlot.HEAD)) ||
            isInfernal(living.getEquippedStack(EquipmentSlot.CHEST)) ||
            isInfernal(living.getEquippedStack(EquipmentSlot.LEGS)) ||
            isInfernal(living.getEquippedStack(EquipmentSlot.FEET));

        if (wearingInfernal) {
            living.addStatusEffect(
                new StatusEffectInstance(
                    StatusEffects.FIRE_RESISTANCE,
                    40,
                    0,
                    true,
                    false,
                    true
                )
            );
        }
    }

    private boolean isInfernal(ItemStack stack) {
        return stack.getItem() instanceof InfernalArmorItem;
    }
}
