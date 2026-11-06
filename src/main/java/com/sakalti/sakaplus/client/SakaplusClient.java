
package com.sakalti.sakaplus.client;

import com.sakalti.sakaplus.client.model.*;
import com.sakalti.sakaplus.client.item.*;
import com.sakalti.sakaplus.effect.*;
import com.sakalti.sakaplus.client.renderer.*;
import com.sakalti.sakaplus.item.DansiteBowItem;
import com.sakalti.sakaplus.registry.ModBeamEntities;
import com.sakalti.sakaplus.registry.ModEntities333;
import com.sakalti.sakaplus.registry.ModItems;
import com.sakalti.sakaplus.block.ModBlocks2;
import com.sakalti.sakaplus.player.skill.SkillUI;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.item.v1.ItemModelPredicateProviderRegistry;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SakaplusClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerClientSystems();
        registerDansiteBowPredicates();
        registerBlockRenderLayers();
        registerEntityRenderers();
        registerModelLayers();
    }

    // =====================================================
    // Client Systems
    // =====================================================

    private void registerClientSystems() {
        GunRecoil.register();
        KruziRifleScopeOverlay.register(ModItems.KRUZI_RIFLE);
        SkillUI.registerClientNetworking();
        KruziRifleKeybind.register();
        new ParalyzeHandler().onInitializeClient();
        HealthBarHud.register();
        new LightBlockOverlay().onInitializeClient();
    }

    // =====================================================
    // Dansite Bow Model Predicates
    // =====================================================

    private void registerDansiteBowPredicates() {

        ItemModelPredicateProviderRegistry.register(
            ModItems.DANSITE_BOW,
            new Identifier("pulling"),
            (stack, world, entity, seed) -> {
                if (entity == null) {
                    return 0.0F;
                }

                return entity.isUsingItem()
                    && ItemStack.areEqual(entity.getActiveItem(), stack)
                    ? 1.0F
                    : 0.0F;
            }
        );

        ItemModelPredicateProviderRegistry.register(
            ModItems.DANSITE_BOW,
            new Identifier("pull"),
            (stack, world, entity, seed) -> {
                if (entity == null || !entity.isUsingItem()
                    || !ItemStack.areEqual(entity.getActiveItem(), stack)) {
                    return 0.0F;
                }

                int elapsed = stack.getMaxUseTime()
                    - entity.getItemUseTimeLeft();

                return BowItem.getPullProgress(elapsed);
            }
        );
    }

    // =====================================================
    // Block Render Layers
    // =====================================================

    private void registerBlockRenderLayers() {

        BlockRenderLayerMap.INSTANCE.putBlock(
            ModBlocks2.GREENY_LEAVES,
            RenderLayer.getCutout()
        );

        BlockRenderLayerMap.INSTANCE.putBlock(
            ModBlocks2.GREENY_SAPLING,
            RenderLayer.getCutout()
        );

        BlockRenderLayerMap.INSTANCE.putBlock(
            ModBlocks2.GREENY_DOOR,
            RenderLayer.getCutout()
        );

        BlockRenderLayerMap.INSTANCE.putBlock(
            ModBlocks2.GREENY_TRAPDOOR,
            RenderLayer.getCutout()
        );
    }

    // =====================================================
    // Entity Renderers
    // =====================================================

    private void registerEntityRenderers() {

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
            ModEntities333.ARSE,
            ArseRenderer::new
        );

        EntityRendererRegistry.register(
            ModEntities333.BUSHBEAST,
            BushBeastRenderer::new
        );

        EntityRendererRegistry.register(
            ModEntities333.XXENA,
            XxenaRenderer::new
        );

        EntityRendererRegistry.register(
            ModEntities333.CRYOFREEZER,
            CryofreezerRenderer::new
        );

        EntityRendererRegistry.register(
            ModEntities333.CRYOMAGE,
            CryomageRenderer::new
        );

        EntityRendererRegistry.register(
            ModEntities333.CRYOWARRIOR,
            CryowarriorRenderer::new
        );

        EntityRendererRegistry.register(
            ModEntities333.CRYOBEAST,
            CryobeastRenderer::new
        );

        EntityRendererRegistry.register(
            ModEntities333.CRYOSNAKE,
            CryoSnakeRenderer::new
        );

        EntityRendererRegistry.register(
            ModEntities333.CRYOPTERYX,
            CryopteryxRenderer::new
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

        EntityRendererRegistry.register(
            ModEntities333.KRYOSON,
            KryosonRenderer::new
        );

        EntityRendererRegistry.register(
            ModEntities333.PHOSPHORAN,
            PhosphoranRenderer::new
        );

        EntityRendererRegistry.register(
            ModEntities333.SULFAN,
            SulfanRenderer::new
        );
    }

    // =====================================================
    // Entity Model Layers
    // =====================================================

    private void registerModelLayers() {

        registerLayer(ScorcherRenderer.MODEL_LAYER, ScorcherModel::getTexturedModelData);
        registerLayer(BushBeastRenderer.MODEL_LAYER, BushBeastModel::getTexturedModelData);
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
        registerLayer(CryoSnakeRenderer.MODEL_LAYER, CryoSnakeModel::getTexturedModelData);
        registerLayer(CryopteryxRenderer.MODEL_LAYER, CryopteryxModel::getTexturedModelData);
        registerLayer(ShyronRenderer.MODEL_LAYER, ShyronModel::getTexturedModelData);
        registerLayer(XxenaRenderer.MODEL_LAYER, XxenaModel::getTexturedModelData);
        registerLayer(ArseRenderer.MODEL_LAYER, ArseModel::getTexturedModelData);
        registerLayer(KryosonRenderer.MODEL_LAYER, KryosonModel::getTexturedModelData);
        registerLayer(PhosphoranRenderer.MODEL_LAYER, PhosphoranModel::getTexturedModelData);
        registerLayer(SulfanRenderer.MODEL_LAYER, SulfanModel::getTexturedModelData);
    }

    // =====================================================
    // Register Model Layer
    // =====================================================

    private void registerLayer(
        EntityModelLayer layer,
        EntityModelLayerRegistry.TexturedModelDataProvider provider
    ) {
        EntityModelLayerRegistry.registerModelLayer(layer, provider);
    }
}
