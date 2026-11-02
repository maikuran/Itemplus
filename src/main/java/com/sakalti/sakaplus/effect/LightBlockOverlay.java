package com.sakalti.sakaplus.effect;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import com.sakalti.sakaplus.effect.ModEffects;

public class LightBlockOverlay implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // HudRenderCallbackはF1非表示時でも背後でイベントが発火し続けます
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            
            // プレイヤーが存在し、かつ対象のデバフ（ステータス効果）を持っている場合のみ実行
            if (client.player != null && client.player.hasStatusEffect(ModEffects.LIGHT_BLOCK)) {
                drawWhiteOverlay(matrices, client);
            }
        });
    }

    private void drawWhiteOverlay(MatrixStack matrices, MinecraftClient client) {
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        // 1.19.2ではDrawableHelper.fillを使用。F1キーを無視して画面全体に描画します
        // 0x33FFFFFF = アルファ値20%の白。必要に応じて濃さを調整してください
        DrawableHelper.fill(matrices, 0, 0, width, height, 0x33FFFFFF);
    }
}
