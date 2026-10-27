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

        registerClientSystems();
        registerEntityRenderers();
        registerModelLayers();
    }

    // =====================================================
    // Client Systems
    // =====================================================

    private void registerClientSystems() {

        // Gun recoil
        GunRecoil.register();

        // Kruzi Rifle Scope UI
        KruziRifleScopeOverlay.register(
                ModItems.KRUZI_RIFLE
        );

        // Skill UI networking
        SkillUI.registerClientNetworking();

        // Kruzi Rifle keybind
        KruziRifleKeybind.register();
    }

    // =====================================================
    // Entity Renderers
    // =====================================================

    private void registerEntityRenderers() {

        // Projectile
        EntityRendererRegistry.register(
                ModBeamEntities.BEAM_PROJECTILE,
                BeamProjectileRenderer::new
        );

        // Scorcher
        EntityRendererRegistry.register(
                ModEntities333.SCORCHER,
                ScorcherRenderer::new
        );

        // Shyron
        EntityRendererRegistry.register(
                ModEntities333.SHYRON,
                ShyronRenderer::new
        );

        // Xxena
        EntityRendererRegistry.register(
                ModEntities333.XXENA,
                XxenaRenderer::new
        );

        // Cryofreezer
        EntityRendererRegistry.register(
                ModEntities333.CRYOFREEZER,
                CryofreezerRenderer::new
        );

        // Kuloogie
        EntityRendererRegistry.register(
                ModEntities333.KULOOGIE,
                KuloogieRenderer::new
        );

        // Kur Brute
        EntityRendererRegistry.register(
                ModEntities333.KUR_BRUTE,
                KurBruteRenderer::new
        );

        // Warperdun
        EntityRendererRegistry.register(
                ModEntities333.WARPERDUN,
                WarperdunRenderer::new
        );

        // Nyanko
        EntityRendererRegistry.register(
                ModEntities333.NYANKO,
                NyankoRenderer::new
        );

        // Battle Nyanko
        EntityRendererRegistry.register(
                ModEntities333.BATTLE_NYANKO,
                BattleNyankoRenderer::new
        );

        // Kruzi Ant
        EntityRendererRegistry.register(
                ModEntities333.KRUZI_ANT,
                KruziAntRenderer::new
        );

        // Oganeson
        EntityRendererRegistry.register(
                ModEntities333.OGANESON,
                OganesonRenderer::new
        );
    }

    // =====================================================
    // Entity Model Layers
    // =====================================================

    private void registerModelLayers() {

        // Scorcher
        registerModelLayer(
                ScorcherRenderer.MODEL_LAYER,
                ScorcherModel::getTexturedModelData
        );

        // Kuloogie
        registerModelLayer(
                KuloogieRenderer.MODEL_LAYER,
                KuloogieModel::getTexturedModelData
        );

        // Kur Brute
        registerModelLayer(
                KurBruteRenderer.MODEL_LAYER,
                KurBruteModel::getTexturedModelData
        );

        // Warperdun
        registerModelLayer(
                WarperdunRenderer.MODEL_LAYER,
                WarperdunModel::getTexturedModelData
        );

        // Kruzi Ant
        registerModelLayer(
                KruziAntRenderer.MODEL_LAYER,
                KruziAntModel::getTexturedModelData
        );

        // Nyanko
        registerModelLayer(
                NyankoRenderer.MODEL_LAYER,
                NyankoModel::getTexturedModelData
        );

        // Battle Nyanko
        registerModelLayer(
                BattleNyankoRenderer.MODEL_LAYER,
                BattleNyankoModel::getTexturedModelData
        );

        // Oganeson
        registerModelLayer(
                OganesonRenderer.MODEL_LAYER,
                OganesonModel::getTexturedModelData
        );

        // Cryofreezer
        registerModelLayer(
                CryofreezerRenderer.MODEL_LAYER,
                CryofreezerModel::getTexturedModelData
        );

        // Shyron
        registerModelLayer(
                ShyronRenderer.MODEL_LAYER,
                ShyronModel::getTexturedModelData
        );

        // Xxena
        registerModelLayer(
                XxenaRenderer.MODEL_LAYER,
                XxenaModel::getTexturedModelData
        );
    }

    // =====================================================
    // Model Layer Registration Helper
    // =====================================================

    private void registerModelLayer(
            net.minecraft.client.render.entity.model.EntityModelLayer layer,
            EntityModelLayerRegistry.TexturedModelDataProvider provider
    ) {
        EntityModelLayerRegistry.registerModelLayer(
                layer,
                provider
        );
    }
}
