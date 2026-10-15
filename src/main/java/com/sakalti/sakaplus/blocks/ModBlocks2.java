
package com.sakalti.sakaplus.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks2 {

    public static final Block IRON_SCAFFOLD = Registry.register(
        Registry.BLOCK,
        new Identifier("sakaplus", "iron_scaffold"),
        new IronScaffoldBlock()
    );

    public static final Block BLOOMSQUER = Registry.register(
        Registry.BLOCK,
        new Identifier("sakaplus", "bloomsquer"),
        new Block(
            AbstractBlock.Settings.of(Material.WOOD)
                .strength(1.8f)
        )
    );

    public static final Block KRUZI_STONE = Registry.register(
        Registry.BLOCK,
        new Identifier("sakaplus", "kruzi_stone"),
        new Block(
            AbstractBlock.Settings.of(Material.STONE)
                .strength(2.0f, 6.0f)
                .requiresTool()
        )
    );

    public static void registerBlocks() {
        Registry.register(
            Registry.ITEM,
            new Identifier("sakaplus", "iron_scaffold"),
            new BlockItem(IRON_SCAFFOLD, new FabricItemSettings())
        );

        Registry.register(
            Registry.ITEM,
            new Identifier("sakaplus", "kruzi_stone"),
            new BlockItem(KRUZI_STONE, new FabricItemSettings())
        );
    }
}
