package com.sakalti.sakaplus.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import com.sakalti.sakaplus.item.IceGreatblade;
import com.sakalti.sakaplus.item.LightningSpark;
import com.sakalti.sakaplus.item.BoneScytheItem;

public class SweepBurstEnchantment extends Enchantment {
    public SweepBurstEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    
    public int getMaxLevel() {
        return 5;
    }

    // ★大剣専用の条件
    
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof IceGreatblade 
            || stack.getItem() instanceof LightningSpark 
            || stack.getItem() instanceof BoneScytheItem;
    }

    // ★エンチャント側でダメージをアップさせる設定（override禁止に対応）
    // レベル1で+1.5、レベル2で+3.0、レベル3で+4.5 の攻撃力が自動で加算されます
    
    public float getAttackDamage(int level, EntityGroup group) {
        return level * 1.5F;
    }
}
