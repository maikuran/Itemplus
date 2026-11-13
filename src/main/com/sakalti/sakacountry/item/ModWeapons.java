
package com.sakalti.sakacountry.item;

import com.sakalti.sakacountry.Sakacountry;
import com.sakalti.sakaplus.sakacountry.item.weapon.CutlassItem;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
            1.8F
    );

    // =====================================
    // Golden Cutlass
    // =====================================

    public static final Item GOLDEN_CUTLASS = register(
            "golden_cutlass",
            ToolMaterial.GOLD,
            3.5F,
            1.9F
    );

    // =====================================
    // Stone Cutlass
    // =====================================

    public static final Item STONE_CUTLASS = register(
            "stone_cutlass",
            ToolMaterial.STONE,
            4.25F,
            1.8F
    );

    // =====================================
    // Copper Cutlass
    // =====================================

    public static final Item COPPER_CUTLASS = register(
            "copper_cutlass",
            ToolMaterial.COPPER,
            4.25F,
            1.85F
    );

    // =====================================
    // Iron Cutlass
    // =====================================

    public static final Item IRON_CUTLASS = register(
            "iron_cutlass",
            ToolMaterial.IRON,
            5.0F,
            1.9F
    );

    // =====================================
    // Diamond Cutlass
    // =====================================

    public static final Item DIAMOND_CUTLASS = register(
            "diamond_cutlass",
            ToolMaterial.DIAMOND,
            5.75F,
            1.95F
    );

    // =====================================
    // Netherite Cutlass
    // =====================================

    public static final Item NETHERITE_CUTLASS = register(
            "netherite_cutlass",
            ToolMaterial.NETHERITE,
            6.5F,
            1.95F
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
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
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
