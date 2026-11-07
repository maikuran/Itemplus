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

    // Mixin仕様に適合する private static 定義
    @Unique
    private static final EnchantmentTarget SAKAPLUS$GREATSWORD;

    @Unique
    private static final EnchantmentTarget SAKAPLUS$GUN;

    static {
        int currentLength = EnchantmentTarget.values().length;
        // バニラの末尾に動的にスロットを追加
        SAKAPLUS$GREATSWORD = sakaplus$create("GREATSWORD", currentLength);
        SAKAPLUS$GUN = sakaplus$create("GUN", currentLength + 1);
    }

    // 外部からリフレクションで安全に引っ張るためのプライベートアクセス用メソッド
    @Unique
    private static EnchantmentTarget sakaplus$getGreatswordTarget() {
        return SAKAPLUS$GREATSWORD;
    }

    @Unique
    private static EnchantmentTarget sakaplus$getGunTarget() {
        return SAKAPLUS$GUN;
    }

    // Enumコンストラクタ呼び出し用のインボーカー
    @Invoker("<init>")
    public static EnchantmentTarget sakaplus$create(String name, int ordinal) {
        throw new AssertionError();
    }

    // ASMヌルポを完全に回避するため、難読化対応メソッド名「method_8177」を併記してヘッドインジェクションを確実に固定
    @Inject(method = "isAcceptableItem(Lnet/minecraft/item/Item;)Z", at = @At("HEAD"), cancellable = true, remap = true)
    private void sakaplus$customAcceptableItem(Item item, CallbackInfoReturnable<Boolean> cir) {
        Object current = (Object) this;
        if (current == SAKAPLUS$GREATSWORD) {
            cir.setReturnValue(item instanceof GreatswordItem);
        } else if (current == SAKAPLUS$GUN) {
            cir.setReturnValue(item instanceof Gun);
        }
    }
}
