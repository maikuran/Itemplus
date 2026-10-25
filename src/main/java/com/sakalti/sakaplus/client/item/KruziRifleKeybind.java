
package com.sakalti.sakaplus.client.item;

import com.sakalti.sakaplus.item.KruziRifle;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import org.lwjgl.glfw.GLFW;

public class KruziRifleKeybind {

    private static KeyBinding reloadKey;

    public static void register() {

        reloadKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.sakaplus.kruzi_rifle_reload",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_J,
                        "category.sakaplus.controls"
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (reloadKey.wasPressed()) {

                if (client.player == null || client.world == null) {
                    continue;
                }

                if (client.currentScreen != null) {
                    continue;
                }

                if (client.player.getMainHandStack().getItem()
                        instanceof KruziRifle
                        || client.player.getOffHandStack().getItem()
                        instanceof KruziRifle) {

                    ClientPlayNetworking.send(
                            KruziRifle.RELOAD_PACKET,
                            PacketByteBufs.empty()
                    );
                }
            }
        });
    }
}
