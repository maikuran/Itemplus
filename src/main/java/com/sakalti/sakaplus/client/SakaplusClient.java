
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
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class SakaplusClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerClientSystems();
        registerEntityRenderers();
        registerModelLayers();
    }

    // Client Systems
    private void registerClientSystems() {
        GunRecoil.register();
        KruziRifleScopeOverlay.register(ModItems.KRUZI_RIFLE);
        SkillUI.registerClientNetworking();
        KruziRifleKeybind.register();
    }

    // Entity Renderers
    private void registerEntityRenderers() {
        EntityRendererRegistry.register(ModBeamEntities.BEAM_PROJECTILE, BeamProjectileRenderer::new);

        EntityRendererRegistry.register(ModEntities333.SCORCHER, ScorcherRenderer::new);
        EntityRendererRegistry.register(ModEntities333.SHYRON, ShyronRenderer::new);
        EntityRendererRegistry.register(ModEntities333.XXENA, XxenaRenderer::new);

        EntityRendererRegistry.register(ModEntities333.CRYOFREEZER, CryofreezerRenderer::new);
        EntityRendererRegistry.register(ModEntities333.CRYOMAGE, CryomageRenderer::new);
        EntityRendererRegistry.register(ModEntities333.CRYOWARRIOR, CryowarriorRenderer::new);
        EntityRendererRegistry.register(ModEntities333.CRYOBEAST, CryobeastRenderer::new);

        EntityRendererRegistry.register(ModEntities333.KULOOGIE, KuloogieRenderer::new);
        EntityRendererRegistry.register(ModEntities333.KUR_BRUTE, KurBruteRenderer::new);
        EntityRendererRegistry.register(ModEntities333.WARPERDUN, WarperdunRenderer::new);
        EntityRendererRegistry.register(ModEntities333.NYANKO, NyankoRenderer::new);
        EntityRendererRegistry.register(ModEntities333.BATTLE_NYANKO, BattleNyankoRenderer::new);
        EntityRendererRegistry.register(ModEntities333.KRUZI_ANT, KruziAntRenderer::new);
        EntityRendererRegistry.register(ModEntities333.OGANESON, OganesonRenderer::new);
    }

    // Entity Model Layers
    private void registerModelLayers() {
        registerLayer(ScorcherRenderer.MODEL_LAYER, ScorcherModel::getTexturedModelData);
        registerLayer(KuloogieRenderer.MODEL_LAYER, KuloogieModel::getTexturedModelData);
        registerLayer(KurBruteRenderer.MODEL_LAYER, KurBruteModel::getTexturedModelData);
        registerLayer(WarperdunRenderer.MODEL_LAYER, WarperdunModel::getTexturedModelData);
        registerLayer(KruziAntRenderer.MODEL_LAYER, KruziAntModel::getTexturedModelData);
        registerLayer(NyankoRenderer.MODEL_LAYER, NyankoModel::getTexturedModelData);
        registerLayer(BattleNyankoRenderer.MODEL_LAYER, BattleNyankoModel::getTexturedModelData);
        registerLayer(OganesonRenderer.MODEL_LAYER, OganesonModel::getTexturedModelData);

        registerLayer(CryofreezerRenderer.MODEL_LAYER, CryofreezerModel::getTexturedModelData);
        registerLayer(CryomageRenderer.MODEL_LAYER, CryoHumanoidModel::getTexturedModelData);
        registerLayer(CryowarriorRenderer.MODEL_LAYER, CryoHumanoidModel::getTexturedModelData);
        registerLayer(CryobeastRenderer.MODEL_LAYER, CryobeastModel::getTexturedModelData);

        registerLayer(ShyronRenderer.MODEL_LAYER, ShyronModel::getTexturedModelData);
        registerLayer(XxenaRenderer.MODEL_LAYER, XxenaModel::getTexturedModelData);
    }

    private void registerLayer(
            EntityModelLayer layer,
            EntityModelLayerRegistry.TexturedModelDataProvider provider
    ) {
        EntityModelLayerRegistry.registerModelLayer(layer, provider);
    }
}
