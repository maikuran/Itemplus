package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.item.GreatswordItem;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    // onPlayerInteractEntity 内での MAX_BREAK_SQUARED_DISTANCE (36.0D) 参照をフック
    @Redirect(
        method = "onPlayerInteractEntity",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D",
            opcode = Opcodes.GETSTATIC
        )
    )
    private double modifyMaxInteractDistance() {
        if (this.player != null && this.player.getMainHandStack().getItem() instanceof GreatswordItem) {
            double extended = 6.0D + 1.5D;
            return extended * extended; // 56.25D
        }
        return 36.0D;
    }
}
