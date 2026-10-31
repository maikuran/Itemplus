
package com.sakalti.sakaplus.registry;

import net.minecraft.item.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.sakalti.sakaplus.block.ModBlocks2;
import com.sakalti.sakaplus.item.*;

public class ModItems {

    private static final Item.Settings STARTER = new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB);
    private static final Item.Settings CYRO = new Item.Settings().group(ModTabs.CYROVILIM_TAB);

    // 食料・通常アイテム
    public static final Item HP_PLUS = new HPPlusItem(STARTER.maxCount(6));
    public static final Item LIGHT_BERRY_CHEESE = new Item(STARTER.food(ModFoods.LIGHT_BERRY_CHEESE));
    public static final Item GLOW_BERRY_BREAD = new Item(STARTER.food(ModFoods.GLOW_BERRY_BREAD));
    public static final Item HACHIWARI_MANJU = new Item(STARTER.food(ModFoods.HACHIWARI_MANJU));
    public static final Item LIGHT_BERRY = new Item(STARTER.food(ModFoods.LIGHT_BERRY));
    public static final Item CHOCOLATE_BAR = new Item(STARTER.food(ModFoods.CHOCOLATE_BAR));
    public static final Item SWEET_BERRY_PIE = new Item(STARTER.food(ModFoods.SWEET_BERRY_PIE));
    public static final Item GLOW_BERRY_DONUT = new Item(STARTER.food(ModFoods.GLOW_BERRY_DONUT));
    public static final Item KUR_BRUTE_MEAT = new Item(STARTER.food(ModFoods.KUR_BRUTE_MEAT));
    public static final Item TOUFU_A = new Item(STARTER.food(ModFoods.TOUFU_A));
    public static final Item HACHIWARI_MANJU_CHOCO = new Item(STARTER.food(ModFoods.HACHIWARI_MANJU_CHOCO));
    public static final Item LIGHT_STONE = new Item(STARTER);
    public static final Item CRYOCRYSTAL = new Item(CYRO);

    // 武器
    public static final Item OVERSHOT = new Overshot(STARTER.maxCount(1));
    public static final Item CRYO_RIFLE = new CryoRifle(CYRO.maxCount(1));
    public static final Item KRUZI_RIFLE = new KruziRifle(STARTER.maxCount(1));

    // Pyronium
    public static final Item RAW_PYRONIUM = new Item(STARTER);
    public static final Item PYRONIUM_INGOT = new Item(STARTER);
    public static final Item CLOTH = new Item(STARTER);
    public static final Item PYRONIUM_SWORD = new SwordItem(ModToolMaterials.PYRONIUM, 3, -2.4F, STARTER);
    public static final Item PYRONIUM_PICKAXE = new PickaxeItem(ModToolMaterials.PYRONIUM, 1, -2.8F, STARTER);
    public static final Item PYRONIUM_HELMET = new ArmorItem(ModArmorMaterials.PYRONIUM, EquipmentSlot.HEAD, STARTER);
    public static final Item PYRONIUM_CHESTPLATE = new ArmorItem(ModArmorMaterials.PYRONIUM, EquipmentSlot.CHEST, STARTER);
    public static final Item PYRONIUM_LEGGINGS = new ArmorItem(ModArmorMaterials.PYRONIUM, EquipmentSlot.LEGS, STARTER);
    public static final Item PYRONIUM_BOOTS = new ArmorItem(ModArmorMaterials.PYRONIUM, EquipmentSlot.FEET, STARTER);

    // Amethond
    public static final Item AMETHOND_HELMET = new ArmorItem(ModArmorMaterials.AMETHOND, EquipmentSlot.HEAD, STARTER);
    public static final Item AMETHOND_CHESTPLATE = new ArmorItem(ModArmorMaterials.AMETHOND, EquipmentSlot.CHEST, STARTER);
    public static final Item AMETHOND_LEGGINGS = new ArmorItem(ModArmorMaterials.AMETHOND, EquipmentSlot.LEGS, STARTER);
    public static final Item AMETHOND_BOOTS = new ArmorItem(ModArmorMaterials.AMETHOND, EquipmentSlot.FEET, STARTER);

    // Infernal
    public static final Item INFERNAL_HELMET = new InfernalArmorItem(ModArmorMaterials.INFERNAL, EquipmentSlot.HEAD, STARTER);
    public static final Item INFERNAL_CHESTPLATE = new InfernalArmorItem(ModArmorMaterials.INFERNAL, EquipmentSlot.CHEST, STARTER);
    public static final Item INFERNAL_LEGGINGS = new InfernalArmorItem(ModArmorMaterials.INFERNAL, EquipmentSlot.LEGS, STARTER);
    public static final Item INFERNAL_BOOTS = new InfernalArmorItem(ModArmorMaterials.INFERNAL, EquipmentSlot.FEET, STARTER);

    private static void reg(String id, Item item) {
        Registry.register(Registry.ITEM, new Identifier("sakaplus", id), item);
    }

    public static void register() {
        reg("hp_plus", HP_PLUS);
        reg("light_berry_cheese", LIGHT_BERRY_CHEESE);
        reg("glow_berry_bread", GLOW_BERRY_BREAD);
        reg("hachiwari_manju", HACHIWARI_MANJU);
        reg("light_berry", LIGHT_BERRY);
        reg("chocolate_bar", CHOCOLATE_BAR);
        reg("sweet_berry_pie", SWEET_BERRY_PIE);
        reg("glow_berry_donut", GLOW_BERRY_DONUT);
        reg("kur_brute_meat", KUR_BRUTE_MEAT);
        reg("toufu_a", TOUFU_A);
        reg("hachiwari_manju_choco", HACHIWARI_MANJU_CHOCO);
        reg("light_stone", LIGHT_STONE);
        reg("cryocrystal", CRYOCRYSTAL);

        reg("overshot", OVERSHOT);
        reg("cryo_rifle", CRYO_RIFLE);
        reg("kruzi_rifle", KRUZI_RIFLE);

        reg("raw_pyronium", RAW_PYRONIUM);
        reg("pyronium_ingot", PYRONIUM_INGOT);
        reg("cloth", CLOTH);
        reg("pyronium_sword", PYRONIUM_SWORD);
        reg("pyronium_pickaxe", PYRONIUM_PICKAXE);
        reg("pyronium_helmet", PYRONIUM_HELMET);
        reg("pyronium_chestplate", PYRONIUM_CHESTPLATE);
        reg("pyronium_leggings", PYRONIUM_LEGGINGS);
        reg("pyronium_boots", PYRONIUM_BOOTS);

        reg("amethond_helmet", AMETHOND_HELMET);
        reg("amethond_chestplate", AMETHOND_CHESTPLATE);
        reg("amethond_leggings", AMETHOND_LEGGINGS);
        reg("amethond_boots", AMETHOND_BOOTS);

        reg("infernal_helmet", INFERNAL_HELMET);
        reg("infernal_chestplate", INFERNAL_CHESTPLATE);
        reg("infernal_leggings", INFERNAL_LEGGINGS);
        reg("infernal_boots", INFERNAL_BOOTS);
    }
}
