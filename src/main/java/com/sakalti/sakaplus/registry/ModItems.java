package com.sakalti.sakaplus.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SwordItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.sakalti.sakaplus.block.ModBlocks2;
import com.sakalti.sakaplus.item.*;
import com.sakalti.sakaplus.effect.ModEffects;

public class ModItems {

    // ==================== 食料・通常アイテム ====================

    public static final Item BLOOMSQUER_ITEM = new BlockItem(
        ModBlocks2.BLOOMSQUER,
        new Item.Settings()
            .group(ItemGroup.FOOD)
            .food(ModFoods.BLOOMSQUER)
            .maxCount(16)
    );

    public static final Item HP_PLUS =
        new HPPlusItem(new Item.Settings().maxCount(6));

    public static final Item LIGHT_BERRY_CHEESE =
        new Item(new Item.Settings().food(ModFoods.LIGHT_BERRY_CHEESE));

    public static final Item GLOW_BERRY_BREAD =
        new Item(new Item.Settings().food(ModFoods.GLOW_BERRY_BREAD));

    public static final Item HACHIWARI_MANJU =
        new Item(new Item.Settings().food(ModFoods.HACHIWARI_MANJU));

    public static final Item LIGHT_BERRY =
        new Item(new Item.Settings().food(ModFoods.LIGHT_BERRY));

    public static final Item CHOCOLATE_BAR =
        new Item(new Item.Settings().food(ModFoods.CHOCOLATE_BAR));

    public static final Item SWEET_BERRY_PIE =
        new Item(new Item.Settings().food(ModFoods.SWEET_BERRY_PIE));

    public static final Item GLOW_BERRY_DONUT =
        new Item(new Item.Settings().food(ModFoods.GLOW_BERRY_DONUT));

    public static final Item KUR_BRUTE_MEAT =
        new Item(new Item.Settings().food(ModFoods.KUR_BRUTE_MEAT));

    public static final Item TOUFU_A =
        new Item(new Item.Settings().food(ModFoods.TOUFU_A));

    public static final Item HACHIWARI_MANJU_CHOCO =
        new Item(new Item.Settings().food(ModFoods.HACHIWARI_MANJU_CHOCO));

    public static final Item LIGHT_STONE =
        new Item(new Item.Settings());

    // ==================== 武器 ====================

    public static final Item OVERSHOT =
        new Overshot(
            new Item.Settings()
                .group(ItemGroup.COMBAT)
                .maxCount(1)
        );

    public static final Item KRUZI_RIFLE =
        new KruziRifle(
            new Item.Settings()
                .group(ItemGroup.COMBAT)
                .maxCount(1)
        );

    // ==================== Pyronium シリーズ ====================

    public static final Item RAW_PYRONIUM =
        new Item(new Item.Settings().group(ItemGroup.MATERIALS));

    public static final Item PYRONIUM_INGOT =
        new Item(new Item.Settings().group(ItemGroup.MATERIALS));

    public static final Item PYRONIUM_SWORD =
        new SwordItem(
            ModToolMaterials.PYRONIUM,
            3,
            -2.4F,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    public static final Item PYRONIUM_PICKAXE =
        new PickaxeItem(
            ModToolMaterials.PYRONIUM,
            1,
            -2.8F,
            new Item.Settings().group(ItemGroup.TOOLS)
        );

    public static final Item PYRONIUM_HELMET =
        new ArmorItem(
            ModArmorMaterials.PYRONIUM,
            EquipmentSlot.HEAD,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    public static final Item PYRONIUM_CHESTPLATE =
        new ArmorItem(
            ModArmorMaterials.PYRONIUM,
            EquipmentSlot.CHEST,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    public static final Item PYRONIUM_LEGGINGS =
        new ArmorItem(
            ModArmorMaterials.PYRONIUM,
            EquipmentSlot.LEGS,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    public static final Item PYRONIUM_BOOTS =
        new ArmorItem(
            ModArmorMaterials.PYRONIUM,
            EquipmentSlot.FEET,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    // ==================== Amethond シリーズ ====================

    public static final Item AMETHOND_HELMET =
        new ArmorItem(
            ModArmorMaterials.AMETHOND,
            EquipmentSlot.HEAD,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    public static final Item AMETHOND_CHESTPLATE =
        new ArmorItem(
            ModArmorMaterials.AMETHOND,
            EquipmentSlot.CHEST,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    public static final Item AMETHOND_LEGGINGS =
        new ArmorItem(
            ModArmorMaterials.AMETHOND,
            EquipmentSlot.LEGS,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    public static final Item AMETHOND_BOOTS =
        new ArmorItem(
            ModArmorMaterials.AMETHOND,
            EquipmentSlot.FEET,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    // ==================== Infernal シリーズ ====================

    public static final Item INFERNAL_HELMET =
        new ArmorItem(
            ModArmorMaterials.INFERNAL,
            EquipmentSlot.HEAD,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    public static final Item INFERNAL_CHESTPLATE =
        new ArmorItem(
            ModArmorMaterials.INFERNAL,
            EquipmentSlot.CHEST,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    public static final Item INFERNAL_LEGGINGS =
        new ArmorItem(
            ModArmorMaterials.INFERNAL,
            EquipmentSlot.LEGS,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    public static final Item INFERNAL_BOOTS =
        new ArmorItem(
            ModArmorMaterials.INFERNAL,
            EquipmentSlot.FEET,
            new Item.Settings().group(ItemGroup.COMBAT)
        );

    // ==========================================================

    public static void register() {

        // ==================== 食料・通常アイテム登録 ====================

        Registry.register(Registry.ITEM, new Identifier("sakaplus", "bloomsquer"), BLOOMSQUER_ITEM);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "light_berry_cheese"), LIGHT_BERRY_CHEESE);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "hachiwari_manju"), HACHIWARI_MANJU);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "glow_berry_bread"), GLOW_BERRY_BREAD);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "light_berry"), LIGHT_BERRY);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "kur_brute_meat"), KUR_BRUTE_MEAT);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "chocolate_bar"), CHOCOLATE_BAR);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "sweet_berry_pie"), SWEET_BERRY_PIE);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "glow_berry_donut"), GLOW_BERRY_DONUT);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "hp_plus"), HP_PLUS);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "light_stone"), LIGHT_STONE);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "toufu_a"), TOUFU_A);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "hachiwari_manju_choco"), HACHIWARI_MANJU_CHOCO);

        // ==================== 武器登録 ====================

        Registry.register(Registry.ITEM, new Identifier("sakaplus", "overshot"), OVERSHOT);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "kruzi_rifle"), KRUZI_RIFLE);

        // ==================== Pyronium 登録 ====================

        Registry.register(Registry.ITEM, new Identifier("sakaplus", "raw_pyronium"), RAW_PYRONIUM);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "pyronium_ingot"), PYRONIUM_INGOT);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "pyronium_sword"), PYRONIUM_SWORD);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "pyronium_pickaxe"), PYRONIUM_PICKAXE);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "pyronium_helmet"), PYRONIUM_HELMET);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "pyronium_chestplate"), PYRONIUM_CHESTPLATE);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "pyronium_leggings"), PYRONIUM_LEGGINGS);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "pyronium_boots"), PYRONIUM_BOOTS);

        // ==================== Amethond 登録 ====================

        Registry.register(Registry.ITEM, new Identifier("sakaplus", "amethond_helmet"), AMETHOND_HELMET);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "amethond_chestplate"), AMETHOND_CHESTPLATE);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "amethond_leggings"), AMETHOND_LEGGINGS);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "amethond_boots"), AMETHOND_BOOTS);

        // ==================== Infernal 登録 ====================

        Registry.register(Registry.ITEM, new Identifier("sakaplus", "infernal_helmet"), INFERNAL_HELMET);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "infernal_chestplate"), INFERNAL_CHESTPLATE);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "infernal_leggings"), INFERNAL_LEGGINGS);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "infernal_boots"), INFERNAL_BOOTS);
    }
}
