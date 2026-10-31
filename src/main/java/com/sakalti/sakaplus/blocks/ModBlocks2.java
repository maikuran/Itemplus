package com.sakalti.sakaplus.block;

import com.sakalti.sakaplus.world.GreenySaplingGenerator;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ModBlocks2 {

    private ModBlocks2() {}

    // 共通登録
    private static Block reg(String name, Block block) {
        Identifier id = new Identifier("sakaplus", name);

        Registry.register(Registry.BLOCK, id, block);
        Registry.register(
                Registry.ITEM,
                id,
                new BlockItem(block, new FabricItemSettings())
        );

        return block;
    }

    // WoodenButtonBlockのprotectedコンストラクターを強制アクセス
    private static Block createWoodenButton(AbstractBlock.Settings settings) {
        try {
            java.lang.reflect.Constructor<WoodenButtonBlock> constructor =
                    WoodenButtonBlock.class.getDeclaredConstructor(
                            AbstractBlock.Settings.class
                    );

            constructor.setAccessible(true);

            return constructor.newInstance(settings);

        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static AbstractBlock.Settings stone(float hardness, float resistance) {
        return AbstractBlock.Settings.of(Material.STONE)
                .strength(hardness, resistance);
    }

    private static AbstractBlock.Settings wood(float hardness, float resistance) {
        return AbstractBlock.Settings.of(Material.WOOD)
                .strength(hardness, resistance);
    }

    private static AbstractBlock.Settings metal(float hardness, float resistance) {
        return AbstractBlock.Settings.of(Material.METAL)
                .strength(hardness, resistance)
                .requiresTool()
                .sounds(BlockSoundGroup.METAL);
    }

    private static Block stairs(String name, Block base, float hardness, float resistance) {
        return reg(name, new StairsBlock(
                base.getDefaultState(),
                AbstractBlock.Settings.copy(base).strength(hardness, resistance)
        ));
    }

    private static Block slab(String name, float hardness, float resistance) {
        return reg(name, new SlabBlock(stone(hardness, resistance)));
    }

    private static Block wall(String name, float hardness, float resistance) {
        return reg(name, new WallBlock(stone(hardness, resistance)));
    }

    // 基本ブロック
    public static final Block IRON_SCAFFOLD =
            reg("iron_scaffold", new IronScaffoldBlock());

    public static final Block BLOOMSQUER =
            reg("bloomsquer", new Block(wood(1.8f, 3)));

    public static final Block KRUZI_STONE =
            reg("kruzi_stone", new Block(stone(2, 6)));

    public static final Block PYRONIUM_ORE =
            reg("pyronium_ore", new Block(stone(2, 6)));

    public static final Block KRUZI_BRICKS =
            reg("kruzi_bricks", new Block(stone(2, 6)));

    // Kruzi Grass
    public static final Block KRUZI_GRASS =
            reg("kruzi_grass", new Block(
                    AbstractBlock.Settings.of(Material.SOIL)
                            .strength(0.6f)
            ));

    public static final Block KRUZI_GRASS_STAIRS =
            reg("kruzi_grass_stairs", new StairsBlock(
                    KRUZI_GRASS.getDefaultState(),
                    AbstractBlock.Settings.copy(KRUZI_GRASS)
            ));

    public static final Block KRUZI_GRASS_SLAB =
            reg("kruzi_grass_slab",
                    new SlabBlock(AbstractBlock.Settings.copy(KRUZI_GRASS)));

    public static final Block KRUZI_GRASS_WALL =
            reg("kruzi_grass_wall",
                    new WallBlock(AbstractBlock.Settings.copy(KRUZI_GRASS)));

    // Kruzi Bricks
    public static final Block KRUZI_BRICK_STAIRS =
            stairs("kruzi_brick_stairs", KRUZI_BRICKS, 2, 6);

    public static final Block KRUZI_BRICK_SLAB =
            slab("kruzi_brick_slab", 2, 6);

    public static final Block KRUZI_BRICK_WALL =
            wall("kruzi_brick_wall", 2, 6);

    public static final Block CHISELED_KRUZI_BRICKS =
            reg("chiseled_kruzi_bricks", new PillarBlock(stone(2, 6)));

    // Greeny Wood
    public static final Block GREENY_LOG =
            reg("greeny_log", new PillarBlock(wood(2.5f, 4)));

    public static final Block GREENY_WOOD =
            reg("greeny_wood", new PillarBlock(wood(2.5f, 4)));

    public static final Block STRIPPED_GREENY_LOG =
            reg("stripped_greeny_log", new PillarBlock(wood(2.5f, 4)));

    public static final Block STRIPPED_GREENY_WOOD =
            reg("stripped_greeny_wood", new PillarBlock(wood(2.5f, 4)));

    public static final Block GREENY_PLANKS =
            reg("greeny_planks", new Block(wood(2.5f, 4)));

    public static final Block GREENY_STAIRS =
            reg("greeny_stairs", new StairsBlock(
                    GREENY_PLANKS.getDefaultState(),
                    AbstractBlock.Settings.copy(GREENY_PLANKS)
            ));

    public static final Block GREENY_SLAB =
            reg("greeny_slab",
                    new SlabBlock(AbstractBlock.Settings.copy(GREENY_PLANKS)));

    public static final Block GREENY_FENCE =
            reg("greeny_fence",
                    new FenceBlock(AbstractBlock.Settings.copy(GREENY_PLANKS)));

    public static final Block GREENY_FENCE_GATE =
            reg("greeny_fence_gate",
                    new FenceGateBlock(AbstractBlock.Settings.copy(GREENY_PLANKS)));

    public static final Block GREENY_WALL =
            reg("greeny_wall",
                    new WallBlock(AbstractBlock.Settings.copy(GREENY_PLANKS)));

    // Greeny Leaves
    public static final Block GREENY_LEAVES =
            reg("greeny_leaves", new LeavesBlock(
                    AbstractBlock.Settings.of(Material.LEAVES)
                            .strength(0.2f)
                            .ticksRandomly()
                            .nonOpaque()
                            .sounds(BlockSoundGroup.GRASS)
            ));

    // Greeny Sapling
    public static final Block GREENY_SAPLING =
            reg("greeny_sapling", new SaplingBlock(
                    new GreenySaplingGenerator(),
                    AbstractBlock.Settings.of(Material.PLANT)
                            .noCollision()
                            .breakInstantly()
                            .sounds(BlockSoundGroup.GRASS)
            ));

    // Greeny Door
    public static final Block GREENY_DOOR =
            reg("greeny_door", new DoorBlock(
                    AbstractBlock.Settings.of(Material.WOOD)
                            .strength(3.0f)
                            .nonOpaque()
                            .sounds(BlockSoundGroup.WOOD)
            ));

    // Greeny Trapdoor
    public static final Block GREENY_TRAPDOOR =
            reg("greeny_trapdoor", new TrapdoorBlock(
                    AbstractBlock.Settings.of(Material.WOOD)
                            .strength(3.0f)
                            .nonOpaque()
                            .sounds(BlockSoundGroup.WOOD)
            ));

    // Greeny Pressure Plate
    public static final Block GREENY_PRESSURE_PLATE =
            reg("greeny_pressure_plate", new PressurePlateBlock(
                    PressurePlateBlock.ActivationRule.EVERYTHING,
                    AbstractBlock.Settings.of(Material.WOOD)
                            .noCollision()
                            .strength(0.5f)
                            .sounds(BlockSoundGroup.WOOD)
            ));

    // Greeny Wooden Button
    public static final Block GREENY_BUTTON =
            reg("greeny_button", createWoodenButton(
                    AbstractBlock.Settings.of(Material.WOOD)
                            .noCollision()
                            .strength(0.5f)
                            .sounds(BlockSoundGroup.WOOD)
            ));

    // 構造物専用
    public static final Block KRUZI_STRUCTURE_BLOCK =
            reg("kruzi_structure_block", new Block(stone(3, 9)));

    // Ant Hive
    public static final Block ANT_HIVE =
            reg("ant_hive", new Block(
                    AbstractBlock.Settings.of(Material.WOOD)
                            .strength(0.2f, 0.2f)
                            .sounds(BlockSoundGroup.WOOD)
            ));

    // Pyronium
    public static final Block PYRONIUM_BLOCK =
            reg("pyronium_block", new Block(metal(17, 14)));

    public static void registerBlocks() {
        // クラス初期化時に登録済み
    }
}
