package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.entity.BeamAttack;
import com.sakalti.sakaplus.sound.SakaplusSounds;
import net.minecraft.entity.player.PlayerEntity; // 追加
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import java.util.Random;

public class WarperStaffItem extends StaffItem {

    private static final Random RANDOM = new Random();

    public WarperStaffItem(Item.Settings settings) {
        super(
            settings.maxDamage(1190),

            0,

            (world, player, hand) -> {
                if (world.isClient) return;

                // 1. クールダウン中なら処理を中断する（プレイヤーがPlayerEntityの場合）
                if (player instanceof PlayerEntity) {
                    PlayerEntity playerEntity = (PlayerEntity) player;
                    if (playerEntity.getItemCooldownManager().isCoolingDown(player.getMainHandStack().getItem())) {
                        return; 
                    }
                    // クールダウンを設定（例: 40チック = 2秒）
                    playerEntity.getItemCooldownManager().set(player.getMainHandStack().getItem(), 24);
                }

                float damage = 24.0F;
                if (RANDOM.nextBoolean()) {
                    damage += 8.0F;
                }

                int beamColor = 0x9933FF;
                float speed = 2.5F; 

                BeamAttack.shoot(player, speed, damage, beamColor);

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

            new CatalystCost(CatalystItems.STORM_CATALYST, 2),
            new CatalystCost(CatalystItems.FORCE_CATALYST, 3)
        );
    }
}
