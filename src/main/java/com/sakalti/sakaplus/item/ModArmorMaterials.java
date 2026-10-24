package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.registry.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public enum ModArmorMaterials implements ArmorMaterial {
    PYRONIUM("sakaplus:pyronium", 37, new int[]{4, 6, 8, 3}, 15, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 4.5F, 1.1F, () -> Ingredient.ofItems(ModItems.PYRONIUM_INGOT)),
    AMETHOND("sakaplus:amethond", 28, new int[]{2, 8, 7, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.5F, 0.1F, () -> Ingredient.ofItems(Items.AMETHYST_SHARD));

    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Lazy<Ingredient> repairIngredient;

    ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new Lazy<>(repairIngredient);
    }

    @Override
    public int getDurability(EquipmentSlot slot) { 
        return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier; 
    }
    
    @Override
    public int getProtectionAmount(EquipmentSlot slot) { 
        return this.protectionAmounts[slot.getEntitySlotId()]; 
    }
    
    @Override
    public int getEnchantability() { return this.enchantability; }
    @Override
    public SoundEvent getEquipSound() { return this.equipSound; }
    @Override
    public Ingredient getRepairIngredient() { return this.repairIngredient.get(); }
    @Override
    public String getName() { return this.name; }
    @Override
    public float getToughness() { return this.toughness; }
    @Override
    public float getKnockbackResistance() { return this.knockbackResistance; }
}
