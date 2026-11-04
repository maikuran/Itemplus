package com.sakalti.sakaplus.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    // バニラのHPハート描画を強制キャンセル
    @Inject(
        method = "renderHealthBar",
        at = @At("HEAD"),
        cancellable = true
    )
    private void sakaplus$cancelVanillaHealthBar(
        MatrixStack matrices,
        ClientPlayerEntity player,
        int x,
        int y,
        int lines,
        int regeneratingHeartIndex,
        float maxHealth,
        int lastHealth,
        int health,
        int absorption,
        boolean blinking,
        CallbackInfo ci
    ) {
        ci.cancel();
    }
}
