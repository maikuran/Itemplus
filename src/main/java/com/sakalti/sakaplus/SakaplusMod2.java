
    

package com.sakalti.sakaplus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.sakalti.sakaplus.item.CatalystItems;
import com.sakalti.sakaplus.client.item.*;
import com.sakalti.sakaplus.tinco.item.ModItems5;
import com.sakalti.sakaplus.effect.ModEffects;
import com.sakalti.sakaplus.command.KruzivilimCommand;
import com.sakalti.sakaplus.registry.*;
import com.sakalti.sakaplus.dimension.SakaplusDimensions;
import com.sakalti.sakaplus.block.ModBlocks2;
import com.sakalti.sakaplus.world.GreenyFeatures;
import com.sakalti.sakaplus.sound.SakaplusSounds;
import com.sakalti.sakaplus.player.skill.KillSkillLogic;
import com.sakalti.sakaplus.player.skill.WalkSkillLogic;

public class SakaplusMod2 implements ModInitializer {

    @Override
    public void onInitialize() {

        // Sounds
        SakaplusSounds.registerSounds();
        KruziRifle.registerServer();


        // Potions
        LongtimePotionRegistry.registerAll();

        // Blocks
        ModBlocks2.registerBlocks();

        // Effects
        ModEffects.registerEffects();

        // Items
        CatalystItems.registerCatalysts();
        Weapons.registerItems();
        ModItems.register();
        ModItems5.register();

        // Entities
        ModEntities333.register();
        ModBeamEntities.register();

        // Dimensions
        SakaplusDimensions.register();

        // Greeny Features
        GreenyFeatures.register();

        // Commands
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) -> {
                KruzivilimCommand.register(dispatcher);
            }
        );

        // Kill Skill
        KillSkillLogic.initialize();

        // Walk Skill
        WalkSkillLogic.initialize();
    }
}
