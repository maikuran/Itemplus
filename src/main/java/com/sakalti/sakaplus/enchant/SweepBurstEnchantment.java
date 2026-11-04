package com.sakalti.sakaplus.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;
import com.sakalti.sakaplus.item.IceGreatblade;
import com.sakalti.sakaplus.item.LightningSpark;
import com.sakalti.sakaplus.item.BoneScytheItem;

public class SweepBurstEnchantment extends Enchantment {
    public SweepBurstEnchantment() {
        // EnchantmentTarget.WEAPON にすることで、エンチャントテーブルの抽選対象になります
        super(Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public int getMaxLevel() {
        return 5;
    }

    // テーブルや金床で「3種の大剣」または「本」に対して付与できるようにします
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof IceGreatblade 
            || stack.getItem() instanceof LightningSpark 
            || stack.getItem() instanceof BoneScytheItem
            || stack.isOf(Items.BOOK); // ★本への付与を許可（これでエンチャント本が作れます）
    }

    // エンチャント側での自動ダメージアップ（レベル5で最大 +7.5 ダメージ）
    public float getAttackDamage(int level, EntityGroup group) {
        return level * 1.5F;
    }

    // ★エンチャントテーブルのランダム抽選プールに出現させるための設定
    public boolean isAvailableForRandomSelection() {
        return true;
    }

    // ★村人の交易やチェストから「エンチャント本」として出現させるための設定
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }
}
