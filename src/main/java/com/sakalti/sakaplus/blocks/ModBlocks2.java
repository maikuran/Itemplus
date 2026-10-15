
package com.sakalti.sakaplus.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.AbstractBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ModBlocks2 {

    // ブロックの生成と登録を同時に行う
    public static final Block IRON_SCAFFOLD = registerBlock(
        "iron_scaffold",
        new IronScaffoldBlock()
    );

    public static final Block BLOOMSQUER = registerBlock(
        "bloomsquer",
        new Block(
            AbstractBlock.Settings
                .of(Material.WOOD)
                .strength(1.8f)
        )
    );

    public static final Block KRUZI_STONE = registerBlock(
        "kruzi_stone",
        new Block(
            AbstractBlock.Settings
                .of(Material.STONE)
                .strength(2.0f, 6.0f)
                .requiresTool()
        )
    );

    private ModBlocks2() {
    }

    private static Block registerBlock(String name, Block block) {
        return Registry.register(
            Registry.BLOCK,
            new Identifier("sakaplus", name),
            block
        );
    }

    private static BlockItem registerBlockItem(String name, Block block) {
        return Registry.register(
            Registry.ITEM,
            new Identifier("sakaplus", name),
            new BlockItem(block, new FabricItemSettings())
        );
    }

    static {
        // ブロックの登録完了後に対応するアイテムを登録する
        registerBlockItem("iron_scaffold", IRON_SCAFFOLD);
        registerBlockItem("kruzi_stone", KRUZI_STONE);
    }

    /**
     * 既存の初期化コードとの互換性を維持する。
     * ブロック本体と対応アイテムはクラス初期化時に登録済み。
     */
    public static void registerBlocks() {
        // 登録済みのブロックを重複登録しない。
    }
}
