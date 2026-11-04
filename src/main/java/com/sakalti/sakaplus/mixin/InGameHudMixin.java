package com.sakalti.sakaplus.mixin;

import net.minecraft.client.gui.hud.InGameHud;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    // ハート1個の描画だけをキャンセル
    @Inject(
        method = "drawHeart",
        at = @At("HEAD"),
        cancellable = true
    )
    private void sakaplus$hideVanillaHearts(CallbackInfo ci) {
        ci.cancel();
    }
}
