package com.sakalti.sakacountry.item;

import com.sakalti.sakacountry.Sakacountry;
import com.sakalti.sakacountry.item.weapon.CutlassItem;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public final class ModWeapons {

    private ModWeapons() {
    }

    // =====================================
    // Wooden Cutlass
    // =====================================

    public static final Item WOODEN_CUTLASS = register(
            "wooden_cutlass",
            ToolMaterial.WOOD,
            3.5F,
            -2.2F
    );

    // =====================================
    // Golden Cutlass
    // =====================================

    public static final Item GOLDEN_CUTLASS = register(
            "golden_cutlass",
            ToolMaterial.GOLD,
            3.5F,
            -2.1F
    );

    // =====================================
    // Stone Cutlass
    // =====================================

    public static final Item STONE_CUTLASS = register(
            "stone_cutlass",
            ToolMaterial.STONE,
            4.25F,
            -2.2F
    );

    // =====================================
    // Copper Cutlass
    // =====================================

    public static final Item COPPER_CUTLASS = register(
            "copper_cutlass",
            ToolMaterial.COPPER,
            4.25F,
            -2.15F
    );

    // =====================================
    // Iron Cutlass
    // =====================================

    public static final Item IRON_CUTLASS = register(
            "iron_cutlass",
            ToolMaterial.IRON,
            5.0F,
            -2.1F
    );

    // =====================================
    // Diamond Cutlass
    // =====================================

    public static final Item DIAMOND_CUTLASS = register(
            "diamond_cutlass",
            ToolMaterial.DIAMOND,
            5.75F,
            -2.05F
    );

    // =====================================
    // Netherite Cutlass
    // =====================================

    public static final Item NETHERITE_CUTLASS = register(
            "netherite_cutlass",
            ToolMaterial.NETHERITE,
            1.5F,
            -2.05F
    );

    // =====================================
    // Registry
    // =====================================

    private static Item register(
            String name,
            ToolMaterial material,
            float damage,
            float attackSpeed
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(
                Sakacountry.MOD_ID,
                name
        );

        ResourceKey<Item> itemKey = ResourceKey.create(
                Registries.ITEM,
                id
        );

        Item item = new CutlassItem(
                material,
                damage,
                attackSpeed,
                new Item.Properties().setId(itemKey)
        );

        return Registry.register(
                BuiltInRegistries.ITEM,
                itemKey,
                item
        );
    }

    // =====================================
    // Initialization
    // =====================================

    public static void initialize() {
        // Static fields are initialized when this method is called.
    }
}
