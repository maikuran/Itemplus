package com.sakalti.sakaplus.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CatalystItems {

    // クリエイティブタブや基本設定用の共通セッティング
    private static Item.Settings baseSettings() {
        return new Item.Settings();
    }

    // 15種類の魔術触媒（Catalysts）の定義
    public static final Item FIRE_CATALYST = new Item(baseSettings());      // 業火の触媒
    public static final Item WIND_CATALYST = new Item(baseSettings());      // 烈風の触媒
    public static final Item WATER_CATALYST = new Item(baseSettings());    // 清流の触媒
    public static final Item POISON_CATALYST = new Item(baseSettings());    // 猛毒の触媒
    public static final Item GEO_CATALYST = new Item(baseSettings());       // 大地の触媒
    public static final Item DEFENSE_CATALYST = new Item(baseSettings());   // 金剛の触媒
    public static final Item FORCE_CATALYST = new Item(baseSettings());     // 斥力の触媒
    public static final Item STORM_CATALYST = new Item(baseSettings());     // 嵐の触媒
    public static final Item COMPASS_CATALYST = new Item(baseSettings());   // 定位の触媒
    public static final Item ELECTRIC_CATALYST = new Item(baseSettings());  // 雷電の触媒
    public static final Item KINETIC_CATALYST = new Item(baseSettings());   // 動力の触媒
    public static final Item CELESTIAL_CATALYST = new Item(baseSettings()); // 星天の触媒
    public static final Item HELL_CATALYST = new Item(baseSettings());      // 冥府の触媒
    public static final Item LIGHT_CATALYST = new Item(baseSettings());     // 聖光の触媒

    /**
     * すべての触媒アイテムをゲーム内に登録するメソッド
     * MODの初期化クラス（ModInitializer）の onInitialize 内で呼び出してください。
     */
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
    }

    private static void register(String path, Item item) {
        Registry.register(Registries.ITEM, new Identifier("sakaplus", path), item);
    }
}
