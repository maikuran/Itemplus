package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.sound.SakaplusSounds;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;

public class ShiningStaffItem extends StaffItem {

    public ShiningStaffItem(Item.Settings settings) {
        super(
            settings.maxDamage(475),

            24,

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
