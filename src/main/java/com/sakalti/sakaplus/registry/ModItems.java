package com.sakalti.sakaplus.registry;

import net.minecraft.item.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.sakalti.sakaplus.item.*;

public class ModItems {

    public static final Item HP_PLUS=new HPPlusItem(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).maxCount(6));
    public static final Item LIGHT_BERRY_CHEESE=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.LIGHT_BERRY_CHEESE));
    public static final Item GLOW_BERRY_BREAD=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.GLOW_BERRY_BREAD));
    public static final Item HACHIWARI_MANJU=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.HACHIWARI_MANJU));
    public static final Item LIGHT_BERRY=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.LIGHT_BERRY));
    public static final Item CHOCOLATE_BAR=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.CHOCOLATE_BAR));
    public static final Item SWEET_BERRY_PIE=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.SWEET_BERRY_PIE));
    public static final Item GLOW_BERRY_DONUT=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.GLOW_BERRY_DONUT));
    public static final Item KUR_BRUTE_MEAT=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.KUR_BRUTE_MEAT));
    public static final Item TOUFU_A=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.TOUFU_A));
    public static final Item HACHIWARI_MANJU_CHOCO=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.HACHIWARI_MANJU_CHOCO));
    public static final Item LIGHT_STONE=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item CRYOCRYSTAL=new Item(new Item.Settings().group(ModTabs.CYROVILIM_TAB));

    public static final Item OVERSHOT=new Overshot(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item CRYO_RIFLE=new CryoRifle(new Item.Settings().group(ModTabs.CYROVILIM_TAB));
    public static final Item KRUZI_RIFLE=new KruziRifle(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item LEATHERGRIP=new Leathergrip(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item RAW_PYRONIUM=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item PYRONIUM_INGOT=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item CLOTH=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item PYRONIUM_SWORD=new SwordItem(ModToolMaterials.PYRONIUM,3,-2.4F,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item PYRONIUM_PICKAXE=new PickaxeItem(ModToolMaterials.PYRONIUM,1,-2.8F,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item DANDELITE_INGOT=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item DANDELITE_SWORD=new SwordItem(ModToolMaterials.DANDELITE,0,-2.4F,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item DANDELITE_PICKAXE=new PickaxeItem(ModToolMaterials.DANDELITE,-2,-2.8F,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item ROSITE_INGOT=new Item(new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item ROSITE_SWORD=new SwordItem(ModToolMaterials.ROSITE,0,-2.4F,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item ROSITE_PICKAXE=new PickaxeItem(ModToolMaterials.ROSITE,-2,-2.8F,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item PYRONIUM_HELMET=new ArmorItem(ModArmorMaterials.PYRONIUM,EquipmentSlot.HEAD,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item PYRONIUM_CHESTPLATE=new ArmorItem(ModArmorMaterials.PYRONIUM,EquipmentSlot.CHEST,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item PYRONIUM_LEGGINGS=new ArmorItem(ModArmorMaterials.PYRONIUM,EquipmentSlot.LEGS,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item PYRONIUM_BOOTS=new ArmorItem(ModArmorMaterials.PYRONIUM,EquipmentSlot.FEET,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item AMETHOND_HELMET=new ArmorItem(ModArmorMaterials.AMETHOND,EquipmentSlot.HEAD,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item AMETHOND_CHESTPLATE=new ArmorItem(ModArmorMaterials.AMETHOND,EquipmentSlot.CHEST,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item AMETHOND_LEGGINGS=new ArmorItem(ModArmorMaterials.AMETHOND,EquipmentSlot.LEGS,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item AMETHOND_BOOTS=new ArmorItem(ModArmorMaterials.AMETHOND,EquipmentSlot.FEET,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static final Item INFERNAL_HELMET=new InfernalArmorItem(ModArmorMaterials.INFERNAL,EquipmentSlot.HEAD,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item INFERNAL_CHESTPLATE=new InfernalArmorItem(ModArmorMaterials.INFERNAL,EquipmentSlot.CHEST,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item INFERNAL_LEGGINGS=new InfernalArmorItem(ModArmorMaterials.INFERNAL,EquipmentSlot.LEGS,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));
    public static final Item INFERNAL_BOOTS=new InfernalArmorItem(ModArmorMaterials.INFERNAL,EquipmentSlot.FEET,new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB));

    public static void register() {
        String[] names={"light_berry_cheese","hachiwari_manju","glow_berry_bread","light_berry","kur_brute_meat","chocolate_bar","sweet_berry_pie","glow_berry_donut","hp_plus","light_stone","cryocrystal","toufu_a","hachiwari_manju_choco","overshot","leathergrip","kruzi_rifle","cryo_rifle","raw_pyronium","pyronium_ingot","cloth","pyronium_sword","pyronium_pickaxe","pyronium_helmet","pyronium_chestplate","pyronium_leggings","pyronium_boots","amethond_helmet","amethond_chestplate","amethond_leggings","amethond_boots","infernal_helmet","infernal_chestplate","infernal_leggings","infernal_boots","dandelite_ingot","dandelite_sword","dandelite_pickaxe","rosite_ingot","rosite_sword","rosite_pickaxe"};
        Item[] items={LIGHT_BERRY_CHEESE,HACHIWARI_MANJU,GLOW_BERRY_BREAD,LIGHT_BERRY,KUR_BRUTE_MEAT,CHOCOLATE_BAR,SWEET_BERRY_PIE,GLOW_BERRY_DONUT,HP_PLUS,LIGHT_STONE,CRYOCRYSTAL,TOUFU_A,HACHIWARI_MANJU_CHOCO,OVERSHOT,LEATHERGRIP,KRUZI_RIFLE,CRYO_RIFLE,RAW_PYRONIUM,PYRONIUM_INGOT,CLOTH,PYRONIUM_SWORD,PYRONIUM_PICKAXE,PYRONIUM_HELMET,PYRONIUM_CHESTPLATE,PYRONIUM_LEGGINGS,PYRONIUM_BOOTS,AMETHOND_HELMET,AMETHOND_CHESTPLATE,AMETHOND_LEGGINGS,AMETHOND_BOOTS,INFERNAL_HELMET,INFERNAL_CHESTPLATE,INFERNAL_LEGGINGS,INFERNAL_BOOTS,DANDELITE_INGOT,DANDELITE_SWORD,DANDELITE_PICKAXE,ROSITE_INGOT,ROSITE_SWORD,ROSITE_PICKAXE};
        for(int i=0;i<names.length;i++) Registry.register(Registry.ITEM,new Identifier("sakaplus",names[i]),items[i]);
    }
}
