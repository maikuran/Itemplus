package com.sakalti.sakaplus.enchant;

import com.sakalti.sakaplus.item.Gun;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;

public class MagnumEnchantment extends Enchantment {

    public MagnumEnchantment() {
        super(
                Rarity.RARE,
                // 💡 BREAKABLE から、銃（Gun）のみを許可するカスタムターゲットに変更
                CUSTOM_GUN_TARGET,
                new EquipmentSlot[] {
                        EquipmentSlot.MAINHAND,
                        EquipmentSlot.OFFHAND
                }
        );
    }

    // 💡 銃（Gun）だけを許可するカスタムEnchantmentTargetの定義
    // (先ほど作成した EnchantmentTargetMixin をそのまま活用できます)
    private static final EnchantmentTarget CUSTOM_GUN_TARGET = EnchantmentTargets.create("GUN", item -> item instanceof Gun);

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinPower(int level) {
        return 1 + (level - 1) * 10;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 50;
    }

    // かなどこ等での最終チェック（本への付与もここで明示的に許可）
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof Gun || stack.isOf(Items.BOOK);
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return true;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }
}
