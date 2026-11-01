package com.sakalti.sakaplus.registry;

import net.minecraft.item.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.sakalti.sakaplus.item.*;

public class ModItems {

    public static final Item HP_PLUS = new HPPlusItem(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).maxCount(6));
    public static final Item LIGHT_BERRY_CHEESE = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.LIGHT_BERRY_CHEESE));
    public static final Item GLOW_BERRY_BREAD = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.GLOW_BERRY_BREAD));
    public static final Item HACHIWARI_MANJU = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.HACHIWARI_MANJU));
    public static final Item LIGHT_BERRY = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.LIGHT_BERRY));
    public static final Item CHOCOLATE_BAR = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.CHOCOLATE_BAR));
    public static final Item SWEET_BERRY_PIE = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.SWEET_BERRY_PIE));
    public static final Item GLOW_BERRY_DONUT = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.GLOW_BERRY_DONUT));
    public static final Item KUR_BRUTE_MEAT = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.KUR_BRUTE_MEAT));
    public static final Item TOUFU_A = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.TOUFU_A));
    public static final Item HACHIWARI_MANJU_CHOCO = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.HACHIWARI_MANJU_CHOCO));
    public static final Item LIGHT_STONE = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item CRYOCRYSTAL = new Item(new Item.Settings().group(ModTabs.CYROVILIM_TAB));

    public static final Item OVERSHOT = new Overshot(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item CRYO_RIFLE = new CryoRifle(new Item.Settings().group(ModTabs.CYROVILIM_TAB));
    public static final Item KRUZI_RIFLE = new KruziRifle(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item LEATHERGRIP = new Leathergrip(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item RAW_PYRONIUM = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item PYRONIUM_INGOT = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item CLOTH = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item PYRONIUM_SWORD = new SwordItem(ModToolMaterials.PYRONIUM, 3, -2.4F, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item PYRONIUM_PICKAXE = new PickaxeItem(ModToolMaterials.PYRONIUM, 1, -2.8F, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item DANDELITE_INGOT = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item DANDELITE_SWORD = new SwordItem(ModToolMaterials.DANDELITE, 0, -2.4F, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item DANDELITE_PICKAXE = new PickaxeItem(ModToolMaterials.DANDELITE, -2, -2.8F, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item ROSITE_INGOT = new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item ROSITE_SWORD = new SwordItem(ModToolMaterials.ROSITE, 0, -2.4F, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item ROSITE_PICKAXE = new PickaxeItem(ModToolMaterials.ROSITE, -2, -2.8F, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item PYRONIUM_HELMET = new ArmorItem(ModArmorMaterials.PYRONIUM, EquipmentSlot.HEAD, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item PYRONIUM_CHESTPLATE = new ArmorItem(ModArmorMaterials.PYRONIUM, EquipmentSlot.CHEST, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item PYRONIUM_LEGGINGS = new ArmorItem(ModArmorMaterials.PYRONIUM, EquipmentSlot.LEGS, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item PYRONIUM_BOOTS = new ArmorItem(ModArmorMaterials.PYRONIUM, EquipmentSlot.FEET, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item AMETHOND_HELMET = new ArmorItem(ModArmorMaterials.AMETHOND, EquipmentSlot.HEAD, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item AMETHOND_CHESTPLATE = new ArmorItem(ModArmorMaterials.AMETHOND, EquipmentSlot.CHEST, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item AMETHOND_LEGGINGS = new ArmorItem(ModArmorMaterials.AMETHOND, EquipmentSlot.LEGS, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item AMETHOND_BOOTS = new ArmorItem(ModArmorMaterials.AMETHOND, EquipmentSlot.FEET, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item CRYSTALLINE_HELMET = new CrystallineArmorItem(ModArmorMaterials.CRYSTALLINE, EquipmentSlot.HEAD, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item CRYSTALLINE_CHESTPLATE = new CrystallineArmorItem(ModArmorMaterials.CRYSTALLINE, EquipmentSlot.CHEST, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item CRYSTALLINE_LEGGINGS = new CrystallineArmorItem(ModArmorMaterials.CRYSTALLINE, EquipmentSlot.LEGS, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item CRYSTALLINE_BOOTS = new CrystallineArmorItem(ModArmorMaterials.CRYSTALLINE, EquipmentSlot.FEET, new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    private static void registerItem(String name, Item item) {
        Registry.register(Registry.ITEM, new Identifier("sakaplus", name), item);
    }

    public static void register() {
        registerItem("light_berry_cheese", LIGHT_BERRY_CHEESE);
        registerItem("hachiwari_manju", HACHIWARI_MANJU);
        registerItem("glow_berry_bread", GLOW_BERRY_BREAD);
        registerItem("light_berry", LIGHT_BERRY);
        registerItem("kur_brute_meat", KUR_BRUTE_MEAT);
        registerItem("chocolate_bar", CHOCOLATE_BAR);
        registerItem("sweet_berry_pie", SWEET_BERRY_PIE);
        registerItem("glow_berry_donut", GLOW_BERRY_DONUT);
        registerItem("hp_plus", HP_PLUS);
        registerItem("light_stone", LIGHT_STONE);
        registerItem("cryocrystal", CRYOCRYSTAL);
        registerItem("toufu_a", TOUFU_A);
        registerItem("hachiwari_manju_choco", HACHIWARI_MANJU_CHOCO);
        registerItem("overshot", OVERSHOT);
        registerItem("leathergrip", LEATHERGRIP);
        registerItem("kruzi_rifle", KRUZI_RIFLE);
        registerItem("cryo_rifle", CRYO_RIFLE);
        registerItem("raw_pyronium", RAW_PYRONIUM);
        registerItem("pyronium_ingot", PYRONIUM_INGOT);
        registerItem("cloth", CLOTH);
        registerItem("pyronium_sword", PYRONIUM_SWORD);
        registerItem("pyronium_pickaxe", PYRONIUM_PICKAXE);
        registerItem("pyronium_helmet", PYRONIUM_HELMET);
        registerItem("pyronium_chestplate", PYRONIUM_CHESTPLATE);
        registerItem("pyronium_leggings", PYRONIUM_LEGGINGS);
        registerItem("pyronium_boots", PYRONIUM_BOOTS);
        registerItem("amethond_helmet", AMETHOND_HELMET);
        registerItem("amethond_chestplate", AMETHOND_CHESTPLATE);
        registerItem("amethond_leggings", AMETHOND_LEGGINGS);
        registerItem("amethond_boots", AMETHOND_BOOTS);
        registerItem("crystalline_helmet", CRYSTALLINE_HELMET);
        registerItem("crystalline_chestplate", CRYSTALLINE_CHESTPLATE);
        registerItem("crystalline_leggings", CRYSTALLINE_LEGGINGS);
        registerItem("crystalline_boots", CRYSTALLINE_BOOTS);
        registerItem("dandelite_ingot", DANDELITE_INGOT);
        registerItem("dandelite_sword", DANDELITE_SWORD);
        registerItem("dandelite_pickaxe", DANDELITE_PICKAXE);
        registerItem("rosite_ingot", ROSITE_INGOT);
        registerItem("rosite_sword", ROSITE_SWORD);
        registerItem("rosite_pickaxe", ROSITE_PICKAXE);
    }
}
