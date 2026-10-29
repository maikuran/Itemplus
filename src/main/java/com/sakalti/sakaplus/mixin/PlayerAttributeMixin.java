
package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.attribute.ModAttributes;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerAttributeMixin {

    @Inject(
            method = "createPlayerAttributes",
            at = @At("RETURN")
    )
    private static void sakaplus$addAdaptationAttribute(
            CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir
    ) {
        cir.getReturnValue().add(
                ModAttributes.ADAPTATION,
                0.0D
        );
    }
}
