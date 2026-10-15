
package com.sakalti.sakaplus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.sakalti.sakaplus.tinco.item.ModItems5;
import com.sakalti.sakaplus.effect.ModEffects;
import com.sakalti.sakaplus.command.KruzivilimCommand;
import com.sakalti.sakaplus.registry.*;
import com.sakalti.sakaplus.dimension.SakaplusDimensions;
import com.sakalti.sakaplus.block.ModBlocks2;

public class SakaplusMod2 implements ModInitializer {
    @Override
    public void onInitialize() {
        // 例: Mod initializerやonInitialize()で
        LongtimePotionRegistry.registerAll();
        ModBlocks2.registerBlocks();
        ModEffects.registerEffects();
        Weapons.registerItems();
        ModEntities333.register();
        ModItems.register();
        SakaplusDimensions.register();
        ModItems5.register();
        // コマンドの登録（Fabric API v2）
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            KruzivilimCommand.register(dispatcher);
        });
    }
}
