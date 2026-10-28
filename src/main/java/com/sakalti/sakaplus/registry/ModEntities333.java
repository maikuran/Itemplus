
package com.sakalti.sakaplus.registry;

import java.util.function.Predicate;
import com.sakalti.sakaplus.entity.*;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BiomeKeys;

public class ModEntities333 {
    private static final String MOD_ID = "sakaplus";

    private static <T extends Entity> EntityType<T> registerEntity(String name, SpawnGroup group, EntityType.EntityFactory<T> factory, float width, float height) {
        return registerEntity(name, group, factory, width, height, 10, 3);
    }

    private static <T extends Entity> EntityType<T> registerEntity(String name, SpawnGroup group, EntityType.EntityFactory<T> factory, float width, float height, int tracking, int update) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(MOD_ID, name),
                FabricEntityTypeBuilder.create(group, factory)
                        .dimensions(EntityDimensions.fixed(width, height))
                        .trackRangeBlocks(tracking).trackedUpdateRate(update).build());
    }

    // ENTITIES
    public static final EntityType<ScorcherEntity> SCORCHER = registerEntity("scorcher", SpawnGroup.MONSTER, ScorcherEntity::new, 0.6F, 1.8F);
    public static final EntityType<KuloogieEntity> KULOOGIE = registerEntity("kuloogie", SpawnGroup.MONSTER, KuloogieEntity::new, 0.8F, 1.2F);
    public static final EntityType<KurBruteEntity> KUR_BRUTE = registerEntity("kur_brute", SpawnGroup.CREATURE, KurBruteEntity::new, 0.5F, 1.1F);
    public static final EntityType<KruziAntEntity> KRUZI_ANT = registerEntity("kruzi_ant", SpawnGroup.MONSTER, KruziAntEntity::new, 0.5F, 0.5F, 64, 3);
    public static final EntityType<CryofreezerEntity> CRYOFREEZER = registerEntity("cryofreezer", SpawnGroup.MONSTER, CryofreezerEntity::new, 1.5F, 1.5F, 64, 3);
    public static final EntityType<CryomageEntity> CRYOMAGE = registerEntity("cryomage", SpawnGroup.MONSTER, CryomageEntity::new, 1.5F, 1.5F, 64, 3);
    public static final EntityType<CryowarriorEntity> CRYOWARRIOR = registerEntity("cryowarrior", SpawnGroup.MONSTER, CryowarriorEntity::new, 0.8F, 2.0F, 64, 3);
    public static final EntityType<CryobeastEntity> CRYOBEAST = registerEntity("cryobeast", SpawnGroup.MONSTER, CryobeastEntity::new, 1.2F, 1.8F, 64, 3);
    public static final EntityType<CryoSnakeEntity> CRYOSNAKE = registerEntity("cryosnake", SpawnGroup.MONSTER, CryoSnakeEntity::new, 2.0F, 1.2F, 64, 3);
    public static final EntityType<WarperdunEntity> WARPERDUN = registerEntity("warperdun", SpawnGroup.MONSTER, WarperdunEntity::new, 1.4F, 2.1F);
    public static final EntityType<OganesonEntity> OGANESON = registerEntity("oganeson", SpawnGroup.MONSTER, OganesonEntity::new, 0.6F, 1.8F);
    public static final EntityType<NyankoEntity> NYANKO = registerEntity("nyanko", SpawnGroup.MONSTER, NyankoEntity::new, 0.8F, 1.2F);
    public static final EntityType<BattleNyankoEntity> BATTLE_NYANKO = registerEntity("battle_nyanko", SpawnGroup.MONSTER, BattleNyankoEntity::new, 0.8F, 1.2F);
    public static final EntityType<ShyronEntity> SHYRON = registerEntity("shyron", SpawnGroup.MONSTER, ShyronEntity::new, 1.5F, 1.5F, 64, 3);
    public static final EntityType<XxenaEntity> XXENA = registerEntity("xxena", SpawnGroup.MONSTER, XxenaEntity::new, 1.75F, 1.5F, 64, 3);

    // SPAWN EGGS
    private static Item registerSpawnEgg(String name, EntityType<? extends MobEntity> type, int primary, int secondary, ItemGroup group) {
        Identifier id = new Identifier(MOD_ID, name);
        if (Registry.ITEM.containsId(id)) throw new IllegalStateException("Duplicate spawn egg registration: " + id);
        return Registry.register(Registry.ITEM, id, new SpawnEggItem(type, primary, secondary, new Item.Settings().group(group)));
    }

    // STARTER AND KRUZIVILIM
    public static final Item SCORCHER_SPAWN_EGG = registerSpawnEgg("scorcher_spawn_egg", SCORCHER, 0xAA0000, 0xFF5500, ModTabs.STARTER_AND_KRUZIVILIM_TAB);
    public static final Item WARPERDUN_SPAWN_EGG = registerSpawnEgg("warperdun_spawn_egg", WARPERDUN, 0x003366, 0x660099, ModTabs.STARTER_AND_KRUZIVILIM_TAB);
    public static final Item OGANESON_SPAWN_EGG = registerSpawnEgg("oganeson_spawn_egg", OGANESON, 0x336699, 0x00FFFF, ModTabs.STARTER_AND_KRUZIVILIM_TAB);
    public static final Item NYANKO_SPAWN_EGG = registerSpawnEgg("nyanko_spawn_egg", NYANKO, 0xFFFFCC, 0xFF66CC, ModTabs.STARTER_AND_KRUZIVILIM_TAB);
    public static final Item BATTLE_NYANKO_SPAWN_EGG = registerSpawnEgg("battle_nyanko_spawn_egg", BATTLE_NYANKO, 0xFFFFCC, 0x999999, ModTabs.STARTER_AND_KRUZIVILIM_TAB);
    public static final Item KUR_BRUTE_SPAWN_EGG = registerSpawnEgg("kur_brute_spawn_egg", KUR_BRUTE, 0x999900, 0x999999, ModTabs.STARTER_AND_KRUZIVILIM_TAB);
    public static final Item KULOOGIE_SPAWN_EGG = registerSpawnEgg("kuloogie_spawn_egg", KULOOGIE, 0x009900, 0x333333, ModTabs.STARTER_AND_KRUZIVILIM_TAB);
    public static final Item KRUZI_ANT_SPAWN_EGG = registerSpawnEgg("kruzi_ant_spawn_egg", KRUZI_ANT, 0xD4AF37, 0xFFD700, ModTabs.STARTER_AND_KRUZIVILIM_TAB);
    public static final Item SHYRON_SPAWN_EGG = registerSpawnEgg("shyron_spawn_egg", SHYRON, 0x555577, 0xAAAAAA, ModTabs.STARTER_AND_KRUZIVILIM_TAB);
    public static final Item XXENA_SPAWN_EGG = registerSpawnEgg("xxena_spawn_egg", XXENA, 0x8A2BE2, 0xFF66CC, ModTabs.STARTER_AND_KRUZIVILIM_TAB);

    // CYROVILIM
    public static final Item CRYOFREEZER_SPAWN_EGG = registerSpawnEgg("cryofreezer_spawn_egg", CRYOFREEZER, 0x336699, 0x00FFFF, ModTabs.CYROVILIM_TAB);
    public static final Item CRYOMAGE_SPAWN_EGG = registerSpawnEgg("cryomage_spawn_egg", CRYOMAGE, 0x66CCFF, 0x2244AA, ModTabs.CYROVILIM_TAB);
    public static final Item CRYOWARRIOR_SPAWN_EGG = registerSpawnEgg("cryowarrior_spawn_egg", CRYOWARRIOR, 0x3366CC, 0x99DDFF, ModTabs.CYROVILIM_TAB);
    public static final Item CRYOBEAST_SPAWN_EGG = registerSpawnEgg("cryobeast_spawn_egg", CRYOBEAST, 0x174B8A, 0xA8E8FF, ModTabs.CYROVILIM_TAB);
    public static final Item CRYOSNAKE_SPAWN_EGG = registerSpawnEgg("cryosnake_spawn_egg", CRYOSNAKE, 0x00AA88, 0x55FFCC, ModTabs.CYROVILIM_TAB);

    // ATTRIBUTES
    private static <T extends LivingEntity> void registerAttributes(EntityType<T> type, DefaultAttributeContainer.Builder attributes) {
        FabricDefaultAttributeRegistry.register(type, attributes);
    }

    private static void registerAllAttributes() {
        registerAttributes(SCORCHER, ScorcherEntity.createAttributes());
        registerAttributes(KULOOGIE, KuloogieEntity.createAttributes());
        registerAttributes(KUR_BRUTE, KurBruteEntity.createAttributes());
        registerAttributes(KRUZI_ANT, KruziAntEntity.createKruziAntAttributes());
        registerAttributes(CRYOFREEZER, CryofreezerEntity.createAttributes());
        registerAttributes(CRYOMAGE, CryomageEntity.createAttributes());
        registerAttributes(CRYOWARRIOR, CryowarriorEntity.createAttributes());
        registerAttributes(CRYOBEAST, CryobeastEntity.createAttributes());
        registerAttributes(CRYOSNAKE, CryoSnakeEntity.createAttributes());
        registerAttributes(WARPERDUN, WarperdunEntity.createAttributes());
        registerAttributes(OGANESON, OganesonEntity.createAttributes());
        registerAttributes(NYANKO, NyankoEntity.createAttributes());
        registerAttributes(BATTLE_NYANKO, BattleNyankoEntity.createAttributes());
        registerAttributes(SHYRON, ShyronEntity.createAttributes());
        registerAttributes(XXENA, XxenaEntity.createAttributes());
    }

    // NATURAL SPAWNING
    private static void registerNaturalSpawn(EntityType<? extends MobEntity> entity, Predicate<BiomeSelectionContext> selector, SpawnGroup group, int weight, int min, int max) {
        BiomeModifications.addSpawn(selector, group, entity, weight, min, max);
    }

    private static void registerNaturalSpawns() {
        registerNaturalSpawn(SCORCHER, BiomeSelectors.includeByKey(BiomeKeys.CRIMSON_FOREST), SpawnGroup.MONSTER, 20, 1, 5);
        registerNaturalSpawn(NYANKO, BiomeSelectors.includeByKey(BiomeKeys.PLAINS), SpawnGroup.MONSTER, 11, 1, 5);
        registerNaturalSpawn(OGANESON, BiomeSelectors.includeByKey(BiomeKeys.WARPED_FOREST, BiomeKeys.CRIMSON_FOREST), SpawnGroup.MONSTER, 20, 1, 6);
    }

    public static void register() {
        registerAllAttributes();
        registerNaturalSpawns();
    }
}
