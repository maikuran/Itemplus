package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.item.GreatswordItem;
import com.sakalti.sakaplus.item.Gun;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentTarget.class)
public abstract class EnchantmentTargetMixin {

    @Unique
    private static final EnchantmentTarget SAKAPLUS$GREATSWORD;

    @Unique
    private static final EnchantmentTarget SAKAPLUS$GUN;

    static {
        int currentLength = EnchantmentTarget.values().length;
        SAKAPLUS$GREATSWORD = sakaplus$create("GREATSWORD", currentLength);
        SAKAPLUS$GUN = sakaplus$create("GUN", currentLength + 1);
    }

    // Mixinの規則に従い、private static に変更
    @Unique
    private static EnchantmentTarget sakaplus$getGreatswordTarget() {
        return SAKAPLUS$GREATSWORD;
    }

    // Mixinの規則に従い、private static に変更
    @Unique
    private static EnchantmentTarget sakaplus$getGunTarget() {
        return SAKAPLUS$GUN;
    }

    @Invoker("<init>")
    public static EnchantmentTarget sakaplus$create(String name, int ordinal) {
        throw new AssertionError();
    }

    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    private void sakaplus$customAcceptableItem(Item item, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this == SAKAPLUS$GREATSWORD) {
            cir.setReturnValue(item instanceof GreatswordItem);
        } else if ((Object) this == SAKAPLUS$GUN) {
            cir.setReturnValue(item instanceof Gun);
        }
    }
}
