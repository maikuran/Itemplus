package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.item.GreatswordItem;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    // サーバー側の最大インタラクト距離の2乗（デフォルト 36.0D = 6.0 * 6.0）を緩和
    @ModifyConstant(method = "onPlayerInteractEntity", constant = @Constant(doubleValue = 36.0D))
    private double modifyMaxInteractDistance(double original) {
        if (this.player != null && this.player.getMainHandStack().getItem() instanceof GreatswordItem) {
            double extended = 6.0D + 1.5D;
            return extended * extended; // 56.25D
        }
        return original;
    }
}
