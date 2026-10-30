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
import com.sakalti.sakaplus.effect.ModEffects;
import net.minecraft.util.Rarity;
import com.sakalti.sakaplus.registry.ModTabs; // ModTabs のインポートを追加

public class BoneScytheItem extends SwordItem {

    public IceGreatblade() {
        super(new ToolMaterial() {
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
                return 17.5F - ToolMaterials.NETHERITE.getAttackDamage();
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
        }, 3, -3.1F,
        new Item.Settings()
            .group(ModTabs.STARTER_AND_KRUZIVILIM_TAB) // Cyrovilim タブを指定
            .rarity(Rarity.EPIC)
            .maxCount(1)
            .maxDamage(870));
    }

    @Override
    public boolean postHit(
            ItemStack stack,
            LivingEntity target,
            LivingEntity attacker
    ) {
        target.addStatusEffect(
            new StatusEffectInstance(
                Nk
                ModEffects.MUDO,
                100,
                1
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
