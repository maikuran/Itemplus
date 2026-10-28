package com.sakalti.sakaplus.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModTabs {
    
    // 既存の Starter and Kruzivilim タブ
    public static final ItemGroup STARTER_AND_KRUZIVILIM_TAB = FabricItemGroupBuilder.build(
        new Identifier("sakaplus", "starter_and_kruzivilim"),
        () -> new ItemStack(ModItems.RAW_PYRONIUM)
    );

    // 新設：Cyrovilim タブ (アイコン: アイスグレートブレード)
    public static final ItemGroup CYROVILIM_TAB = FabricItemGroupBuilder.build(
        new Identifier("sakaplus", "cyrovilim"),
        () -> new ItemStack(Weapons.ICE_GREATBLADE)
    );
}
