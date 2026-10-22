
package com.sakalti.sakaplus.registry;

import com.sakalti.sakaplus.SakaplusMod2;
import com.sakalti.sakaplus.entity.*;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BiomeKeys;

public class ModEntities333 {

    // =========================================================
    // Entity Types
    // =========================================================

    public static final EntityType<ScorcherEntity> SCORCHER =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier("sakaplus", "scorcher"),
                    FabricEntityTypeBuilder.create(
                                    SpawnGroup.MONSTER,
                                    ScorcherEntity::new
                            )
                            .dimensions(EntityDimensions.fixed(0.6F, 1.8F))
                            .build()
            );

    public static final EntityType<KuloogieEntity> KULOOGIE =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier("sakaplus", "kuloogie"),
                    FabricEntityTypeBuilder.create(
                                    SpawnGroup.MONSTER,
                                    KuloogieEntity::new
                            )
                            .dimensions(EntityDimensions.fixed(1.2F, 1.2F))
                            .build()
            );

    public static final EntityType<KurBruteEntity> KUR_BRUTE =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier("sakaplus", "kur_brute"),
                    FabricEntityTypeBuilder.create(
                                    SpawnGroup.CREATURE,
                                    KurBruteEntity::new
                            )
                            .dimensions(EntityDimensions.fixed(2.0F, 2.0F))
                            .build()
            );

    public static final EntityType<KruziAntEntity> KRUZI_ANT =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier("sakaplus", "kruzi_ant"),
                    FabricEntityTypeBuilder.create(
                                    SpawnGroup.MONSTER,
                                    KruziAntEntity::new
                            )
                            .dimensions(EntityDimensions.fixed(0.9F, 0.9F))
                            .trackRangeBlocks(64)
                            .trackedUpdateRate(3)
                            .build()
            );

    public static final EntityType<WarperdunEntity> WARPERDUN =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier("sakaplus", "warperdun"),
                    FabricEntityTypeBuilder.create(
                                    SpawnGroup.MONSTER,
                                    WarperdunEntity::new
                            )
                            .dimensions(EntityDimensions.fixed(3.0F, 3.0F))
                            .build()
            );

    public static final EntityType<OganesonEntity> OGANESON =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier("sakaplus", "oganeson"),
                    FabricEntityTypeBuilder.create(
                                    SpawnGroup.MONSTER,
                                    OganesonEntity::new
                            )
                            .dimensions(EntityDimensions.fixed(0.6F, 1.8F))
                            .build()
            );

    public static final EntityType<NyankoEntity> NYANKO =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier("sakaplus", "nyanko"),
                    FabricEntityTypeBuilder.create(
                                    SpawnGroup.MONSTER,
                                    NyankoEntity::new
                            )
                            .dimensions(EntityDimensions.fixed(0.8F, 0.8F))
                            .build()
            );

    public static final EntityType<BattleNyankoEntity> BATTLE_NYANKO =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier("sakaplus", "battle_nyanko"),
                    FabricEntityTypeBuilder.create(
                                    SpawnGroup.MONSTER,
                                    BattleNyankoEntity::new
                            )
                            .dimensions(EntityDimensions.fixed(0.8F, 0.8F))
                            .build()
            );

    // =========================================================
    // Spawn Eggs
    // =========================================================

    public static final Item SCORCHER_SPAWN_EGG =
            Registry.register(
                    Registry.ITEM,
                    new Identifier("sakaplus", "scorcher_spawn_egg"),
                    new SpawnEggItem(
                            SCORCHER,
                            0xAA0000,
                            0xFF5500,
                            new Item.Settings()
                    )
            );

    public static final Item WARPERDUN_SPAWN_EGG =
            Registry.register(
                    Registry.ITEM,
                    new Identifier("sakaplus", "warperdun_spawn_egg"),
                    new SpawnEggItem(
                            WARPERDUN,
                            0x003366,
                            0x660099,
                            new Item.Settings()
                    )
            );

    public static final Item OGANESON_SPAWN_EGG =
            Registry.register(
                    Registry.ITEM,
                    new Identifier("sakaplus", "oganeson_spawn_egg"),
                    new SpawnEggItem(
                            OGANESON,
                            0x336699,
                            0x00FFFF,
                            new Item.Settings()
                    )
            );

    public static final Item NYANKO_SPAWN_EGG =
            Registry.register(
                    Registry.ITEM,
                    new Identifier("sakaplus", "nyanko_spawn_egg"),
                    new SpawnEggItem(
                            NYANKO,
                            0xFFFFCC,
                            0xFF66CC,
                            new Item.Settings()
                    )
            );

    public static final Item BATTLE_NYANKO_SPAWN_EGG =
            Registry.register(
                    Registry.ITEM,
                    new Identifier("sakaplus", "battle_nyanko_spawn_egg"),
                    new SpawnEggItem(
                            BATTLE_NYANKO,
                            0xFFFFCC,
                            0x999999,
                            new Item.Settings()
                    )
            );

    public static final Item KUR_BRUTE_SPAWN_EGG =
            Registry.register(
                    Registry.ITEM,
                    new Identifier("sakaplus", "kur_brute_spawn_egg"),
                    new SpawnEggItem(
                            KUR_BRUTE,
                            0x999900,
                            0x999999,
                            new Item.Settings()
                    )
            );

    public static final Item KULOOGIE_SPAWN_EGG =
            Registry.register(
                    Registry.ITEM,
                    new Identifier("sakaplus", "kuloogie_spawn_egg"),
                    new SpawnEggItem(
                            KULOOGIE,
                            0x009900,
                            0x333333,
                            new Item.Settings()
                    )
            );

    public static final Item KRUZI_ANT_SPAWN_EGG =
            Registry.register(
                    Registry.ITEM,
                    new Identifier("sakaplus", "kruzi_ant_spawn_egg"),
                    new SpawnEggItem(
                            KRUZI_ANT,
                            0xD4AF37,
                            0xFFD700,
                            new Item.Settings()
                    )
            );

    // =========================================================
    // Registration
    // =========================================================

    public static void register() {

        // Entity attributes
        FabricDefaultAttributeRegistry.register(
                SCORCHER,
                ScorcherEntity.createAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                KULOOGIE,
                KuloogieEntity.createAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                KUR_BRUTE,
                KurBruteEntity.createAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                KRUZI_ANT,
                KruziAntEntity.createKruziAntAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                WARPERDUN,
                WarperdunEntity.createAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                OGANESON,
                OganesonEntity.createAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                NYANKO,
                NyankoEntity.createAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                BATTLE_NYANKO,
                BattleNyankoEntity.createAttributes()
        );

        // =====================================================
        // Natural Spawning
        // Warperdun is intentionally excluded.
        // =====================================================

        // Scorcher: Crimson Forest
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.CRIMSON_FOREST),
                SpawnGroup.MONSTER,
                SCORCHER,
                20,
                1,
                5
        );

        // Nyanko: Plains
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.PLAINS),
                SpawnGroup.MONSTER,
                NYANKO,
                11,
                1,
                5
        );

        // Oganeson: Warped Forest and Crimson Forest
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(
                        BiomeKeys.WARPED_FOREST,
                        BiomeKeys.CRIMSON_FOREST
                ),
                SpawnGroup.MONSTER,
                OGANESON,
                20,
                1,
                6
        );
    }
}
