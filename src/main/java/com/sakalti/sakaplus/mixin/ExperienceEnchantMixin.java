
package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.enchant.ModEnchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ExperienceEnchantMixin {

    @Shadow
    protected PlayerEntity attackingPlayer;

    @Inject(
        method = "getXpToDrop()I",
        at = @At("RETURN"),
        cancellable = true
    )
    private void sakaplus$increaseExperience(
            CallbackInfoReturnable<Integer> cir
    ) {
        PlayerEntity player = this.attackingPlayer;

        if (player == null) return;

        ItemStack weapon = player.getMainHandStack();

        int level = EnchantmentHelper.getLevel(
                ModEnchantments.EXPERIENCE,
                weapon
        );

        if (level <= 0) return;

        int original = cir.getReturnValue();

        if (original <= 0) return;

        double multiplier = 1.0 + (0.25 * level);

        int increased = (int) Math.round(original * multiplier);

        cir.setReturnValue(increased);
    }
}
