
package com.sakalti.sakaplus.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class IcyBowHitMixin {

    @Inject(
            method = "onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V",
            at = @At("HEAD")
    )
    private void sakaplus$icyBowHit(
            EntityHitResult hitResult,
            CallbackInfo ci
    ) {
        PersistentProjectileEntity arrow =
                (PersistentProjectileEntity) (Object) this;

        if (!arrow.getScoreboardTags().contains("sakaplus_icy_bow")) {
            return;
        }

        Entity entity = hitResult.getEntity();

        if (!(entity instanceof LivingEntity)) {
            return;
        }

        LivingEntity target = (LivingEntity) entity;

        if (target.getWorld().isClient) {
            return;
        }

        target.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.SLOWNESS,
                        100,
                        2
                )
        );
    }
}
