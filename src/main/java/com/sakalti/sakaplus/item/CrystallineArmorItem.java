package com.sakalti.sakaplus.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;

public class CrystallineArmorItem extends ArmorItem {

    public CrystallineArmorItem(
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

        boolean wearingCrystalline =
            isCrystalline(living.getEquippedStack(EquipmentSlot.HEAD)) ||
            isCrystalline(living.getEquippedStack(EquipmentSlot.CHEST)) ||
            isCrystalline(living.getEquippedStack(EquipmentSlot.LEGS)) ||
            isCrystalline(living.getEquippedStack(EquipmentSlot.FEET));

        if (wearingCrystalline) {
            living.addStatusEffect(
                new StatusEffectInstance(
                    StatusEffects.RESISTANCE,
                    40,
                    0,
                    true,
                    false,
                    true
                )
            );
        }
    }

    private boolean isCrystalline(ItemStack stack) {
        return stack.getItem() instanceof CrystallineArmorItem;
    }
}
