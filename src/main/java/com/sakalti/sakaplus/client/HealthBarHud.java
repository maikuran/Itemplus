
package com.sakalti.sakaplus.client;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.effect.StatusEffects;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;

import java.util.Locale;

public final class HealthBarHud {

    private static final Identifier BACKGROUND =
        new Identifier("sakaplus", "textures/gui/hud/health_bar_background.png");

    private static final Identifier RED =
        new Identifier("sakaplus", "textures/gui/hud/health_bar_red.png");

    private static final Identifier GREEN =
        new Identifier("sakaplus", "textures/gui/hud/health_bar_green.png");

    private static final Identifier YELLOW =
        new Identifier("sakaplus", "textures/gui/hud/health_bar_yellow.png");

    private static final Identifier BLACK =
        new Identifier("sakaplus", "textures/gui/hud/health_bar_black.png");

    private static final Identifier HEART =
        new Identifier("sakaplus", "textures/gui/hud/mini_heart.png");

    private static final float BAR_WIDTH = 182.0F;
    private static final float BAR_HEIGHT = 10.0F;

    private HealthBarHud() {
    }

    public static void register() {

        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {

            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player == null || client.options.hudHidden) {
                return;
            }

            PlayerEntity player = client.player;

            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            float health = player.getHealth();
            float maxHealth = Math.max(player.getMaxHealth(), 1.0F);
            float absorption = player.getAbsorptionAmount();

            float x = (screenWidth - BAR_WIDTH) / 2.0F;
            float y = screenHeight - 62.0F;

            float healthRatio = MathHelper.clamp(
                health / maxHealth,
                0.0F,
                1.0F
            );

            float healthWidth = BAR_WIDTH * healthRatio;

            // 背景
            drawTexture(
                matrices,
                BACKGROUND,
                x, y,
                BAR_WIDTH, BAR_HEIGHT,
                1.0F
            );

            // HPカラー
            Identifier healthTexture = RED;

            if (player.hasStatusEffect(StatusEffects.WITHER)) {
                healthTexture = BLACK;
            } else if (player.hasStatusEffect(StatusEffects.POISON)) {
                healthTexture = GREEN;
            }

            // HPバー
            if (healthWidth > 0.0F) {
                drawTexture(
                    matrices,
                    healthTexture,
                    x, y,
                    healthWidth, BAR_HEIGHT,
                    healthRatio
                );
            }

            // 吸収HP
            if (absorption > 0.0F) {

                float absorptionWidth = Math.min(
                    BAR_WIDTH - healthWidth,
                    BAR_WIDTH * absorption / maxHealth
                );

                if (absorptionWidth > 0.0F) {
                    drawTexture(
                        matrices,
                        YELLOW,
                        x + healthWidth,
                        y,
                        absorptionWidth,
                        BAR_HEIGHT,
                        absorptionWidth / BAR_WIDTH
                    );
                }
            }

            // HP数値
            String hpText = String.format(
                Locale.ROOT,
                "%.1f/%.1f",
                health,
                maxHealth
            );

            int textWidth = client.textRenderer.getWidth(hpText);
            int totalWidth = 9 + 3 + textWidth;

            int textX = (screenWidth - totalWidth) / 2;
            int textY = (int) y - 13;

            // ミニハート
            drawTexture(
                matrices,
                HEART,
                textX,
                textY,
                9.0F,
                9.0F,
                1.0F
            );

            // HP数値
            client.textRenderer.drawWithShadow(
                matrices,
                hpText,
                textX + 12,
                textY,
                0xFFFFFFFF
            );
        });
    }

    private static void drawTexture(
        MatrixStack matrices,
        Identifier texture,
        float x,
        float y,
        float width,
        float height,
        float uMax
    ) {

        if (width <= 0.0F || height <= 0.0F) {
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        buffer.begin(
            VertexFormat.DrawMode.QUADS,
            VertexFormats.POSITION_TEXTURE
        );

        float u0 = 0.0F;
        float u1 = MathHelper.clamp(uMax, 0.0F, 1.0F);

        buffer.vertex(matrix, x, y + height, 0.0F)
            .texture(u0, 1.0F).next();

        buffer.vertex(matrix, x + width, y + height, 0.0F)
            .texture(u1, 1.0F).next();

        buffer.vertex(matrix, x + width, y, 0.0F)
            .texture(u1, 0.0F).next();

        buffer.vertex(matrix, x, y, 0.0F)
            .texture(u0, 0.0F).next();

        BufferRenderer.drawWithShader(buffer.end());

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}
