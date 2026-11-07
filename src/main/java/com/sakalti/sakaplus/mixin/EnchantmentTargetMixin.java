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
        // 強引にバニラのEnum配列の末尾のインデックスを取得して拡張
        int currentLength = EnchantmentTarget.values().length;
        SAKAPLUS$GREATSWORD = sakaplus$create("GREATSWORD", currentLength);
        SAKAPLUS$GUN = sakaplus$create("GUN", currentLength + 1);
    }

    @Invoker("<init>")
    public static EnchantmentTarget sakaplus$create(String name, int ordinal) {
        throw new AssertionError();
    }

    /**
     * 【強引上書き仕様】
     * ターゲットのインデックス（insnNode）を探すからヌルポで落ちるバグへの対策。
     * メソッドの「最先端（HEAD）」で、すべての処理が行われる前に割って入ります。
     * method_8177 と isAcceptableItem の両方を指定し、
     * シグネチャ（引数型）をあえて書かないことで、ASMのパース失敗を強制回避します。
     */
    @Inject(
        method = {"isAcceptableItem", "method_8177"},
        at = @At("HEAD"),
        cancellable = true
    )
    private void sakaplus$forceAcceptableItem(Item item, CallbackInfoReturnable<Boolean> cir) {
        // 念のため、初期化前に呼ばれた場合のヌルポ防止ガード
        if (item == null) return;

        Object current = (Object) this;

        // 自分の拡張Enum要素だった場合は、バニラの処理を完全に無視して結果を強制返却
        if (current == SAKAPLUS$GREATSWORD) {
            cir.setReturnValue(item instanceof GreatswordItem);
        } else if (current == SAKAPLUS$GUN) {
            cir.setReturnValue(item instanceof Gun);
        }
    }
}
