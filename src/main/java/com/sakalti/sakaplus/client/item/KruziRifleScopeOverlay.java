
package com.sakalti.sakaplus.client.item;

import com.sakalti.sakaplus.item.KruziRifle;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.item.Item;

import net.minecraft.util.Identifier;

import com.mojang.blaze3d.systems.RenderSystem;

public final class KruziRifleScopeOverlay {

    private KruziRifleScopeOverlay() {}

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

            if (client.player == null || client.world == null) {
                return;
            }

            // Do not display while another GUI is open.
            if (client.currentScreen != null) {
                return;
            }

            // Check held weapon.
            boolean holdingRifle =
                    client.player.getMainHandStack().isOf(rifleItem)
                    || client.player.getOffHandStack().isOf(rifleItem);

            if (!holdingRifle) {
                return;
            }

            // Only while sneaking.
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

            RenderSystem.setShader(GameRenderer::getPositionTexProgram);

            RenderSystem.setShaderTexture(
                    0,
                    SCOPE_TEXTURE
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

            RenderSystem.disableBlend();
        });
    }
}
