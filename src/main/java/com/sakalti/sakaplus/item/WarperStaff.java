package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.entity.BeamAttack;
import com.sakalti.sakaplus.sound.SakaplusSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import java.util.Random;

public class WarperStaffItem extends StaffItem {

    private static final Random RANDOM = new Random();

    public WarperStaffItem(Item.Settings settings) {
        super(
            settings.maxDamage(500), // 耐久度は適宜調整してください

            0,

            (world, player, hand) -> {
                // クライアント側では処理しない（BeamAttack内でもガードされていますが念のため）
                if (world.isClient) return;

                // 基本ダメージは 16
                float damage = 16.0F;

                // 50%の確率でダメージを+8（合計24）にする例（確率は必要に応じて変更してください）
                if (RANDOM.nextBoolean()) {
                    damage += 8.0F;
                }

                // ビームの色（RGBを16進数intで指定。例として紫：0x9933FF）
                int beamColor = 0x9933FF;
                // ビームの速度
                float speed = 2.5F; 

                // プレイヤーの視線の先、または周囲のターゲットに撃つ処理
                // ここではプレイヤーの向いている方向に射出する「shoot」を使用
                BeamAttack.shoot(player, speed, damage, beamColor);

                // 使用音の再生
                world.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SakaplusSounds.StaffUse, // 必要に応じて適切な音に変更してください
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
                );
            },

            // 複数種類の触媒コストを定義（StaffItem側が複数コストに対応していると想定）
            new CatalystCost(CatalystItems.STORM_CATALYST, 2),
            new CatalystCost(CatalystItems.FORCE_CATALYST, 3)
        );
    }
}
