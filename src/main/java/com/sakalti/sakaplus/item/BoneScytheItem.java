package com.sakalti.sakaplus.item;

import net.minecraft.recipe.Ingredient;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Rarity;

import com.sakalti.sakaplus.effect.ModEffects;
import com.sakalti.sakaplus.registry.ModTabs;

public class BoneScytheItem extends GreatswordItem {

    private static final ToolMaterial BONE_SCYTHE_MATERIAL = new ToolMaterial() {

        @Override
        public int getDurability() {
            return 870;
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 9.0F;
        }

        @Override
        public float getAttackDamage() {
            return 13.5F - ToolMaterials.NETHERITE.getAttackDamage();
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
            return Ingredient.ofItems(Items.BONE);
        }
    };

    public BoneScytheItem() {
        super(
            BONE_SCYTHE_MATERIAL,
            3,
            -3.1F,
            new Item.Settings()
                .group(ModTabs.STARTER_AND_KRUZIVILIM_TAB)
                .rarity(Rarity.EPIC)
                .maxCount(1)
                .maxDamage(870)
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
                ModEffects.MUDO,
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
