
package com.sakalti.sakacountry.mixin;

import com.sakalti.sakacountry.item.weapon.CutlassItem;

import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CombatRules.class)
public class CutlassArmorDamageMixin {

    @Inject(
        method = "getDamageAfterAbsorb",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void sakacountry$increaseCutlassDamage(
        LivingEntity victim,
        float damage,
        DamageSource source,
        float totalArmor,
        float armorToughness,
        CallbackInfoReturnable<Float> cir
    ) {
        if (totalArmor <= 0.0F) {
            return;
        }

        ItemStack weapon = source.getWeaponItem();

        if (weapon == null || weapon.isEmpty()) {
            return;
        }

        if (weapon.getItem() instanceof CutlassItem) {
            cir.setReturnValue(
                cir.getReturnValue() * 1.20F
            );
        }
    }
}
