
package com.sakalti.sakaplus.client.item;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public final class GunRecoil {

    private GunRecoil() {}

    // ==============================
    // Configurable Values
    // ==============================

    /**
     * リコイルの強さ（度数）
     * 大きくすると上方向への反動が強くなる
     */
    public static float value = 4.0F;

    /**
     * 1tickごとの復帰率
     * 大きいほど速く元の角度に戻る
     */
    public static float RECOVERY_SPEED = 0.18F;

    /**
     * リコイルの最大累積角度
     */
    public static float MAX_RECOIL = 18.0F;

    // ==============================
    // Internal State
    // ==============================

    private static float recoilOffset = 0.0F;
    private static float appliedOffset = 0.0F;

    /**
     * 発射時に呼び出す
     */
    public static void recoil() {

        recoilOffset -= value;

        // 連射時の過剰な反動を防止
        recoilOffset = Math.max(
            recoilOffset,
            -MAX_RECOIL
        );
    }

    /**
     * Client tick registration
     */
    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            PlayerEntity player = client.player;

            if (player == null || client.world == null) {
                recoilOffset = 0.0F;
                appliedOffset = 0.0F;
                return;
            }

            // Smooth recovery
            float nextOffset = recoilOffset
                    * (1.0F - RECOVERY_SPEED);

            // 現在の反動との差分だけ視点に適用
            float delta = nextOffset - appliedOffset;

            if (Math.abs(delta) > 0.0001F) {

                float newPitch = player.getPitch() + delta;

                // Minecraftの視点角度制限
                newPitch = Math.max(
                    -90.0F,
                    Math.min(90.0F, newPitch)
                );

                player.setPitch(newPitch);
            }

            appliedOffset = nextOffset;
            recoilOffset = nextOffset;

            // 微小な誤差を除去
            if (Math.abs(recoilOffset) < 0.001F) {
                recoilOffset = 0.0F;
                appliedOffset = 0.0F;
            }
        });
    }
}
