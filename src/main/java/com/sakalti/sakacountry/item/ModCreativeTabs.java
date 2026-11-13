
package com.sakalti.sakacountry.item;

import com.sakalti.sakacountry.Sakacountry;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTabs {

    private ModCreativeTabs() {
    }

    // =====================================
    // Sakacountry Weapons Tab
    // =====================================

    public static final ResourceKey<CreativeModeTab> SAKACOUNTRY_WEAPONS_KEY =
            ResourceKey.create(
                    Registries.CREATIVE_MODE_TAB,
                    Identifier.fromNamespaceAndPath(
                            Sakacountry.MOD_ID,
                            "sakacountry_weapons"
                    )
            );

    public static final CreativeModeTab SAKACOUNTRY_WEAPONS =
            FabricCreativeModeTab.builder()
                    .title(Component.literal("SakacountryWeapons"))

                    // Netherite Cutlass icon
                    .icon(() -> new ItemStack(
                            ModWeapons.NETHERITE_CUTLASS
                    ))

                    // Items
                    .displayItems((parameters, output) -> {

                        // =====================================
                        // Cutlasses
                        // =====================================

                        output.accept(ModWeapons.WOODEN_CUTLASS);
                        output.accept(ModWeapons.GOLDEN_CUTLASS);
                        output.accept(ModWeapons.STONE_CUTLASS);
                        output.accept(ModWeapons.COPPER_CUTLASS);
                        output.accept(ModWeapons.IRON_CUTLASS);
                        output.accept(ModWeapons.DIAMOND_CUTLASS);
                        output.accept(ModWeapons.NETHERITE_CUTLASS);

                        // =====================================
                        // Halberds
                        // =====================================

                        output.accept(ModWeapons.WOODEN_HALBERD);
                        output.accept(ModWeapons.GOLDEN_HALBERD);
                        output.accept(ModWeapons.STONE_HALBERD);
                        output.accept(ModWeapons.COPPER_HALBERD);
                        output.accept(ModWeapons.IRON_HALBERD);
                        output.accept(ModWeapons.DIAMOND_HALBERD);
                        output.accept(ModWeapons.NETHERITE_HALBERD);
                    })

                    .build();

    // =====================================
    // Registration
    // =====================================

    public static void initialize() {
        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                SAKACOUNTRY_WEAPONS_KEY,
                SAKACOUNTRY_WEAPONS
        );
    }
}
