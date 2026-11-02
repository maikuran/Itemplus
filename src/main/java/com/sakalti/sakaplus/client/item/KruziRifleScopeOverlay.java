package com.sakalti.sakaplus.client.item;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import com.mojang.blaze3d.systems.RenderSystem;

public final class KruziRifleScopeOverlay {

    private KruziRifleScopeOverlay() {
    }

    private static final Identifier SCOPE_TEXTURE =
            new Identifier(
                    "sakaplus",
                    "textures/gui/kruzi_rifle_scoop.png"
            );

    public static final double ZOOM_MULTIPLIER = 4.0;
    private static final double ZOOM_TRANSITION_SECONDS = 0.12;

    private static boolean zooming = false;
    private static double zoomProgress = 0.0;
    private static long lastZoomUpdateNanos = System.nanoTime();

    public static void register(Item rifleItem) {

        // ズーム状態更新
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) {
                zooming = false;
                return;
            }

            boolean holdingRifle =
                    client.player.getMainHandStack().isOf(rifleItem)
                    || client.player.getOffHandStack().isOf(rifleItem);

            zooming =
                    holdingRifle
                    && client.player.isSneaking()
                    && client.currentScreen == null
                    && client.options.getPerspective() == Perspective.FIRST_PERSON;
        });

        // スコープ画像描画
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {

            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player == null
                    || client.world == null
                    || client.options.getPerspective() != Perspective.FIRST_PERSON) {
                return;
            }

            double progress = getZoomProgress();
            if (progress <= 0.0) {
                return;
            }

            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            RenderSystem.setShader(
                    GameRenderer::getPositionTexShader
            );

            RenderSystem.setShaderTexture(
                    0,
                    SCOPE_TEXTURE
            );

            RenderSystem.setShaderColor(
                    1.0F, 1.0F, 1.0F, (float) progress
            );

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

            RenderSystem.setShaderColor(
                    1.0F, 1.0F, 1.0F, 1.0F
            );

            RenderSystem.disableBlend();
        });
    }

    /** Returns the eased zoom amount, from 0.0 (off) to 1.0 (fully zoomed). */
    public static double getZoomProgress() {
        long now = System.nanoTime();
        double deltaSeconds = (now - lastZoomUpdateNanos) / 1_000_000_000.0;
        lastZoomUpdateNanos = now;

        double target = zooming ? 1.0 : 0.0;
        double blend = 1.0 - Math.exp(-deltaSeconds / ZOOM_TRANSITION_SECONDS);
        zoomProgress += (target - zoomProgress) * blend;

        if (Math.abs(target - zoomProgress) < 0.001) {
            zoomProgress = target;
        }

        return zoomProgress;
    }

    public static boolean isZooming() {
        return zooming;
    }
}
