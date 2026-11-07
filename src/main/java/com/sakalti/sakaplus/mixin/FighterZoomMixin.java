package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.client.item.FighterScopeOverlay;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class FighterZoomMixin {

    @Inject(
            method = "getFov",
            at = @At("RETURN"),
            cancellable = true
    )
    private void sakaplus$applyFighterZoom(
            Camera camera,
            float tickDelta,
            boolean changingFov,
            CallbackInfoReturnable<Double> cir
    ) {

        double zoomMultiplier =
                FighterScopeOverlay.getZoomMultiplier();

        if (zoomMultiplier <= 1.0) {
            return;
        }

        double originalFov = cir.getReturnValue();

        cir.setReturnValue(
                originalFov / zoomMultiplier
        );
    }
}
