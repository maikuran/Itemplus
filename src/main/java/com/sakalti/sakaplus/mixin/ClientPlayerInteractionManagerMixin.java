package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.item.GreatswordItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "getReachDistance", at = @At("RETURN"), cancellable = true)
    private void modifyReachDistance(CallbackInfoReturnable<Float> cir) {
        if (this.client.player == null) return;

        PlayerEntity player = this.client.player;
        if (player.getMainHandStack().getItem() instanceof GreatswordItem) {
            cir.setReturnValue(cir.getReturnValueF() + 1.5F);
        }
    }
}
