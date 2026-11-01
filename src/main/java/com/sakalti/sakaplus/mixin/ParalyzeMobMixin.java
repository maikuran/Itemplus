package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.effect.ModEffects;

import net.minecraft.entity.mob.MobEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class ParalyzeMobMixin {

    @Inject(
        method = "mobTick",
        at = @At("HEAD"),
        cancellable = true
    )
    private void sakaplus$stopMobAI(CallbackInfo ci) {

        MobEntity mob = (MobEntity) (Object) this;

        if (mob.hasStatusEffect(ModEffects.PARALYZE)) {
            mob.setVelocity(0.0, 0.0, 0.0);
            ci.cancel();
        }
    }
}
