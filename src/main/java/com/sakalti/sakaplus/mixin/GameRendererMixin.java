package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.item.GreatswordItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    // サバイバル(3.0D) と クリエイティブ(6.0D) の両方のエンティティ判定距離を延長
    @ModifyConstant(
        method = "updateTargetedEntity",
        constant = {
            @Constant(doubleValue = 3.0D),
            @Constant(doubleValue = 6.0D)
        }
    )
    private double modifyEntityReach(double original) {
        if (this.client.player != null && this.client.player.getMainHandStack().getItem() instanceof GreatswordItem) {
            return original + 1.5D;
        }
        return original;
    }

    // サバイバル時の距離の2乗チェック (9.0D = 3.0 * 3.0) を延長
    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9.0D))
    private double modifyEntityReachSquared(double original) {
        if (this.client.player != null && this.client.player.getMainHandStack().getItem() instanceof GreatswordItem) {
            double extended = 3.0D + 1.5D;
            return extended * extended; // 20.25D
        }
        return original;
    }
}
