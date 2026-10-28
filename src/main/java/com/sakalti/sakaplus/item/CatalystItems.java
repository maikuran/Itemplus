package com.sakalti.sakaplus.item;

import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import com.sakalti.sakaplus.registry.ModTabs; // ModTabsをインポート

public class CatalystItems {

    private static Item.Settings baseSettings() {
        // 設定にカスタムアイテムグループを紐付けます
        return new Item.Settings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB);
    }

    // 魔術触媒
    public static final Item FIRE_CATALYST = new Item(baseSettings());
    public static final Item WIND_CATALYST = new Item(baseSettings());
    public static final Item WATER_CATALYST = new Item(baseSettings());
    public static final Item POISON_CATALYST = new Item(baseSettings());
    public static final Item GEO_CATALYST = new Item(baseSettings());
    public static final Item DEFENSE_CATALYST = new Item(baseSettings());
    public static final Item FORCE_CATALYST = new Item(baseSettings());
    public static final Item STORM_CATALYST = new Item(baseSettings());
    public static final Item COMPASS_CATALYST = new Item(baseSettings());
    public static final Item ELECTRIC_CATALYST = new Item(baseSettings());
    public static final Item KINETIC_CATALYST = new Item(baseSettings());
    public static final Item CELESTIAL_CATALYST = new Item(baseSettings());
    public static final Item HELL_CATALYST = new Item(baseSettings());
    public static final Item LIGHT_CATALYST = new Item(baseSettings());

    // 魔術杖
    public static final Item SHINING_STAFF =
        new ShiningStaffItem(baseSettings());
    public static final Item ADAPTATION_STAFF =
        new AdaptationStaffItem(baseSettings());
    public static final Item WARPER_STAFF =
        new WarperStaffItem(baseSettings());

    public static void registerCatalysts() {
        register("fire_catalyst", FIRE_CATALYST);
        register("wind_catalyst", WIND_CATALYST);
        register("water_catalyst", WATER_CATALYST);
        register("poison_catalyst", POISON_CATALYST);
        register("geo_catalyst", GEO_CATALYST);
        register("defense_catalyst", DEFENSE_CATALYST);
        register("force_catalyst", FORCE_CATALYST);
        register("storm_catalyst", STORM_CATALYST);
        register("compass_catalyst", COMPASS_CATALYST);
        register("electric_catalyst", ELECTRIC_CATALYST);
        register("kinetic_catalyst", KINETIC_CATALYST);
        register("celestial_catalyst", CELESTIAL_CATALYST);
        register("hell_catalyst", HELL_CATALYST);
        register("light_catalyst", LIGHT_CATALYST);

        // 魔術杖の登録
        register("shining_staff", SHINING_STAFF);
        register("adaptation_staff", ADAPTATION_STAFF);
        register("warper_staff", WARPER_STAFF);
    }

    private static void register(String path, Item item) {
        Registry.register(
            Registry.ITEM,
            new Identifier("sakaplus", path),
            item
        );
    }
}
