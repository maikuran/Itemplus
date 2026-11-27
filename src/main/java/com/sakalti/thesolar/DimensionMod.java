package com.sakalti.thesolar;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class DimensionMod implements ModInitializer {
    public static final String MOD_ID = "thesolar";
    public static final SoundEvent SELENE_TELEGRAPH_SOUND = Registry.register(BuiltInRegistries.SOUND_EVENT, id("selene_telegraph"), SoundEvent.createVariableRangeEvent(id("selene_telegraph")));
    public static final SoundEvent ARES_TELEGRAPH_SOUND = Registry.register(BuiltInRegistries.SOUND_EVENT, id("ares_telegraph"), SoundEvent.createVariableRangeEvent(id("ares_telegraph")));

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static final ResourceKey<Level> MOON_DIMENSION = ResourceKey.create(Registries.DIMENSION, id("moon"));
    public static final ResourceKey<Level> MARS_DIMENSION = ResourceKey.create(Registries.DIMENSION, id("mars"));

    private static ResourceKey<Item> itemKey(String path) {
        return ResourceKey.create(Registries.ITEM, id(path));
    }

    private static ResourceKey<Block> blockKey(String path) {
        return ResourceKey.create(Registries.BLOCK, id(path));
    }

    public static final Block MOONSTONE = registerBlock("moonstone", 2.2f, 6.0f, SoundType.STONE);
    public static final Block MOON_DUST = registerBlock("moon_dust", 0.5f, 0.5f, SoundType.SAND);
    public static final Block SELENITE_ORE = registerOreBlock("selenite_ore", 3.0f, 7.5f, SoundType.STONE);
    public static final Block LUNARIUM_ORE = registerOreBlock("lunarium_ore", 4.0f, 10.0f, SoundType.STONE);
    public static final Block ANCIENT_LUNAR_BRICKS = registerBlock("ancient_lunar_bricks", 3.0f, 12.0f, SoundType.DEEPSLATE);
    public static final Block MOON_CRYSTAL = registerBlock("moon_crystal", 1.5f, 2.0f, SoundType.AMETHYST);
    public static final Block MARTIAN_ROCK = registerBlock("martian_rock", 2.0f, 5.0f, SoundType.STONE);
    public static final Block MARTIAN_SAND = registerBlock("martian_sand", 0.5f, 0.5f, SoundType.SAND);
    public static final Block FERRITE_ORE = registerOreBlock("ferrite_ore", 3.0f, 7.5f, SoundType.STONE);
    public static final Block BLOOD_CRYSTAL_ORE = registerOreBlock("blood_crystal_ore", 4.0f, 10.0f, SoundType.STONE);
    public static final Block ARES_ORE = registerOreBlock("ares_ore", 5.0f, 12.5f, SoundType.STONE);
    public static final Block ANCIENT_MARTIAN_BRICKS = registerBlock("ancient_martian_bricks", 3.5f, 12.0f, SoundType.DEEPSLATE);
    public static final Block MARTIAN_CORE = registerBlock("martian_core", 4.0f, 10.0f, SoundType.METAL);
    public static final Block MOON_GATEWAY = registerPortalBlock("moon_gateway", "thesolar:moon", 112);
    public static final Block MARS_GATEWAY = registerPortalBlock("mars_gateway", "thesolar:mars", 112);

    public static final Item SELENITE = registerItem("selenite");
    public static final Item LUNARIUM_INGOT = registerItem("lunarium_ingot");
    public static final Item STAR_DUST = registerItem("star_dust");
    public static final Item FERRITE_INGOT = registerItem("ferrite_ingot");
    public static final Item BLOOD_CRYSTAL = registerItem("blood_crystal");
    public static final Item ARES_INGOT = registerItem("ares_ingot");
    public static final Item ANCIENT_MARTIAN_GEAR = registerItem("ancient_martian_gear");
    public static final Item MOONLIGHT_BLADE = registerItem("moonlight_blade", new Item.Properties().durability(900));
    public static final Item ARES_GREATSWORD = registerItem("ares_greatsword", new Item.Properties().durability(1500));
    public static final Item LUNAR_SEAL = registerItem("lunar_seal");
    public static final Item MARTIAN_SIGIL = registerItem("martian_sigil");

    public static Block LUNAR_BOSS_ALTAR;
    public static Block MARTIAN_BOSS_ALTAR;

    static Block registerOreBlock(String name, float hardness, float resistance, SoundType sound) {
        ResourceKey<Block> key = blockKey(name);
        Block block = new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(hardness, resistance).sound(sound).requiresCorrectToolForDrops().setId(key));
        Registry.register(BuiltInRegistries.BLOCK, key, block);
        ResourceKey<Item> itemId = itemKey(name);
        BlockItem item = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(itemId));
        Registry.register(BuiltInRegistries.ITEM, itemId, item);
        return block;
    }
    static Block registerBlock(String name, float hardness, float resistance, SoundType sound) {
        ResourceKey<Block> key = blockKey(name);
        Block block = new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                .strength(hardness, resistance).sound(sound).setId(key));
        Registry.register(BuiltInRegistries.BLOCK, key, block);
        ResourceKey<Item> itemId = itemKey(name);
        BlockItem item = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(itemId));
        Registry.register(BuiltInRegistries.ITEM, itemId, item);
        return block;
    }

    private static Block registerBossAltarBlock(String name, EntityType<? extends Mob> boss,
                                                Item catalyst, ResourceKey<Level> dimension) {
        ResourceKey<Block> key = blockKey(name);
        Block block = new BossAltarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)
                .strength(5.0f, 12.0f).sound(SoundType.AMETHYST).setId(key), boss, catalyst, dimension);
        Registry.register(BuiltInRegistries.BLOCK, key, block);
        ResourceKey<Item> itemId = itemKey(name);
        BlockItem item = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(itemId));
        Registry.register(BuiltInRegistries.ITEM, itemId, item);
        return block;
    }

    private static Block registerPortalBlock(String name, String target, int y) {
        ResourceKey<Block> key = blockKey(name);
        Block block = new SolarPortalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)
                .strength(2.0f, 6.0f).noCollission().lightLevel(state -> 12).setId(key), target, y);
        Registry.register(BuiltInRegistries.BLOCK, key, block);
        ResourceKey<Item> itemId = itemKey(name);
        Registry.register(BuiltInRegistries.ITEM, itemId, new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(itemId)));
        return block;
    }

    static Item itemById(String path) { return BuiltInRegistries.ITEM.get(id(path)); }
    static Item registerItem(String name) {
        return registerItem(name, new Item.Properties());
    }

    static Item registerItem(String name, Item.Properties properties) {
        ResourceKey<Item> key = itemKey(name);
        Item item = new Item(properties.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    @Override
    public void onInitialize() {
        com.sakalti.thesolar.entity.ModEntities.initialize();
        SolarMaterials.registerAll();
        LunarGearAbilities.register();
        SolarOreWorldgen.register();
        registerSkyEffects();
        LUNAR_BOSS_ALTAR = registerBossAltarBlock("lunar_boss_altar",
                com.sakalti.thesolar.entity.ModEntities.SELENE, STAR_DUST, MOON_DIMENSION);
        MARTIAN_BOSS_ALTAR = registerBossAltarBlock("martian_boss_altar",
                com.sakalti.thesolar.entity.ModEntities.ARES, ANCIENT_MARTIAN_GEAR, MARS_DIMENSION);

        ItemGroupEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.accept(SELENITE);
            entries.accept(LUNARIUM_INGOT);
            entries.accept(STAR_DUST);
            entries.accept(FERRITE_INGOT);
            entries.accept(BLOOD_CRYSTAL);
            entries.accept(ARES_INGOT);
            entries.accept(ANCIENT_MARTIAN_GEAR);
            entries.accept(MOONLIGHT_BLADE);
            entries.accept(ARES_GREATSWORD);
            entries.accept(LUNAR_SEAL);
            entries.accept(MARTIAN_SIGIL);
            SolarMaterials.addIngredients(entries);
        });
        ItemGroupEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(SolarMaterials::addTools);
        ItemGroupEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(SolarMaterials::addCombat);
        ItemGroupEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.accept(MOONSTONE);
            entries.accept(MOON_DUST);
            entries.accept(SELENITE_ORE);
            entries.accept(LUNARIUM_ORE);
            entries.accept(ANCIENT_LUNAR_BRICKS);
            entries.accept(MOON_CRYSTAL);
            entries.accept(LUNAR_BOSS_ALTAR);
            entries.accept(MARTIAN_ROCK);
            entries.accept(MARTIAN_SAND);
            entries.accept(FERRITE_ORE);
            entries.accept(BLOOD_CRYSTAL_ORE);
            entries.accept(ARES_ORE);
            entries.accept(ANCIENT_MARTIAN_BRICKS);
            entries.accept(MARTIAN_CORE);
            entries.accept(MARTIAN_BOSS_ALTAR);
            entries.accept(MOON_GATEWAY);
            entries.accept(MARS_GATEWAY);
            SolarMaterials.addOres(entries);
        });
    }
    private static void registerSkyEffects() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> updateSkyEffects(handler.player));
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> updateSkyEffects(player));
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> updateSkyEffects(newPlayer));
    }

    private static void updateSkyEffects(ServerPlayer player) {
        if (player.getServer() == null) return;
        var source = player.createCommandSourceStack().withPermission(4).withSuppressedOutput();
        var commands = player.getServer().getCommands();
        commands.performPrefixedCommand(source, "posteffect remove @s thesolar:moon_sky");
        commands.performPrefixedCommand(source, "posteffect remove @s thesolar:mars_sky");
        if (player.level().dimension().equals(MOON_DIMENSION)) {
            commands.performPrefixedCommand(source, "posteffect add @s thesolar:moon_sky");
        } else if (player.level().dimension().equals(MARS_DIMENSION)) {
            commands.performPrefixedCommand(source, "posteffect add @s thesolar:mars_sky");
        }
    }
}
