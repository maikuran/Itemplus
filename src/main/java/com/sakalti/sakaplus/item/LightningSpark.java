package com.sakalti.sakaplus.item;

import net.minecraft.item.SwordItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.util.ActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Rarity;
import net.minecraft.item.ToolMaterials;
import net.minecraft.entity.Entity;
import com.sakalti.sakaplus.registry.ModTabs; // ModTabs のインポートを追加

public class LightningSpark extends SwordItem {

    public LightningSpark() {
        super(new ToolMaterial() {
            @Override public int getDurability() { return 2185; }
            @Override public float getMiningSpeedMultiplier() { return 9.0F; }
            @Override public float getAttackDamage() { return 20.5F - ToolMaterials.NETHERITE.getAttackDamage(); } // relative boost
            @Override public int getMiningLevel() { return ToolMaterials.NETHERITE.getMiningLevel(); }
            @Override public int getEnchantability() { return 13; }
            @Override public Ingredient getRepairIngredient() {
                return Ingredient.ofItems(Items.NETHERITE_INGOT);
            }
        }, 3, -3.23F, new Item.Settings()
            .group(ModTabs.STARTER_AND_KRUZIVILIM_TAB) // Starter and Kruzivilim タブを指定
            .rarity(Rarity.EPIC)
            .maxCount(1)
            .maxDamage(2185));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(10);
        stack.damage(1, attacker, e -> e.sendToolBreakStatus(attacker.getActiveHand()));
        return true;
    }

    @Override
    public boolean isFireproof() {
        return true;
    }
}
