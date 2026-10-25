package com.sakalti.sakaplus.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;

public class ShiningStaffItem extends StaffItem {

    public ShiningStaffItem(Item.Settings settings) {
        super(
            settings,
            0, // クールダウン（tick）

            (world, player, hand) -> {

                // 3分間の効果（3600 tick）
                player.addStatusEffect(
                    new StatusEffectInstance(
                        StatusEffects.FIRE_RESISTANCE,
                        3600,
                        0
                    )
                );

                // 耐性 II
                player.addStatusEffect(
                    new StatusEffectInstance(
                        StatusEffects.RESISTANCE,
                        3600,
                        1
                    )
                );

                // 衝撃吸収 IV
                player.addStatusEffect(
                    new StatusEffectInstance(
                        StatusEffects.ABSORPTION,
                        3600,
                        3
                    )
                );

                // 鈍化 II（15秒）
                player.addStatusEffect(
                    new StatusEffectInstance(
                        StatusEffects.SLOWNESS,
                        300,
                        1
                    )
                );
            },

            // 必要な触媒
            new CatalystCost(CatalystItems.FIRE_CATALYST, 2),
            new CatalystCost(CatalystItems.FORCE_CATALYST, 4)
        );
    }
}
