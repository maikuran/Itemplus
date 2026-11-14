package com.sakalti.sakaplus.item;

import net.minecraft.recipe.Ingredient;
import net.minecraft.item.Items;
import com.sakalti.sakaplus.effect.ModEffects;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Rarity;

import com.sakalti.sakaplus.registry.ModTabs;

public class VolcanicGreatblade extends GreatswordItem {

    private static final ToolMaterial VOLCANIC_MATERIAL = new ToolMaterial() {

        @Override
        public int getDurability() {
            return 2285;
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 9.0F;
        }

        @Override
        public float getAttackDamage() {
            return 24.75F - ToolMaterials.NETHERITE.getAttackDamage();
        }

        @Override
        public int getMiningLevel() {
            return ToolMaterials.NETHERITE.getMiningLevel();
        }

        @Override
        public int getEnchantability() {
            return 19;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(Items.MAGMA_CREAM);
        }
    };

    public VolcanicGreatblade() {
        super(
            VOLCANIC_MATERIAL,
            3,
            -3.2F,
            new Item.Settings()
                .group(ModTabs.VOLCANOS_TAB)
                .rarity(Rarity.EPIC)
                .maxCount(1)
                .maxDamage(2285)
        );
    }

    @Override
    public boolean postHit(
            ItemStack stack,
            LivingEntity target,
            LivingEntity attacker
    ) {
        target.addStatusEffect(
            new StatusEffectInstance(
                ModEffects.CONFUSION,
                100,
                3
            )
        );

        target.addStatusEffect(
            new StatusEffectInstance(
                ModEffects.PARALYZE,
                100,
                1
            )
        );

        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isFireproof() {
        return true;
    }
}
