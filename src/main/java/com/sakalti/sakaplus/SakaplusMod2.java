
package com.sakalti.sakaplus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.sakalti.sakaplus.block.ModBlocks2;
import com.sakalti.sakaplus.block.SakaplusBlocks;
import com.sakalti.sakaplus.ore.SakaPlusOreBlocks;
import com.sakalti.sakaplus.ore.SakaPlusOreBlockItems;
import com.sakalti.sakaplus.ore.SakaPlusOreWorldgen;

import com.sakalti.sakaplus.effect.ModEffects;
import com.sakalti.sakaplus.command.KruzivilimCommand;
import com.sakalti.sakaplus.registry.*;
import com.sakalti.sakaplus.dimension.SakaplusDimensions;
import com.sakalti.sakaplus.tinco.item.ModItems5;

public class SakaplusMod2 implements ModInitializer {

    @Override
    public void onInitialize() {
        // ポーション
        LongtimePotionRegistry.registerAll();

        // 新しいブロック
        ModBlocks2.registerBlocks();

        // 旧ブロック・鉱石ブロック
        SakaplusBlocks.register();
        SakaPlusOreBlocks.init();
        SakaPlusOreBlockItems.init();
        SakaPlusOreWorldgen.init();

        // ステータス効果・アイテム・エンティティ
        ModEffects.registerEffects();
        Weapons.registerItems();
        ModEntities333.register();
        ModItems.register();
        ModItems5.register();

        // ディメンション
        SakaplusDimensions.register();

        // コマンド
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) ->
                KruzivilimCommand.register(dispatcher)
        );
    }
}
