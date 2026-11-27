package com.sakalti.thesolar;

import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public final class SolarMaterials {
    private SolarMaterials() {}
    private record Spec(String id, int oreHardness, int oreResistance, int toolDurability, float speed, float damage,
                        int enchantability, int armorDurability, int helmet, int chest, int legs,
                        int boots, float toughness, float knockback, int blockHardness, int blockResistance) {}
    private static final Spec[] ALL = {
        new Spec("cobalt",3,8,430,7.0F,2.0F,16,15,2,6,5,2,0.0F,0.0F,5,15),
        new Spec("limonite",4,10,560,7.5F,2.5F,18,17,2,6,5,2,0.0F,0.0F,6,18),
        new Spec("jade",5,13,680,8.0F,3.0F,20,19,2,7,5,2,0.5F,0.0F,7,25),
        new Spec("ruby",7,18,850,8.5F,3.5F,22,21,3,8,6,3,1.0F,0.0F,8,35),
        new Spec("krillul",9,30,1100,9.5F,4.0F,25,23,3,8,6,3,1.5F,0.05F,10,45),
        new Spec("sitherian",13,65,1350,10.0F,4.5F,26,25,3,9,7,3,2.0F,0.05F,13,65),
        new Spec("hellphyros_remnantius_forgotina",16,80,1900,11.5F,5.5F,30,29,4,10,8,4,3.0F,0.10F,16,80),
        new Spec("selenite",4,10,720,8.2F,3.0F,24,24,3,7,6,3,1.5F,0.10F,0,0),
        new Spec("lunarium",5,13,1450,10.8F,4.8F,30,32,4,9,7,4,3.5F,0.25F,0,0)
    };
    public static void registerAll() { for (Spec s : ALL) register(s); }
    public static void addIngredients(net.minecraft.world.item.CreativeModeTab.Output out) {
        for (Spec s : ALL) { out.accept(DimensionMod.itemById(s.id()+"_raw")); out.accept(DimensionMod.itemById(s.id()+"_ingot")); }
    }
    public static void addCombat(net.minecraft.world.item.CreativeModeTab.Output out) {
        for (Spec s : ALL) for (String part : new String[]{"_sword","_helmet","_chestplate","_leggings","_boots"}) out.accept(DimensionMod.itemById(s.id()+part));
    }
    public static void addTools(net.minecraft.world.item.CreativeModeTab.Output out) { for (Spec s : ALL) for (String part : new String[]{"_pickaxe","_axe","_shovel","_hoe"}) out.accept(DimensionMod.itemById(s.id()+part)); }
    public static void addOres(net.minecraft.world.item.CreativeModeTab.Output out) {
        for (Spec s : ALL) { out.accept(DimensionMod.itemById(s.id()+"_ore")); if (!s.id().equals("selenite") && !s.id().equals("lunarium")) out.accept(DimensionMod.itemById(s.id()+"_block")); }
    }
    private static void register(Spec s) {
        String n=s.id();
        if (!n.equals("selenite") && !n.equals("lunarium")) {
            DimensionMod.registerOreBlock(n+"_ore",s.oreHardness(),s.oreResistance(),
                n.equals("sitherian") || n.equals("hellphyros_remnantius_forgotina") ? SoundType.NETHER_ORE : SoundType.STONE);
            DimensionMod.registerBlock(n+"_block",s.blockHardness(),s.blockResistance(),SoundType.METAL);
            DimensionMod.registerItem(n+"_raw");
            DimensionMod.registerItem(n+"_ingot");
        } else {
            DimensionMod.registerItem(n+"_raw");
            if (n.equals("selenite")) DimensionMod.registerItem("selenite_ingot");
        }
        TagKey<Block> incorrect=TagKey.create(BuiltInRegistries.BLOCK.key(),DimensionMod.id("incorrect_for_"+n+"_tool"));
        TagKey<Item> repair=TagKey.create(BuiltInRegistries.ITEM.key(),DimensionMod.id("repairs_"+n+"_equipment"));
        ToolMaterial tool=new ToolMaterial(incorrect,s.toolDurability(),s.speed(),s.damage(),s.enchantability(),repair);
        DimensionMod.registerItem(n+"_sword",new Item.Properties().sword(tool,3.0F,-2.4F));
        DimensionMod.registerItem(n+"_pickaxe",new Item.Properties().pickaxe(tool,1.0F,-2.8F));
        DimensionMod.registerItem(n+"_axe",new Item.Properties().axe(tool,5.0F,-3.0F));
        DimensionMod.registerItem(n+"_shovel",new Item.Properties().shovel(tool,1.5F,-3.0F));
        DimensionMod.registerItem(n+"_hoe",new Item.Properties().hoe(tool,0.0F,-3.0F));
        ResourceKey<EquipmentAsset> asset=ResourceKey.create(EquipmentAssets.ROOT_ID,DimensionMod.id(n));
        ArmorMaterial armor=new ArmorMaterial(s.armorDurability(),Map.of(
            ArmorType.HELMET,s.helmet(),ArmorType.CHESTPLATE,s.chest(),ArmorType.LEGGINGS,s.legs(),ArmorType.BOOTS,s.boots()),
            s.enchantability(),SoundEvents.ARMOR_EQUIP_IRON,s.toughness(),s.knockback(),repair,asset);
        DimensionMod.registerItem(n+"_helmet",new Item.Properties().humanoidArmor(armor,ArmorType.HELMET).durability(ArmorType.HELMET.getDurability(s.armorDurability())));
        DimensionMod.registerItem(n+"_chestplate",new Item.Properties().humanoidArmor(armor,ArmorType.CHESTPLATE).durability(ArmorType.CHESTPLATE.getDurability(s.armorDurability())));
        DimensionMod.registerItem(n+"_leggings",new Item.Properties().humanoidArmor(armor,ArmorType.LEGGINGS).durability(ArmorType.LEGGINGS.getDurability(s.armorDurability())));
        DimensionMod.registerItem(n+"_boots",new Item.Properties().humanoidArmor(armor,ArmorType.BOOTS).durability(ArmorType.BOOTS.getDurability(s.armorDurability())));
    }
}
