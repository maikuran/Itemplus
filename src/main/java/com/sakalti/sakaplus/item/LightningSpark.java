package com.sakalti.sakaplus.item;

import net.minecraft.item.SwordItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Rarity;
import com.sakalti.sakaplus.registry.ModTabs;

public class LightningSpark extends SwordItem {

    public LightningSpark() {
        super(new ToolMaterial() {
            @Override public int getDurability() { return 2185; }
            @Override public float getMiningSpeedMultiplier() { return 9.0F; }
            @Override public float getAttackDamage() { return 20.5F - ToolMaterials.NETHERITE.getAttackDamage(); }
            @Override public int getMiningLevel() { return ToolMaterials.NETHERITE.getMiningLevel(); }
            @Override public int getEnchantability() { return 13; }
            @Override public Ingredient getRepairIngredient() {
                return Ingredient.ofItems(Items.NETHERITE_INGOT);
            }
        }, 3, -3.23F, new Item.Settings()
            .group(ModTabs.STARTER_AND_KRUZIVILIM_TAB) // 1.19.2 では group() が有効
            .rarity(Rarity.EPIC)
            .maxCount(1)
            .maxDamage(2185));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // 1.19.2 では setOnFireFor() ではなく setFireTicks() を使用します
        // 10秒 = 200 ticks
        target.setFireTicks(200);

        // ツールが壊れた際のパケット送信を含んだ耐久値減少処理
        stack.damage(1, attacker, e -> e.sendToolBreakStatus(attacker.getActiveHand()));
        return true;
    }

    @Override
    public boolean isFireproof() {
        return true;
    }
}
