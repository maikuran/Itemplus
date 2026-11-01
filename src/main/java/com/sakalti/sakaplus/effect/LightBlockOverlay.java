package com.sakalti.sakaplus.effect;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import com.sakalti.sakaplus.effect.ModEffects;

public class LightBlockOverlay implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null 
                && client.player.hasStatusEffect(ModEffects.LIGHT_BLOCK)) { // ModEffects.LIGHT_BLOCK が他ファイルで定義されている必要あり
                drawWhiteOverlay(matrices, client);
            }
        });
    }

    private void drawWhiteOverlay(MatrixStack matrices, MinecraftClient client) {
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        // fill(matrices, left, top, right, bottom, color)
        // 0xAARRGGBB形式。0xE0FFFFFFは白の80%不透明
        client.inGameHud.fill(matrices, 0, 0, width, height, 0x33FFFFFF);
    }
}
