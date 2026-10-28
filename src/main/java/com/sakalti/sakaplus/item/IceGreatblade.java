package com.sakalti.sakaplus.item;

import net.minecraft.item.SwordItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Rarity;
import com.sakalti.sakaplus.registry.ModTabs; // ModTabs のインポートを追加

public class IceGreatblade extends SwordItem {

    public IceGreatblade() {
        super(new ToolMaterial() {
            @Override
            public int getDurability() {
                return 2160;
            }

            @Override
            public float getMiningSpeedMultiplier() {
                return 9.0F;
            }

            @Override
            public float getAttackDamage() {
                return 22.5F - ToolMaterials.NETHERITE.getAttackDamage();
            }

            @Override
            public int getMiningLevel() {
                return ToolMaterials.NETHERITE.getMiningLevel();
            }

            @Override
            public int getEnchantability() {
                return 13;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return Ingredient.ofItems(Items.BLUE_ICE);
            }
        }, 3, -3.23F,
        new Item.Settings()
            .group(ModTabs.CYROVILIM_TAB) // Cyrovilim タブを指定
            .rarity(Rarity.EPIC)
            .maxCount(1)
            .maxDamage(2160));
    }

    @Override
    public boolean postHit(
            ItemStack stack,
            LivingEntity target,
            LivingEntity attacker
    ) {
        target.addStatusEffect(
            new StatusEffectInstance(
                StatusEffects.SLOWNESS,
                100,
                3
            )
        );

        stack.damage(
            1,
            attacker,
            e -> e.sendToolBreakStatus(attacker.getActiveHand())
        );

        return true;
    }

    @Override
    public boolean isFireproof() {
        return true;
    }
}
