package com.sakalti.sakaplus.effect;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public class ParalyzeHandler implements ClientModInitializer {

    private static boolean wasParalyzed = false;

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null) {
                wasParalyzed = false;
                return;
            }

            boolean paralyzed =
                client.player.hasStatusEffect(ModEffects.PARALYZE);

            if (paralyzed) {

                releaseAllKeys(client);

                wasParalyzed = true;

            } else if (wasParalyzed) {

                // 麻痺終了時に物理キーの状態を再取得
                KeyBinding.updateKeysByCode();

                wasParalyzed = false;
            }
        });
    }

    private static void releaseAllKeys(MinecraftClient client) {

        // 移動
        client.options.forwardKey.setPressed(false);
        client.options.backKey.setPressed(false);
        client.options.leftKey.setPressed(false);
        client.options.rightKey.setPressed(false);

        // ジャンプ・姿勢
        client.options.jumpKey.setPressed(false);
        client.options.sneakKey.setPressed(false);
        client.options.sprintKey.setPressed(false);

        // 攻撃・使用
        client.options.attackKey.setPressed(false);
        client.options.useKey.setPressed(false);

        // その他
        client.options.dropKey.setPressed(false);
        client.options.swapHandsKey.setPressed(false);
        client.options.inventoryKey.setPressed(false);
        client.options.chatKey.setPressed(false);
        client.options.playerListKey.setPressed(false);
        client.options.pickItemKey.setPressed(false);
    }
}
