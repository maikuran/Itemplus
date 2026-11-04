package com.sakalti.sakaplus.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Rarity;

import com.sakalti.sakaplus.registry.ModTabs;

public class LightningSpark extends GreatswordItem {

    private static final ToolMaterial LIGHTNING_MATERIAL = new ToolMaterial() {

        @Override
        public int getDurability() {
            return 2185;
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 9.0F;
        }

        @Override
        public float getAttackDamage() {
            return 20.5F - ToolMaterials.NETHERITE.getAttackDamage();
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
            return Ingredient.ofItems(Items.NETHERITE_INGOT);
        }
    };

    public LightningSpark() {
        super(
            LIGHTNING_MATERIAL,
            3,
            -3.23F,
            new Item.Settings()
                .group(ModTabs.STARTER_AND_KRUZIVILIM_TAB)
                .rarity(Rarity.EPIC)
                .maxCount(1)
                .maxDamage(2185)
        );
    }

    @Override
    public boolean postHit(
            ItemStack stack,
            LivingEntity target,
            LivingEntity attacker
    ) {
        target.setFireTicks(200);

        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isFireproof() {
        return true;
    }
}
