package com.sakalti.sakaplus.effect;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ParalyzeHandler implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null ||
                !client.player.hasStatusEffect(ModEffects.PARALYZE)) {
                return;
            }

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

            // その他の操作
            client.options.dropKey.setPressed(false);
            client.options.swapHandsKey.setPressed(false);
            client.options.inventoryKey.setPressed(false);
            client.options.chatKey.setPressed(false);
            client.options.playerListKey.setPressed(false);
            client.options.pickItemKey.setPressed(false);
        });
    }
}
