
package com.sakalti.sakaplus.registry;

import com.sakalti.sakaplus.entity.*;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BiomeKeys;

public class ModEntities333 {

    private static final String MOD_ID = "sakaplus";

    // =========================================================
    // Entity Registration Function
    // =========================================================

    private static <T extends Entity> EntityType<T> registerEntity(
            String name,
            SpawnGroup group,
            EntityType.EntityFactory<T> factory,
            float width,
            float height
    ) {
        return registerEntity(name, group, factory, width, height, 0, 0);
    }

    private static <T extends Entity> EntityType<T> registerEntity(
            String name,
            SpawnGroup group,
            EntityType.EntityFactory<T> factory,
            float width,
            float height,
            int trackingRange,
            int updateRate
    ) {
        FabricEntityTypeBuilder<T> builder =
                FabricEntityTypeBuilder.create(group, factory)
                        .dimensions(EntityDimensions.fixed(width, height));

        if (trackingRange > 0) {
            builder.trackRangeBlocks(trackingRange);
        }

        if (updateRate > 0) {
            builder.trackedUpdateRate(updateRate);
        }

        return Registry.register(
                Registry.ENTITY_TYPE,
                new Identifier(MOD_ID, name),
                builder.build()
        );
    }

    // =========================================================
    // Entity Types
    // =========================================================

    public static final EntityType<ScorcherEntity> SCORCHER =
            registerEntity("scorcher", SpawnGroup.MONSTER,
                    ScorcherEntity::new, 0.6F, 1.8F);

    public static final EntityType<KuloogieEntity> KULOOGIE =
            registerEntity("kuloogie", SpawnGroup.MONSTER,
                    KuloogieEntity::new, 0.8F, 1.2F);

    public static final EntityType<KurBruteEntity> KUR_BRUTE =
            registerEntity("kur_brute", SpawnGroup.CREATURE,
                    KurBruteEntity::new, 0.5F, 1.1F);

    public static final EntityType<KruziAntEntity> KRUZI_ANT =
            registerEntity("kruzi_ant", SpawnGroup.MONSTER,
                    KruziAntEntity::new, 0.5F, 0.5F, 64, 3);

    public static final EntityType<WarperdunEntity> WARPERDUN =
            registerEntity("warperdun", SpawnGroup.MONSTER,
                    WarperdunEntity::new, 1.4F, 2.1F);

    public static final EntityType<OganesonEntity> OGANESON =
            registerEntity("oganeson", SpawnGroup.MONSTER,
                    OganesonEntity::new, 0.6F, 1.8F);

    public static final EntityType<NyankoEntity> NYANKO =
            registerEntity("nyanko", SpawnGroup.MONSTER,
                    NyankoEntity::new, 0.8F, 1.2F);

    public static final EntityType<BattleNyankoEntity> BATTLE_NYANKO =
            registerEntity("battle_nyanko", SpawnGroup.MONSTER,
                    BattleNyankoEntity::new, 0.8F, 1.2F);

    public static final EntityType<ShyronEntity> SHYRON =
            registerEntity("shyron", SpawnGroup.MONSTER,
                    ShyronEntity::new, 1.0F, 1.5F, 64, 3);

    // Xxena
    public static final EntityType<XxenaEntity> XXENA =
            registerEntity("xxena", SpawnGroup.MONSTER,
                    XxenaEntity::new, 1.0F, 1.5F, 64, 3);

    // =========================================================
    // Spawn Egg Registration Function
    // =========================================================

    private static Item registerSpawnEgg(
            String name,
            EntityType<? extends MobEntity> entityType,
            int primaryColor,
            int secondaryColor
    ) {
        return Registry.register(
                Registry.ITEM,
                new Identifier(MOD_ID, name),
                new SpawnEggItem(
                        entityType,
                        primaryColor,
                        secondaryColor,
                        new Item.Settings()
                )
        );
    }

    // =========================================================
    // Spawn Eggs
    // =========================================================

    public static final Item SCORCHER_SPAWN_EGG =
            registerSpawnEgg("scorcher_spawn_egg", SCORCHER,
                    0xAA0000, 0xFF5500);

    public static final Item WARPERDUN_SPAWN_EGG =
            registerSpawnEgg("warperdun_spawn_egg", WARPERDUN,
                    0x003366, 0x660099);

    public static final Item OGANESON_SPAWN_EGG =
            registerSpawnEgg("oganeson_spawn_egg", OGANESON,
                    0x336699, 0x00FFFF);

    public static final Item NYANKO_SPAWN_EGG =
            registerSpawnEgg("nyanko_spawn_egg", NYANKO,
                    0xFFFFCC, 0xFF66CC);

    public static final Item BATTLE_NYANKO_SPAWN_EGG =
            registerSpawnEgg("battle_nyanko_spawn_egg", BATTLE_NYANKO,
                    0xFFFFCC, 0x999999);

    public static final Item KUR_BRUTE_SPAWN_EGG =
            registerSpawnEgg("kur_brute_spawn_egg", KUR_BRUTE,
                    0x999900, 0x999999);

    public static final Item KULOOGIE_SPAWN_EGG =
            registerSpawnEgg("kuloogie_spawn_egg", KULOOGIE,
                    0x009900, 0x333333);

    public static final Item KRUZI_ANT_SPAWN_EGG =
            registerSpawnEgg("kruzi_ant_spawn_egg", KRUZI_ANT,
                    0xD4AF37, 0xFFD700);

    public static final Item SHYRON_SPAWN_EGG =
            registerSpawnEgg("shyron_spawn_egg", SHYRON,
                    0x555577, 0xAAAAAA);

    // Xxena Spawn Egg
    public static final Item XXENA_SPAWN_EGG =
            registerSpawnEgg("xxena_spawn_egg", XXENA,
                    0x8A2BE2, 0xFF66CC);

    // =========================================================
    // Attribute Registration Function
    // =========================================================

    private static <T extends LivingEntity> void registerAttributes(
            EntityType<T> entityType,
            DefaultAttributeContainer.Builder attributes
    ) {
        FabricDefaultAttributeRegistry.register(entityType, attributes);
    }

    // =========================================================
    // Registration
    // =========================================================

    public static void register() {

        // Attributes

        registerAttributes(SCORCHER, ScorcherEntity.createAttributes());
        registerAttributes(KULOOGIE, KuloogieEntity.createAttributes());
        registerAttributes(KUR_BRUTE, KurBruteEntity.createAttributes());
        registerAttributes(KRUZI_ANT, KruziAntEntity.createKruziAntAttributes());
        registerAttributes(WARPERDUN, WarperdunEntity.createAttributes());
        registerAttributes(OGANESON, OganesonEntity.createAttributes());
        registerAttributes(NYANKO, NyankoEntity.createAttributes());
        registerAttributes(BATTLE_NYANKO, BattleNyankoEntity.createAttributes());
        registerAttributes(SHYRON, ShyronEntity.createAttributes());
        registerAttributes(XXENA, XxenaEntity.createAttributes());

        // =====================================================
        // Natural Spawning
        // =====================================================

        // Scorcher: Crimson Forest
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.CRIMSON_FOREST),
                SpawnGroup.MONSTER,
                SCORCHER,
                20, 1, 5
        );

        // Nyanko: Plains
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.PLAINS),
                SpawnGroup.MONSTER,
                NYANKO,
                11, 1, 5
        );

        // Oganeson: Warped Forest and Crimson Forest
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(
                        BiomeKeys.WARPED_FOREST,
                        BiomeKeys.CRIMSON_FOREST
                ),
                SpawnGroup.MONSTER,
                OGANESON,
                20, 1, 6
        );
    }
}
