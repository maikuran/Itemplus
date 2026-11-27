package com.sakalti.thesolar.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

public final class SolarQuestKeybinds {
    private SolarQuestKeybinds() {}

    public static void register() {
        KeyMapping questJournal = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.thesolar.quests",
                InputConstants.KEY_J,
                KeyMapping.Category.MISC
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (questJournal.consumeClick()) {
                if (client.player != null && client.gui.screen() == null) {
                    client.gui.setScreen(new SolarQuestScreen());
                }
            }
        });
    }
}
