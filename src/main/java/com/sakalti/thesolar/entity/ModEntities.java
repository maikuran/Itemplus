package com.sakalti.thesolar.entity;

import com.sakalti.thesolar.DimensionMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {
    private ModEntities() {}

    private static ResourceKey<EntityType<?>> key(String id) {
        return ResourceKey.create(Registries.ENTITY_TYPE, DimensionMod.id(id));
    }

    private static <T extends SolarHostileEntity> EntityType<T> register(String id,
            EntityType.EntityFactory<T> factory, float width, float height, int level,
            double hp, double speed, double damage, double range) {
        ResourceKey<EntityType<?>> key = key(id);
        EntityType<T> type = Registry.register(BuiltInRegistries.ENTITY_TYPE, key,
                EntityType.Builder.<T>of((entityType, world) -> {
                    T entity = factory.create(entityType, world);
                    entity.setCombatLevel(level);
                    return entity;
                }, MobCategory.MONSTER).sized(width, height).build(key));
        FabricDefaultAttributeRegistry.register(type, SolarHostileEntity.createAttributes(hp, speed, damage, range));
        return type;
    }

    // Moon: ordinary hostiles Lv.40-62, elite creatures Lv.68-70.
    public static final EntityType<LunarCrawlerEntity> LUNAR_CRAWLER = register("lunar_crawler", LunarCrawlerEntity::new, .8f, 1.1f, 40, 42, .27, 7, 28);
    public static final EntityType<MoonStalkerEntity> MOON_STALKER = register("moon_stalker", MoonStalkerEntity::new, .9f, 1.9f, 46, 58, .34, 9, 34);
    public static final EntityType<CrystalGolemEntity> CRYSTAL_GOLEM = register("crystal_golem", CrystalGolemEntity::new, 1.4f, 2.5f, 62, 135, .20, 15, 30);
    public static final EntityType<LunarSentinelEntity> LUNAR_SENTINEL = register("lunar_sentinel", LunarSentinelEntity::new, .9f, 2.1f, 58, 96, .25, 12, 36);
    public static final EntityType<AstralWispEntity> ASTRAL_WISP = register("astral_wisp", AstralWispEntity::new, .7f, 1.0f, 42, 42, .38, 8, 30);
    public static final EntityType<MoonBeastEntity> MOON_BEAST = register("moon_beast", MoonBeastEntity::new, 1.8f, 1.7f, 68, 205, .30, 18, 38);
    public static final EntityType<SeleneEntity> SELENE = register("selene", SeleneEntity::new, 1.3f, 2.8f, 70, 1600, .30, 24, 56);

    // Mars: ordinary hostiles Lv.50-72, elite creatures Lv.75-78, Ares Lv.80.
    public static final EntityType<MartianScoutEntity> MARTIAN_SCOUT = register("martian_scout", MartianScoutEntity::new, .8f, 1.9f, 50, 66, .31, 11, 36);
    public static final EntityType<RedStalkerEntity> RED_STALKER = register("red_stalker", RedStalkerEntity::new, 1.1f, 1.5f, 55, 82, .36, 12, 34);
    public static final EntityType<AresSoldierEntity> ARES_SOLDIER = register("ares_soldier", AresSoldierEntity::new, .9f, 2.0f, 60, 105, .28, 15, 38);
    public static final EntityType<MartianBruteEntity> MARTIAN_BRUTE = register("martian_brute", MartianBruteEntity::new, 1.4f, 2.7f, 72, 175, .22, 20, 34);
    public static final EntityType<AncientAutomatonEntity> ANCIENT_AUTOMATON = register("ancient_automaton", AncientAutomatonEntity::new, 1.2f, 2.4f, 68, 160, .18, 17, 38);
    public static final EntityType<CrimsonMageEntity> CRIMSON_MAGE = register("crimson_mage", CrimsonMageEntity::new, .8f, 1.9f, 65, 92, .24, 16, 42);
    public static final EntityType<MartianBeastEntity> MARTIAN_BEAST = register("martian_beast", MartianBeastEntity::new, 1.7f, 2.0f, 75, 235, .32, 21, 40);
    public static final EntityType<BloodWyrmEntity> BLOOD_WYRM = register("blood_wyrm", BloodWyrmEntity::new, 2.2f, 1.4f, 78, 260, .26, 22, 42);
    public static final EntityType<AresEntity> ARES = register("ares", AresEntity::new, 1.5f, 3.0f, 80, 2200, .33, 30, 64);

    public static void initialize() {}
}
