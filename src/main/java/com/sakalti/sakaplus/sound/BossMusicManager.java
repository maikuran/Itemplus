
package com.sakalti.sakaplus.sound;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossMusicManager {

    private static final Map<UUID, String> ACTIVE_MUSIC = new HashMap<>();

    private static final String[] BOSS_IDS = {
            "sakaplus:warperdun",
            "sakaplus:cryopteryx",
            "sakaplus:volcanic_king"
    };

    private static final String[] MUSIC_IDS = {
            "sakaplus:warperdunbgm",
            "sakaplus:cryopteryxbgm",
            "sakaplus:volcanickingbgm"
    };

    private static final String[] SOUND_NAMES = {
            "warperdunbgm",
            "cryopteryxbgm",
            "volcanickingbgm"
    };

    private BossMusicManager() {
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(
                BossMusicManager::tick
        );
    }

    private static void tick(MinecraftServer server) {

        for (ServerPlayerEntity player :
                server.getPlayerManager().getPlayerList()) {

            String detectedMusic = null;

            for (int i = 0; i < BOSS_IDS.length; i++) {

                Identifier bossId = new Identifier(BOSS_IDS[i]);

                if (!Registry.ENTITY_TYPE.containsId(bossId)) {
                    continue;
                }

                boolean found = false;

                for (Entity entity :
                        player.getWorld().getEntitiesByType(
                                Registry.ENTITY_TYPE.get(bossId),
                                Entity::isAlive,
                                java.util.Collections.emptyList()
                        )) {

                    found = true;
                    break;
                }

                if (found) {
                    detectedMusic = MUSIC_IDS[i];
                    break;
                }
            }

            String previousMusic =
                    ACTIVE_MUSIC.get(player.getUuid());

            if (java.util.Objects.equals(
                    previousMusic, detectedMusic)) {
                continue;
            }

            if (previousMusic != null) {
                stopMusic(player, previousMusic);
            }

            if (detectedMusic != null) {
                playMusic(player, detectedMusic);
                ACTIVE_MUSIC.put(
                        player.getUuid(),
                        detectedMusic
                );
            } else {
                ACTIVE_MUSIC.remove(player.getUuid());
            }
        }

        ACTIVE_MUSIC.keySet().removeIf(uuid ->
                server.getPlayerManager().getPlayer(uuid) == null
        );
    }

    private static void playMusic(
            ServerPlayerEntity player,
            String musicId
    ) {
        Identifier id = new Identifier(musicId);

        if (!Registry.SOUND_EVENT.containsId(id)) {
            return;
        }

        player.playSound(
                Registry.SOUND_EVENT.get(id),
                SoundCategory.MUSIC,
                1.0F,
                1.0F
        );
    }

    private static void stopMusic(
            ServerPlayerEntity player,
            String musicId
    ) {
        player.getServerWorld().getServer()
                .getCommandManager()
                .executeWithPrefix(
                        player.getCommandSource(),
                        "stopsound @s music " + musicId
                );
    }
}
