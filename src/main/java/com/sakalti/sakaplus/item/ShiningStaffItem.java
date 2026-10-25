package com.sakalti.sakaplus.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;

public class ShiningStaffItem extends StaffItem {

    public ShiningStaffItem(Item.Settings settings) {
        super(
            settings.maxDamage(475),

            0,

            (world, player, hand) -> {

                // 火炎耐性 I：3分
                player.addStatusEffect(
                    new StatusEffectInstance(
                        StatusEffects.FIRE_RESISTANCE,
                        3600,
                        0
                    )
                );

                // 耐性 II：3分
                player.addStatusEffect(
                    new StatusEffectInstance(
                        StatusEffects.RESISTANCE,
                        3600,
                        1
                    )
                );

                // 吸収 IV：3分
                player.addStatusEffect(
                    new StatusEffectInstance(
                        StatusEffects.ABSORPTION,
                        3600,
                        3
                    )
                );

                // 移動速度低下 II：15秒
                player.addStatusEffect(
                    new StatusEffectInstance(
                        StatusEffects.SLOWNESS,
                        300,
                        1
                    )
                );
            },

            // 消費触媒
            new CatalystCost(
                CatalystItems.FIRE_CATALYST,
                2
            ),

            new CatalystCost(
                CatalystItems.FORCE_CATALYST,
                4
            )
        );
    }
}
