package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.item.GreatswordItem;
import com.sakalti.sakaplus.item.Gun;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * クラス名は変更せず、中身の標的をバニラの共通エンチャント処理(Enchantment.class)に変更。
 * これにより、あの忌々しい Enum の解析エラー(insnNode is null)を完全に回避します。
 */
@Mixin(Enchantment.class)
public abstract class EnchantmentTargetMixin {

    /**
     * すべてのエンチャント判定の根本(isAcceptableItem)の先頭(HEAD)に強引に割り込みます。
     * あなたが作成した各カスタムエンチャントのクラス名に応じて、
     * 大剣や銃以外には絶対に付与できないようにルールを強制上書きします。
     */
    @Inject(
        method = {"isAcceptableItem", "method_8177"},
        at = @At("HEAD"),
        cancellable = true
    )
    private void sakaplus$forceWeaponEnchants(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack == null || stack.isEmpty()) return;

        Object enchantment = (Object) this;
        String className = enchantment.getClass().getSimpleName();

        // 1. クラス名に「Greatsword」または「GreatSword」が含まれるエンチャントの場合
        if (className.contains("Greatsword") || className.contains("GreatSword")) {
            cir.setReturnValue(stack.getItem() instanceof GreatswordItem);
        }
        
        // 2. クラス名に「Gun」が含まれるエンチャントの場合
        else if (className.contains("Gun")) {
            cir.setReturnValue(stack.getItem() instanceof Gun);
        }
    }
}
