
package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.client.item.KruziRifleScopeOverlay;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class KruziRifleZoomMixin {

    @Inject(
            method = "getFov",
            at = @At("RETURN"),
            cancellable = true
    )
    private void sakaplus$applyRifleZoom(
            Camera camera,
            float tickDelta,
            boolean changingFov,
            CallbackInfoReturnable<Double> cir
    ) {

        if (!KruziRifleScopeOverlay.isZooming()) {
            return;
        }

        double originalFov = cir.getReturnValue();

        double zoomedFov =
                originalFov / KruziRifleScopeOverlay.ZOOM_MULTIPLIER;

        cir.setReturnValue(zoomedFov);
    }
}
