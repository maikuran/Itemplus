
package com.sakalti.sakaplus.client;

import com.sakalti.sakaplus.client.model.*;
import com.sakalti.sakaplus.client.item.*;
import com.sakalti.sakaplus.client.renderer.*;
import com.sakalti.sakaplus.registry.ModBeamEntities;
import com.sakalti.sakaplus.registry.ModEntities333;
import com.sakalti.sakaplus.registry.ModItems;
import com.sakalti.sakaplus.player.skill.SkillUI;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class SakaplusClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // =====================================================
        // Gun
        // =====================================================

        GunRecoil.register();
        KruziRifle.registerServer();

        // Kruzi Rifle Scope UI
        KruziRifleScopeOverlay.register(
                ModItems.KRUZI_RIFLE
        );

        // =====================================================
        // Skill UI
        // =====================================================

        SkillUI.registerClientNetworking();

        // =====================================================
        // Entity Renderers
        // =====================================================

        EntityRendererRegistry.register(
                ModBeamEntities.BEAM_PROJECTILE,
                BeamProjectileRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities333.SCORCHER,
                ScorcherRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities333.SHYRON,
                ShyronRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities333.XXENA,
                XxenaRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities333.KULOOGIE,
                KuloogieRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities333.KUR_BRUTE,
                KurBruteRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities333.WARPERDUN,
                WarperdunRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities333.NYANKO,
                NyankoRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities333.BATTLE_NYANKO,
                BattleNyankoRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities333.KRUZI_ANT,
                KruziAntRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities333.OGANESON,
                OganesonRenderer::new
        );
        KruziRifleKeybind.register();

        // =====================================================
        // Entity Model Layers
        // =====================================================

        EntityModelLayerRegistry.registerModelLayer(
                ScorcherRenderer.MODEL_LAYER,
                ScorcherModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                KuloogieRenderer.MODEL_LAYER,
                KuloogieModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                KurBruteRenderer.MODEL_LAYER,
                KurBruteModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                WarperdunRenderer.MODEL_LAYER,
                WarperdunModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                KruziAntRenderer.MODEL_LAYER,
                KruziAntModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                NyankoRenderer.MODEL_LAYER,
                NyankoModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                BattleNyankoRenderer.MODEL_LAYER,
                BattleNyankoModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                OganesonRenderer.MODEL_LAYER,
                OganesonModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                ShyronRenderer.MODEL_LAYER,
                ShyronModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                XxenaRenderer.MODEL_LAYER,
                XxenaModel::getTexturedModelData
        );
    }
}
