package com.sakalti.sakaplus.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Inject(
        method = "renderHealthBar",
        at = @At("HEAD"),
        cancellable = true
    )
    private void sakaplus$hideVanillaHealth(
        MatrixStack matrices,
        PlayerEntity player,
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
