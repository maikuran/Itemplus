package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.sound.SakaplusSounds;
import com.sakalti.sakaplus.effect.ModEffects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;

public class AdaptationStaffItem extends StaffItem {

    public AdaptationStaffItem(Item.Settings settings) {
        super(
            settings.maxDamage(409),

            0,

            (world, player, hand) -> {

                // 火炎耐性 I：3分
    

                // 耐性 II：3分
                player.addStatusEffect(
                    new StatusEffectInstance(
                        StatusEffects.RESISTANCE,
                        3600,
                        3
                    )
                );

                // 吸収 IV：3分
                player.addStatusEffect(
                    new StatusEffectInstance(
                        ModEffects.ADAPT,
                        3600,
                        3
                    )
                );



                // 使用音
                world.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SakaplusSounds.StaffUse,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
                );
            },


            new CatalystCost(
                CatalystItems.FORCE_CATALYST,
                7
            )
        );
    }
}
