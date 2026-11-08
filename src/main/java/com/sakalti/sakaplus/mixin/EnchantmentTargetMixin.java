package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.enchant.MagnumEnchantment;
import com.sakalti.sakaplus.enchant.SweepBurstEnchantment;
import com.sakalti.sakaplus.item.GreatswordItem;
import com.sakalti.sakaplus.item.Gun;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {
    SweepBurstEnchantment.class,
    MagnumEnchantment.class
})
public abstract class EnchantmentTargetMixin {

    @Inject(
        method = "isAcceptableItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void sakaplus$restrictCustomEnchantmentItems(
        ItemStack stack,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (stack == null || stack.isEmpty()) {
            cir.setReturnValue(false);
            return;
        }

        Object enchantment = this;

        if (enchantment instanceof SweepBurstEnchantment) {
            cir.setReturnValue(stack.getItem() instanceof GreatswordItem);
            return;
        }

        if (enchantment instanceof MagnumEnchantment) {
            cir.setReturnValue(
                stack.getItem() instanceof Gun
                    || stack.isOf(Items.BOOK)
            );
        }
    }
}
