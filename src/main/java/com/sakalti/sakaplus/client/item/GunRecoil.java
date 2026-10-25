
package com.sakalti.sakaplus.client.item;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerEntity;

public final class GunRecoil {

    private GunRecoil() {}

    // =========================================================
    // DEFAULT CONFIGURATION
    // =========================================================

    /**
     * デフォルトの反動の強さ
     */
    public static float value = 4.0F;

    /**
     * デフォルトの復帰速度
     */
    public static float RECOVERY_SPEED = 0.18F;

    /**
     * デフォルトの最大反動角度
     */
    public static float MAX_RECOIL = 18.0F;

    // =========================================================
    // INTERNAL STATE
    // =========================================================

    /**
     * 現在の累積反動
     */
    private static float recoilOffset = 0.0F;

    /**
     * 現在適用されている反動
     */
    private static float appliedOffset = 0.0F;

    /**
     * 現在使用する復帰速度
     */
    private static float activeRecoverySpeed = RECOVERY_SPEED;

    /**
     * 現在使用する最大反動角度
     */
    private static float activeMaxRecoil = MAX_RECOIL;

    // =========================================================
    // DEFAULT RECOIL
    // =========================================================

    /**
     * デフォルト設定で反動を発生させる
     */
    public static void recoil() {

        recoil(
                value,
                RECOVERY_SPEED,
                MAX_RECOIL
        );
    }

    // =========================================================
    // CUSTOM RECOIL
    // =========================================================

    /**
     * カスタム設定で反動を発生させる
     *
     * @param strength 反動の強さ
     * @param recoverySpeed 復帰速度 (0.0 ～ 1.0)
     * @param maxRecoil 最大反動角度
     */
    public static void recoil(
            float strength,
            float recoverySpeed,
            float maxRecoil
    ) {

        // 不正な値を防止
        strength = Math.max(0.0F, strength);

        recoverySpeed = Math.max(
                0.0F,
                Math.min(1.0F, recoverySpeed)
        );

        maxRecoil = Math.max(0.0F, maxRecoil);

        // カスタム設定を適用
        activeRecoverySpeed = recoverySpeed;
        activeMaxRecoil = maxRecoil;

        // 反動を追加
        recoilOffset -= strength;

        // 最大反動角度を制限
        recoilOffset = Math.max(
                recoilOffset,
                -activeMaxRecoil
        );
    }

    // =========================================================
    // CLIENT TICK REGISTRATION
    // =========================================================

    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            PlayerEntity player = client.player;

            // =================================================
            // PLAYER CHECK
            // =================================================

            if (player == null || client.world == null) {

                recoilOffset = 0.0F;
                appliedOffset = 0.0F;

                activeRecoverySpeed = RECOVERY_SPEED;
                activeMaxRecoil = MAX_RECOIL;

                return;
            }

            // =================================================
            // SMOOTH RECOVERY
            // =================================================

            float nextOffset = recoilOffset
                    * (1.0F - activeRecoverySpeed);

            // 現在の反動との差分
            float delta = nextOffset - appliedOffset;

            // =================================================
            // APPLY CAMERA ROTATION
            // =================================================

            if (Math.abs(delta) > 0.0001F) {

                float newPitch = player.getPitch() + delta;

                // Minecraftの視点角度制限
                newPitch = Math.max(
                        -90.0F,
                        Math.min(
                                90.0F,
                                newPitch
                        )
                );

                player.setPitch(newPitch);
            }

            // =================================================
            // UPDATE STATE
            // =================================================

            appliedOffset = nextOffset;
            recoilOffset = nextOffset;

            // =================================================
            // REMOVE SMALL ERRORS
            // =================================================

            if (Math.abs(recoilOffset) < 0.001F) {

                recoilOffset = 0.0F;
                appliedOffset = 0.0F;
            }
        });
    }

    // =========================================================
    // RESET
    // =========================================================

    public static void reset() {

        recoilOffset = 0.0F;
        appliedOffset = 0.0F;
    }
}
