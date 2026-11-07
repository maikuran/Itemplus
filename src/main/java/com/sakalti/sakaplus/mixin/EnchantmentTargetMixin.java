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

    @Invoker("<init>")
    public static EnchantmentTarget sakaplus$create(String name, int ordinal) {
        throw new AssertionError();
    }

    /**
     * 修正ポイント: 
     * シグネチャ付きの method 指定がASMのパースに失敗していたため、
     * Yarn名と中間マッピング名(method_8177)を両方カバーする指定に変更。
     * これにより "insnNode is null"（ターゲット見失いによるヌルポ）を回避します。
     */
    @Inject(
        method = {"isAcceptableItem", "method_8177"}, 
        at = @At("HEAD"), 
        cancellable = true
    )
    private void sakaplus$customAcceptableItem(Item item, CallbackInfoReturnable<Boolean> cir) {
        Object current = (Object) this;
        if (current == SAKAPLUS$GREATSWORD) {
            cir.setReturnValue(item instanceof GreatswordItem);
        } else if (current == SAKAPLUS$GUN) {
            cir.setReturnValue(item instanceof Gun);
        }
    }
}
