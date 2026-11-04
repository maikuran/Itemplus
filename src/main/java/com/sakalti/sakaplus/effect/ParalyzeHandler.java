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

                // 操作を無効化
                releaseAllKeys(client);

                wasParalyzed = true;

            } else if (wasParalyzed) {

                // 麻痺終了時にキー状態を復元
                restoreKeys(client);

                wasParalyzed = false;
            }
        });
    }

    private static void releaseAllKeys(MinecraftClient client) {

        client.options.forwardKey.setPressed(false);
        client.options.backKey.setPressed(false);
        client.options.leftKey.setPressed(false);
        client.options.rightKey.setPressed(false);

        client.options.jumpKey.setPressed(false);
        client.options.sneakKey.setPressed(false);
        client.options.sprintKey.setPressed(false);

        client.options.attackKey.setPressed(false);
        client.options.useKey.setPressed(false);

        client.options.dropKey.setPressed(false);
        client.options.swapHandsKey.setPressed(false);
        client.options.inventoryKey.setPressed(false);
        client.options.chatKey.setPressed(false);
        client.options.playerListKey.setPressed(false);
        client.options.pickItemKey.setPressed(false);
    }

    private static void restoreKeys(MinecraftClient client) {

        KeyBinding[] keys = {
            client.options.forwardKey,
            client.options.backKey,
            client.options.leftKey,
            client.options.rightKey,
            client.options.jumpKey,
            client.options.sneakKey,
            client.options.sprintKey,
            client.options.attackKey,
            client.options.useKey,
            client.options.dropKey,
            client.options.swapHandsKey,
            client.options.inventoryKey,
            client.options.chatKey,
            client.options.playerListKey,
            client.options.pickItemKey
        };

        for (KeyBinding key : keys) {
            KeyBinding.setKeyPressed(
                key.getBoundKey(),
                false
            );
        }

        KeyBinding.updateKeysByCode();
    }
}
