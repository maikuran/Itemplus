package com.sakalti.sakaplus.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModTabs {
    
    // カスタムクリエイティブタブの定義
    public static final ItemGroup STARTER_AND_KRUZIVILIM_TAB = FabricItemGroupBuilder.build(
        new Identifier("sakaplus", "starter_and_kruzivilim"),
        () -> new ItemStack(ModItems.RAW_PYRONIUM) // タブのアイコンとして使用するアイテム
    );
}
