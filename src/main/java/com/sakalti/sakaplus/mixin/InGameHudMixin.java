package com.sakalti.sakaplus.mixin;

import net.minecraft.client.gui.hud.InGameHud;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    // バニラのハート描画をキャンセル
    @Inject(
        method = "renderHealthBar",
        at = @At("HEAD"),
        cancellable = true
    )
    private void sakaplus$removeHealthHearts(CallbackInfo ci) {
        ci.cancel();
    }
}
