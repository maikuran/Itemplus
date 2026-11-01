package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.effect.ModEffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ParalyzeLivingMixin {

    @Inject(
        method = "travel",
        at = @At("HEAD"),
        cancellable = true
    )
    private void sakaplus$paralyzeMovement(
            Vec3d movementInput,
            CallbackInfo ci
    ) {

        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.hasStatusEffect(ModEffects.PARALYZE)) {

            entity.setVelocity(Vec3d.ZERO);
            entity.velocityModified = true;

            ci.cancel();
        }
    }
}
