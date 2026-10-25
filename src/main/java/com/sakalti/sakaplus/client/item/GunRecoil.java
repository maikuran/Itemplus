package com.sakalti.sakaplus.client.item;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public final class GunRecoil {

    private GunRecoil() {}

    // ==============================
    // 設定値（お好みで調整してください）
    // ==============================
    
    // 1発あたりの跳ね上がりの強さ（度数）
    public static float RECOIL_FORCE = 3.5F;

    // 反動が元の位置に戻るスピード（大きいほど素早く戻る）
    public static final float RECOVERY_SPEED = 0.22F;

    // 累積する最大反動角度
    public static final float MAX_RECOIL = 15.0F;

    // ==============================
    // 内部状態（状態管理用）
    // ==============================
    
    private static float targetRecoil = 0.0F;
    private static float lastRecoil = 0.0F;
    private static float currentAppliedRecoil = 0.0F;

    /**
     * 銃を発射したときに呼び出す
     */
    public static void recoil() {
        targetRecoil -= RECOIL_FORCE;

        if (targetRecoil < -MAX_RECOIL) {
            targetRecoil = -MAX_RECOIL;
        }
    }

    /**
     * クライアント起動時（ClientModInitializerなど）に一度だけ呼び出す登録処理
     */
    public static void register() {
        
        // 1. 1秒間に20回実行されるロジック処理（反動の減衰管理）
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            PlayerEntity player = client.player;

            if (player == null || client.world == null || client.isPaused()) {
                targetRecoil = 0.0F;
                lastRecoil = 0.0F;
                currentAppliedRecoil = 0.0F;
                return;
            }

            // 前回のチックの値を保存（描画の補間計算用）
            lastRecoil = targetRecoil;

            // チックごとに元の位置へ滑らかに減衰させる
            if (Math.abs(targetRecoil) > 0.001F) {
                targetRecoil *= (1.0F - RECOVERY_SPEED);
            } else {
                targetRecoil = 0.0F;
                lastRecoil = 0.0F;
            }
        });

        // 2. 【Mixinの代わり】毎フレームの描画直前に呼び出されるイベント
        // これにより、画面の書き換え（fps）に合わせて極限まで滑らかにカメラを動かします
        WorldRenderEvents.START.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            PlayerEntity player = client.player;

            if (player == null || client.world == null || client.isPaused()) {
                return;
            }

            // 現在のフレームが、前のチックからどれくらい時間が経過しているか（0.0 ~ 1.0）を取得
            float tickDelta = context.tickDelta();

            // 前回のチックと今回のチックの間をフレームレートに合わせて滑らかに補間（Lerp）
            float visualRecoil = MathHelper.lerp(tickDelta, lastRecoil, targetRecoil);

            // 前のフレームで適用した古い反動分を一度リセットし、新しい滑らかな反動を適用する
            // これによりマウス操作と競合しなくなります
            float currentPitch = player.getPitch();
            float newPitch = currentPitch - currentAppliedRecoil + visualRecoil;

            // マイクラの限界角度（真上・真下）にクランプ
            newPitch = MathHelper.clamp(newPitch, -90.0F, 90.0F);

            // プレイヤーの視線角度を直接更新
            player.setPitch(newPitch);

            // 今回適用した反動の量を記憶（次のフレームで引き算してリセットするため）
            currentAppliedRecoil = visualRecoil;
        });
    }
}
