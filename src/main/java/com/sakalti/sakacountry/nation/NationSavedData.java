
package com.sakalti.sakacountry.nation;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class NationSavedData extends SavedData {

    private static final Codec<NationSavedData> CODEC =
            Nation.CODEC.listOf().xmap(
                    NationSavedData::new,
                    data -> new ArrayList<>(data.nations.values())
            );

    private static final SavedDataType<NationSavedData> TYPE =
            new SavedDataType<>(
                    ResourceLocation.fromNamespaceAndPath(
                            "sakacountry",
                            "nations"
                    ),
                    NationSavedData::new,
                    CODEC,
                    null
            );

    private final Map<String, Nation> nations =
            new LinkedHashMap<>();

    public NationSavedData() {
    }

    public NationSavedData(Collection<Nation> loadedNations) {
        for (Nation nation : loadedNations) {
            nations.put(nation.getId(), nation);
        }
    }

    public static NationSavedData get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    public Nation createNation(String name, UUID founder) {

        if (findByName(name) != null) {
            return null;
        }

        Nation nation = Nation.create(name, founder);

        nations.put(nation.getId(), nation);

        setDirty();

        return nation;
    }

    public Nation findByName(String name) {
        return nations.values().stream()
                .filter(nation ->
                        nation.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Nation findById(String id) {
        return nations.get(id);
    }

    public Nation findByMember(UUID player) {
        return nations.values().stream()
                .filter(nation -> nation.isMember(player))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteNation(String id) {
        if (nations.remove(id) == null) {
            return false;
        }

        setDirty();
        return true;
    }

    public Collection<Nation> getNations() {
        return nations.values();
    }

    public int getNationCount() {
        return nations.size();
    }

    public void markChanged() {
        setDirty();
    }
}
