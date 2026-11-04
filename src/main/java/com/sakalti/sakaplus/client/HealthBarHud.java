package com.sakalti.sakaplus.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class HealthBarHud {

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

    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 10;

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

            int x = (screenWidth - BAR_WIDTH) / 2;
            int y = screenHeight - 62;

            // =========================================
            // HP割合
            // =========================================

            float healthRatio = MathHelper.clamp(
                health / maxHealth,
                0.0F,
                1.0F
            );

            int healthWidth = Math.round(BAR_WIDTH * healthRatio);

            // =========================================
            // 背景テクスチャ
            // =========================================

            drawTexture(
                client,
                matrices,
                BACKGROUND,
                x,
                y,
                BAR_WIDTH,
                BAR_HEIGHT,
                BAR_WIDTH,
                BAR_HEIGHT
            );

            // =========================================
            // HPカラー選択
            // =========================================

            Identifier healthTexture = RED;

            if (player.hasStatusEffect(StatusEffects.WITHER)) {
                healthTexture = BLACK;
            } else if (player.hasStatusEffect(StatusEffects.POISON)) {
                healthTexture = GREEN;
            }

            // =========================================
            // HPバー
            // =========================================

            if (healthWidth > 0) {

                drawTexture(
                    client,
                    matrices,
                    healthTexture,
                    x,
                    y,
                    healthWidth,
                    BAR_HEIGHT,
                    BAR_WIDTH,
                    BAR_HEIGHT
                );
            }

            // =========================================
            // 吸収HP（黄色）
            // =========================================

            if (absorption > 0) {

                int absorptionWidth = Math.min(
                    BAR_WIDTH - healthWidth,
                    Math.round(BAR_WIDTH * absorption / maxHealth)
                );

                if (absorptionWidth > 0) {

                    drawTexture(
                        client,
                        matrices,
                        YELLOW,
                        x + healthWidth,
                        y,
                        absorptionWidth,
                        BAR_HEIGHT,
                        BAR_WIDTH,
                        BAR_HEIGHT
                    );
                }
            }

            // =========================================
            // ミニハートアイコン
            // =========================================

            String hpText =
                MathHelper.ceil(health)
                + "/"
                + MathHelper.ceil(maxHealth);

            int textWidth = client.textRenderer.getWidth(hpText);

            int totalWidth = 9 + 3 + textWidth;

            int textX = (screenWidth - totalWidth) / 2;

            int textY = y - 13;

            drawTexture(
                client,
                matrices,
                HEART,
                textX,
                textY,
                9,
                9,
                9,
                9
            );

            // =========================================
            // HP数値
            // =========================================

            client.textRenderer.drawWithShadow(
                matrices,
                hpText,
                textX + 12,
                textY,
                0xFFFFFFFF
            );
        });
    }

    // =============================================
    // Texture Renderer (Minecraft 1.19.2)
    // =============================================

    private static void drawTexture(
        MinecraftClient client,
        net.minecraft.client.util.math.MatrixStack matrices,
        Identifier texture,
        int x,
        int y,
        int width,
        int height,
        int textureWidth,
        int textureHeight
    ) {

        client.getTextureManager().bindTexture(texture);

        DrawableHelper.drawTexture(
            matrices,
            x,
            y,
            0,
            0,
            width,
            height,
            textureWidth,
            textureHeight
        );
    }
}
