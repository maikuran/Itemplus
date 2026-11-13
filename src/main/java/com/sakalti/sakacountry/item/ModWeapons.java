
package com.sakalti.sakacountry.item;

import com.sakalti.sakacountry.Sakacountry;
import com.sakalti.sakacountry.item.weapon.CutlassItem;
import com.sakalti.sakacountry.item.weapon.HalberdItem;

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
            "wooden_cutlass", ToolMaterial.WOOD, 3.5F, -2.2F
    );

    // =====================================
    // Golden Cutlass
    // =====================================

    public static final Item GOLDEN_CUTLASS = register(
            "golden_cutlass", ToolMaterial.GOLD, 3.5F, -2.1F
    );

    // =====================================
    // Stone Cutlass
    // =====================================

    public static final Item STONE_CUTLASS = register(
            "stone_cutlass", ToolMaterial.STONE, 4.25F, -2.2F
    );

    // =====================================
    // Copper Cutlass
    // =====================================

    public static final Item COPPER_CUTLASS = register(
            "copper_cutlass", ToolMaterial.COPPER, 4.25F, -2.15F
    );

    // =====================================
    // Iron Cutlass
    // =====================================

    public static final Item IRON_CUTLASS = register(
            "iron_cutlass", ToolMaterial.IRON, 5.0F, -2.1F
    );

    // =====================================
    // Diamond Cutlass
    // =====================================

    public static final Item DIAMOND_CUTLASS = register(
            "diamond_cutlass", ToolMaterial.DIAMOND, 5.75F, -2.05F
    );

    // =====================================
    // Netherite Cutlass
    // =====================================

    public static final Item NETHERITE_CUTLASS = register(
            "netherite_cutlass", ToolMaterial.NETHERITE, 6.5F, -2.05F
    );


    // =====================================
    // Wooden Halberd
    // =====================================

    public static final Item WOODEN_HALBERD = registerHalberd(
            "wooden_halberd",
            ToolMaterial.WOOD,
            5.0F,
            -2.8F
    );

    // =====================================
    // Golden Halberd
    // =====================================

    public static final Item GOLDEN_HALBERD = registerHalberd(
            "golden_halberd",
            ToolMaterial.GOLD,
            5.0F,
            -2.75F
    );

    // =====================================
    // Stone Halberd
    // =====================================

    public static final Item STONE_HALBERD = registerHalberd(
            "stone_halberd",
            ToolMaterial.STONE,
            5.75F,
            -2.85F
    );

    // =====================================
    // Copper Halberd
    // =====================================

    public static final Item COPPER_HALBERD = registerHalberd(
            "copper_halberd",
            ToolMaterial.COPPER,
            6.0F,
            -2.8F
    );

    // =====================================
    // Iron Halberd
    // =====================================

    public static final Item IRON_HALBERD = registerHalberd(
            "iron_halberd",
            ToolMaterial.IRON,
            6.5F,
            -2.75F
    );

    // =====================================
    // Diamond Halberd
    // =====================================

    public static final Item DIAMOND_HALBERD = registerHalberd(
            "diamond_halberd",
            ToolMaterial.DIAMOND,
            7.25F,
            -2.7F
    );

    // =====================================
    // Netherite Halberd
    // =====================================

    public static final Item NETHERITE_HALBERD = registerHalberd(
            "netherite_halberd",
            ToolMaterial.NETHERITE,
            8.0F,
            -2.65F
    );


    // =====================================
    // Cutlass Registration
    // =====================================

    private static Item register(
            String name,
            ToolMaterial material,
            float targetDamage,
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

        float baseDamage =
                targetDamage - material.attackDamageBonus();

        Item.Properties properties = new Item.Properties()
                .setId(itemKey);

        Item item = new CutlassItem(
                material,
                baseDamage,
                attackSpeed,
                properties
        );

        return Registry.register(
                BuiltInRegistries.ITEM,
                itemKey,
                item
        );
    }


    // =====================================
    // Halberd Registration
    // =====================================

    private static Item registerHalberd(
            String name,
            ToolMaterial material,
            float targetDamage,
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

        float baseDamage =
                targetDamage - material.attackDamageBonus();

        Item.Properties properties = new Item.Properties()
                .setId(itemKey);

        Item item = new HalberdItem(
                material,
                baseDamage,
                attackSpeed,
                properties
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
        // Trigger static initialization.
    }
}
