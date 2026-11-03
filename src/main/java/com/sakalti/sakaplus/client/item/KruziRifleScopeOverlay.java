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

    /** Zoom from the normal FOV to one quarter of it. */
    public static final double MAX_ZOOM_MULTIPLIER = 4.0;
    /** Time for the zoom to reach its target, in milliseconds. */
    private static final double ZOOM_TRANSITION_MILLIS = 200.0;
    private static final long NANOS_PER_MILLI = 1_000_000L;

    private static boolean zooming = false;
    private static double zoomStartProgress = 0.0;
    private static double zoomTargetProgress = 0.0;
    private static long zoomTransitionStartNanos = System.nanoTime();

    public static void register(Item rifleItem) {

        // ズーム状態更新
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) {
                setZooming(false);
                return;
            }

            boolean holdingRifle =
                    client.player.getMainHandStack().isOf(rifleItem)
                    || client.player.getOffHandStack().isOf(rifleItem);

            setZooming(
                    holdingRifle
                    && client.player.isSneaking()
                    && client.currentScreen == null
                    && client.options.getPerspective() == Perspective.FIRST_PERSON
            );
        });

        // スコープ画像描画
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {

            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player == null
                    || client.world == null
                    || client.options.getPerspective() != Perspective.FIRST_PERSON
                    || getZoomProgress() <= 0.0) {
                return;
            }

            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, SCOPE_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

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

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
        });
    }

    private static void setZooming(boolean shouldZoom) {
        if (zooming == shouldZoom) {
            return;
        }

        // 途中で切り替えても、現在のズーム量から自然に反転させる
        zoomStartProgress = getZoomProgress();
        zoomTargetProgress = shouldZoom ? 1.0 : 0.0;
        zoomTransitionStartNanos = System.nanoTime();
        zooming = shouldZoom;
    }

    /** Smoothly transitions from 0.0 (off) to 1.0 (fully zoomed) over 200 ms. */
    public static double getZoomProgress() {
        double elapsedMillis =
                (System.nanoTime() - zoomTransitionStartNanos) / (double) NANOS_PER_MILLI;
        double linearProgress = Math.min(elapsedMillis / ZOOM_TRANSITION_MILLIS, 1.0);

        // Smoothstep: soft start and stop, with continuous values rather than discrete steps.
        double easedProgress = linearProgress * linearProgress * (3.0 - 2.0 * linearProgress);
        return zoomStartProgress
                + (zoomTargetProgress - zoomStartProgress) * easedProgress;
    }

    /** Returns a continuously changing zoom multiplier from 1x to 4x. */
    public static double getZoomMultiplier() {
        return 1.0 + (MAX_ZOOM_MULTIPLIER - 1.0) * getZoomProgress();
    }

    public static boolean isZooming() {
        return zooming;
    }
}
