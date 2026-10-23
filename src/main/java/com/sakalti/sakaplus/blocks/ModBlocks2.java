package com.sakalti.sakaplus.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ModBlocks2 {

    private ModBlocks2() {}

    /*
     * 共通登録関数
     * ブロックとBlockItemを同じIDで登録する。
     */
    private static Block registerBlock(String name, Block block) {
        Identifier id = new Identifier("sakaplus", name);

        Registry.register(Registry.BLOCK, id, block);
        Registry.register(
            Registry.ITEM,
            id,
            new BlockItem(block, new FabricItemSettings())
        );

        return block;
    }

    private static AbstractBlock.Settings stone(float hardness, float resistance) {
        return AbstractBlock.Settings.of(Material.STONE)
            .strength(hardness, resistance);
    }

    private static AbstractBlock.Settings wood(float hardness, float resistance) {
        return AbstractBlock.Settings.of(Material.WOOD)
            .strength(hardness, resistance);
    }

    private static Block stairs(String name, Block base, float hardness, float resistance) {
        return registerBlock(
            name,
            new StairsBlock(
                base.getDefaultState(),
                AbstractBlock.Settings.copy(base).strength(hardness, resistance)
            )
        );
    }

    private static Block slab(String name, float hardness, float resistance) {
        return registerBlock(
            name,
            new SlabBlock(stone(hardness, resistance))
        );
    }

    private static Block wall(String name, float hardness, float resistance) {
        return registerBlock(
            name,
            new WallBlock(stone(hardness, resistance))
        );
    }

    // --------------------------------------------------
    // 既存ブロック
    // --------------------------------------------------

    public static final Block IRON_SCAFFOLD =
        registerBlock("iron_scaffold", new IronScaffoldBlock());

    // ★修正箇所: 二重登録を避けるため、ブロック単体のみをレジストリに登録します
    public static final Block BLOOMSQUER =
        Registry.register(
            Registry.BLOCK,
            new Identifier("sakaplus", "bloomsquer"),
            new Block(wood(1.8f, 3.0f))
        );

    public static final Block KRUZI_STONE =
        registerBlock("kruzi_stone",
            new Block(stone(2.0f, 6.0f)));

    public static final Block KRUZI_BRICKS =
        registerBlock("kruzi_bricks",
            new Block(stone(2.0f, 6.0f)));

    // --------------------------------------------------
    // Kruzi Grass
    // --------------------------------------------------

    public static final Block KRUZI_GRASS =
        registerBlock("kruzi_grass",
            new Block(
                AbstractBlock.Settings.of(Material.SOIL)
                    .strength(0.6f)
            ));

    public static final Block KRUZI_GRASS_STAIRS =
        registerBlock("kruzi_grass_stairs",
            new StairsBlock(
                KRUZI_GRASS.getDefaultState(),
                AbstractBlock.Settings.copy(KRUZI_GRASS)
            ));

    public static final Block KRUZI_GRASS_SLAB =
        registerBlock("kruzi_grass_slab",
            new SlabBlock(
                AbstractBlock.Settings.copy(KRUZI_GRASS)
            ));

    public static final Block KRUZI_GRASS_WALL =
        registerBlock("kruzi_grass_wall",
            new WallBlock(
                AbstractBlock.Settings.copy(KRUZI_GRASS)
            ));

    // --------------------------------------------------
    // Kruzi Bricks
    // --------------------------------------------------

    public static final Block KRUZI_BRICK_STAIRS =
        stairs("kruzi_brick_stairs", KRUZI_BRICKS, 2.0f, 6.0f);

    public static final Block KRUZI_BRICK_SLAB =
        slab("kruzi_brick_slab", 2.0f, 6.0f);

    public static final Block KRUZI_BRICK_WALL =
        wall("kruzi_brick_wall", 2.0f, 6.0f);

    public static final Block CHISELED_KRUZI_BRICKS =
        registerBlock("chiseled_kruzi_bricks",
            new PillarBlock(stone(2.0f, 6.0f)));

    // --------------------------------------------------
    // Greeny wood
    // --------------------------------------------------

    public static final Block GREENY_LOG =
        registerBlock("greeny_log",
            new PillarBlock(wood(2.5f, 4.0f)));

    public static final Block GREENY_WOOD =
        registerBlock("greeny_wood",
            new PillarBlock(wood(2.5f, 4.0f)));

    public static final Block STRIPPED_GREENY_LOG =
        registerBlock("stripped_greeny_log",
            new PillarBlock(wood(2.5f, 4.0f)));

    public static final Block STRIPPED_GREENY_WOOD =
        registerBlock("stripped_greeny_wood",
            new PillarBlock(wood(2.5f, 4.0f)));

    // Greeny Leaves
    public static final Block GREENY_LEAVES =
        registerBlock("greeny_leaves",
            new LeavesBlock(
                AbstractBlock.Settings.of(Material.LEAVES)
                    .strength(0.2f)
                    .ticksRandomly()
                    .nonOpaque()
                    .sounds(net.minecraft.sound.BlockSoundGroup.GRASS)
            ));

    public static final Block GREENY_PLANKS =
        registerBlock("greeny_planks",
            new Block(wood(2.5f, 4.0f)));

    public static final Block GREENY_STAIRS =
        registerBlock("greeny_stairs",
            new StairsBlock(
                GREENY_PLANKS.getDefaultState(),
                AbstractBlock.Settings.copy(GREENY_PLANKS)
            ));

    public static final Block GREENY_SLAB =
        registerBlock("greeny_slab",
            new SlabBlock(
                AbstractBlock.Settings.copy(GREENY_PLANKS)
            ));

    public static final Block GREENY_FENCE =
        registerBlock("greeny_fence",
            new FenceBlock(
                AbstractBlock.Settings.copy(GREENY_PLANKS)
            ));

    public static final Block GREENY_FENCE_GATE =
        registerBlock("greeny_fence_gate",
            new FenceGateBlock(
                AbstractBlock.Settings.copy(GREENY_PLANKS)
            ));

    public static final Block GREENY_WALL =
        registerBlock("greeny_wall",
            new WallBlock(
                AbstractBlock.Settings.copy(GREENY_PLANKS)
            ));

    // --------------------------------------------------
    // 構造物専用ブロック
    // --------------------------------------------------

    public static final Block KRUZI_STRUCTURE_BLOCK =
        registerBlock("kruzi_structure_block",
            new Block(stone(3.0f, 9.0f)));

    // --------------------------------------------------
    // Ant Hive：非常に柔らかいブロック
    // --------------------------------------------------

    public static final Block ANT_HIVE =
        registerBlock("ant_hive",
            new Block(
                AbstractBlock.Settings.of(Material.WOOD)
                    .strength(0.2f, 0.2f)
                    .sounds(net.minecraft.sound.BlockSoundGroup.WOOD)
            ));

    public static void registerBlocks() {
        // クラス初期化時に各ブロックとアイテムは登録済み。
        // 二重登録を防ぐため、ここでは再登録しない。
    }
}
