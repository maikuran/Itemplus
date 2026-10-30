package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.sound.SakaplusSounds;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;

public class CryogenicStaff extends StaffItem {

    public CryogenicStaff(Item.Settings settings) {
        super(
            settings.maxDamage(550),

            24,

            (world, player, hand) -> {

                // 周囲のモブに鈍化VIを12秒付与
                if (!world.isClient) {

                    double radius = 12.0D;

                    Box area = player.getBoundingBox().expand(radius);

                    for (LivingEntity entity : world.getEntitiesByClass(
                        LivingEntity.class,
                        area,
                        entity -> entity.isAlive()
                            && entity != player
                            && !entity.isSpectator()
                    )) {

                        entity.addStatusEffect(
                            new StatusEffectInstance(
                                StatusEffects.SLOWNESS,
                                240,
                                5
                            ),
                            player
                        );
                    }
                }

                // 発動音
                world.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SakaplusSounds.StaffUse,
                    SoundCategory.PLAYERS,
                    1.0F,
                    0.8F
                );
            },

            new CatalystCost(
                CatalystItems.WATER_CATALYST,
                2
            ),

            new CatalystCost(
                CatalystItems.WIND_CATALYST,
                2
            )
        );
    }
}
