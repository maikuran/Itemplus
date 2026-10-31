
package com.sakalti.sakaplus.block;

import com.sakalti.sakaplus.world.GreenySaplingGenerator;
import com.sakalti.sakaplus.registry.ModFoods;
import com.sakalti.sakaplus.registry.ModTabs;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import java.lang.reflect.Constructor;

public final class ModBlocks2 {
    private ModBlocks2() {}

    private static Block reg(String n,Block b) {
        Identifier id=new Identifier("sakaplus",n);
        Registry.register(Registry.BLOCK,id,b);
        Registry.register(Registry.ITEM,id,new BlockItem(b,new FabricItemSettings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB)));
        return b;
    }

    private static Block regFood(String n,Block b) {
        Identifier id=new Identifier("sakaplus",n);
        Registry.register(Registry.BLOCK,id,b);
        Registry.register(Registry.ITEM,id,new BlockItem(b,new FabricItemSettings().group(ModTabs.STARTER_AND_KRUZIVILIM_TAB).food(ModFoods.BLOOMSQUER).maxCount(16)));
        return b;
    }

    private static Block button(AbstractBlock.Settings s) {
        try {
            Constructor<WoodenButtonBlock> c=WoodenButtonBlock.class.getDeclaredConstructor(AbstractBlock.Settings.class);
            c.setAccessible(true);
            return c.newInstance(s);
        } catch(Exception e) {
            throw new RuntimeException("Greeny Button creation failed",e);
        }
    }

    private static AbstractBlock.Settings stone(float h,float r) {
        return AbstractBlock.Settings.of(Material.STONE).strength(h,r);
    }

    private static AbstractBlock.Settings wood(float h,float r) {
        return AbstractBlock.Settings.of(Material.WOOD).strength(h,r).sounds(BlockSoundGroup.WOOD);
    }

    private static Block stairs(String n,Block b) {
        return reg(n,new StairsBlock(b.getDefaultState(),AbstractBlock.Settings.copy(b)));
    }

    private static Block slab(String n,AbstractBlock.Settings s) {
        return reg(n,new SlabBlock(s));
    }

    private static Block wall(String n,AbstractBlock.Settings s) {
        return reg(n,new WallBlock(s));
    }

    // Basic Blocks
    public static final Block IRON_SCAFFOLD=reg("iron_scaffold",new IronScaffoldBlock());
    public static final Block BLOOMSQUER=regFood("bloomsquer",new Block(wood(1.8f,3)));
    public static final Block KRUZI_STONE=reg("kruzi_stone",new Block(stone(2,6)));
    public static final Block PYRONIUM_ORE=reg("pyronium_ore",new Block(stone(2,6)));
    public static final Block KRUZI_BRICKS=reg("kruzi_bricks",new Block(stone(2,6)));

    // Dandelite
    public static final Block DANDELITE_ORE=reg("dandelite_ore",new Block(stone(3,6).requiresTool()));
    public static final Block DANDELITE_BLOCK=reg("dandelite_block",new Block(AbstractBlock.Settings.of(Material.METAL).strength(5,6).requiresTool().sounds(BlockSoundGroup.METAL)));

    // Rosite
    public static final Block ROSITE_ORE=reg("rosite_ore",new Block(stone(4,6).requiresTool()));
    public static final Block ROSITE_BLOCK=reg("rosite_block",new Block(AbstractBlock.Settings.of(Material.METAL).strength(6,8).requiresTool().sounds(BlockSoundGroup.METAL)));

    // Kruzi Grass
    public static final Block KRUZI_GRASS=reg("kruzi_grass",new Block(AbstractBlock.Settings.of(Material.SOIL).strength(0.6f)));
    public static final Block KRUZI_GRASS_STAIRS=stairs("kruzi_grass_stairs",KRUZI_GRASS);
    public static final Block KRUZI_GRASS_SLAB=slab("kruzi_grass_slab",AbstractBlock.Settings.copy(KRUZI_GRASS));
    public static final Block KRUZI_GRASS_WALL=wall("kruzi_grass_wall",AbstractBlock.Settings.copy(KRUZI_GRASS));

    // Kruzi Bricks
    public static final Block KRUZI_BRICK_STAIRS=reg("kruzi_brick_stairs",new StairsBlock(KRUZI_BRICKS.getDefaultState(),stone(2,6)));
    public static final Block KRUZI_BRICK_SLAB=slab("kruzi_brick_slab",stone(2,6));
    public static final Block KRUZI_BRICK_WALL=wall("kruzi_brick_wall",stone(2,6));
    public static final Block CHISELED_KRUZI_BRICKS=reg("chiseled_kruzi_bricks",new PillarBlock(stone(2,6)));

    // Greeny Wood
    public static final Block GREENY_LOG=reg("greeny_log",new PillarBlock(wood(2.5f,4)));
    public static final Block GREENY_WOOD=reg("greeny_wood",new PillarBlock(wood(2.5f,4)));
    public static final Block STRIPPED_GREENY_LOG=reg("stripped_greeny_log",new PillarBlock(wood(2.5f,4)));
    public static final Block STRIPPED_GREENY_WOOD=reg("stripped_greeny_wood",new PillarBlock(wood(2.5f,4)));
    public static final Block GREENY_PLANKS=reg("greeny_planks",new Block(wood(2.5f,4)));
    public static final Block GREENY_STAIRS=stairs("greeny_stairs",GREENY_PLANKS);
    public static final Block GREENY_SLAB=slab("greeny_slab",AbstractBlock.Settings.copy(GREENY_PLANKS));
    public static final Block GREENY_FENCE=reg("greeny_fence",new FenceBlock(AbstractBlock.Settings.copy(GREENY_PLANKS)));
    public static final Block GREENY_FENCE_GATE=reg("greeny_fence_gate",new FenceGateBlock(AbstractBlock.Settings.copy(GREENY_PLANKS)));
    public static final Block GREENY_WALL=wall("greeny_wall",AbstractBlock.Settings.copy(GREENY_PLANKS));

    public static final Block GREENY_LEAVES=reg("greeny_leaves",new LeavesBlock(AbstractBlock.Settings.of(Material.LEAVES).strength(0.2f).ticksRandomly().nonOpaque().sounds(BlockSoundGroup.GRASS)));

    // Greeny Sapling
    public static final Block GREENY_SAPLING=reg("greeny_sapling",new SaplingBlock(new GreenySaplingGenerator(),AbstractBlock.Settings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));

    // Greeny Door / Trapdoor
    public static final Block GREENY_DOOR=reg("greeny_door",new DoorBlock(AbstractBlock.Settings.of(Material.WOOD).strength(3).nonOpaque().sounds(BlockSoundGroup.WOOD)));
    public static final Block GREENY_TRAPDOOR=reg("greeny_trapdoor",new TrapdoorBlock(AbstractBlock.Settings.of(Material.WOOD).strength(3).nonOpaque().sounds(BlockSoundGroup.WOOD)));

    // Greeny Redstone
    public static final Block GREENY_PRESSURE_PLATE=reg("greeny_pressure_plate",new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING,AbstractBlock.Settings.of(Material.WOOD).noCollision().strength(0.5f).sounds(BlockSoundGroup.WOOD)));
    public static final Block GREENY_BUTTON=reg("greeny_button",button(AbstractBlock.Settings.of(Material.WOOD).noCollision().strength(0.5f).sounds(BlockSoundGroup.WOOD)));

    // Special Blocks
    public static final Block KRUZI_STRUCTURE_BLOCK=reg("kruzi_structure_block",new Block(stone(3,9)));
    public static final Block ANT_HIVE=reg("ant_hive",new Block(AbstractBlock.Settings.of(Material.WOOD).strength(0.2f,0.2f).sounds(BlockSoundGroup.WOOD)));
    public static final Block PYRONIUM_BLOCK=reg("pyronium_block",new Block(AbstractBlock.Settings.of(Material.METAL).strength(17,14).requiresTool().sounds(BlockSoundGroup.METAL)));

    public static void registerBlocks() {}
}
