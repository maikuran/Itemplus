
package com.sakalti.sakaplus.client.item;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import com.mojang.blaze3d.systems.RenderSystem;

public final class KruziRifleScopeOverlay {

    private KruziRifleScopeOverlay() {
    }

    // =========================================================
    // TEXTURE
    // =========================================================

    private static final Identifier SCOPE_TEXTURE =
            new Identifier(
                    "sakaplus",
                    "textures/gui/kruzi_rifle_scoop.png"
            );

    // =========================================================
    // REGISTER
    // =========================================================

    public static void register(Item rifleItem) {

        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {

            MinecraftClient client = MinecraftClient.getInstance();

            // プレイヤーとワールドの確認
            if (client.player == null || client.world == null) {
                return;
            }

            // GUI表示中は描画しない
            if (client.currentScreen != null) {
                return;
            }

            // =================================================
            // WEAPON CHECK
            // =================================================

            boolean holdingRifle =
                    client.player.getMainHandStack().isOf(rifleItem)
                    || client.player.getOffHandStack().isOf(rifleItem);

            if (!holdingRifle) {
                return;
            }

            // =================================================
            // SNEAK CHECK
            // =================================================

            if (!client.player.isSneaking()) {
                return;
            }

            // =================================================
            // SCREEN SIZE
            // =================================================

            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            // =================================================
            // RENDER SCOPE
            // =================================================

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            // 1.19.2 Yarn対応シェーダー
            RenderSystem.setShader(
                    GameRenderer::getPositionTexShader
            );

            // スコープテクスチャを指定
            RenderSystem.setShaderTexture(
                    0,
                    SCOPE_TEXTURE
            );

            // 描画色を白に設定
            RenderSystem.setShaderColor(
                    1.0F,
                    1.0F,
                    1.0F,
                    1.0F
            );

            // 画面全体にスコープを描画
            DrawableHelper.drawTexture(
                    matrices,
                    0,
                    0,
                    0.0F,
                    0.0F,
                    width,
                    height,
                    width,
                    height
            );

            // 描画状態を復元
            RenderSystem.setShaderColor(
                    1.0F,
                    1.0F,
                    1.0F,
                    1.0F
            );

            RenderSystem.disableBlend();
        });
    }
}
