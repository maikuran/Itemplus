<!-- User Intent: Context: Fabric 1.19.2 Modding | Constraints: Keep mixin-based EnchantmentTarget definition, create separate target for Gun | Intent: Define separate SAKAPLUS$GREATSWORD and SAKAPLUS$GUN targets in the same Mixin -->
package com.sakalti.sakaplus.mixin;

import com.sakalti.sakaplus.item.GreatswordItem;
import com.sakalti.sakaplus.item.Gun; // 銃アイテム(item.Gun)をインポート
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

    // 大剣専用ターゲット
    @Unique
    public static final EnchantmentTarget SAKAPLUS$GREATSWORD;

    // 銃専用ターゲット
    @Unique
    public static final EnchantmentTarget SAKAPLUS$GUN;

    static {
        int currentLength = EnchantmentTarget.values().length;
        
        // 個別にインスタンスを生成（インデックスが被らないように注意）
        SAKAPLUS$GREATSWORD = sakaplus$create("GREATSWORD", currentLength);
        SAKAPLUS$GUN = sakaplus$create("GUN", currentLength + 1);
    }

    @Invoker("<init>")
    public static EnchantmentTarget sakaplus$create(String name, int ordinal) {
        throw new AssertionError();
    }

    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    private void sakaplus$customAcceptableItem(Item item, CallbackInfoReturnable<Boolean> cir) {
        // 大剣ターゲットの判定
        if ((Object) this == SAKAPLUS$GREATSWORD) {
            cir.setReturnValue(item instanceof GreatswordItem);
        }
        // 銃ターゲットの判定（個別に処理）
        else if ((Object) this == SAKAPLUS$GUN) {
            cir.setReturnValue(item instanceof Gun);
        }
    }
}
