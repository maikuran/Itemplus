
package com.sakalti.sakacountry.nation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Nation {

    public static final Codec<Nation> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("id")
                            .forGetter(Nation::getId),

                    Codec.STRING.fieldOf("name")
                            .forGetter(Nation::getName),

                    Codec.STRING.fieldOf("founder")
                            .forGetter(nation -> nation.founderUuid),

                    Codec.unboundedMap(Codec.STRING, Codec.INT)
                            .fieldOf("members")
                            .forGetter(Nation::getMembers)
            ).apply(instance, Nation::new));

    private final String id;
    private final String name;
    private final String founderUuid;

    private final Map<String, Integer> members;

    public Nation(
            String id,
            String name,
            String founderUuid,
            Map<String, Integer> members
    ) {
        this.id = id;
        this.name = name;
        this.founderUuid = founderUuid;
        this.members = new HashMap<>(members);
    }

    public static Nation create(String name, UUID founder) {
        String id = UUID.randomUUID().toString();

        Map<String, Integer> members = new HashMap<>();

        // 初期権限レベルは0
        members.put(founder.toString(), 0);

        return new Nation(
                id,
                name,
                founder.toString(),
                members
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFounderUuid() {
        return founderUuid;
    }

    public Map<String, Integer> getMembers() {
        return members;
    }

    public boolean isMember(UUID player) {
        return members.containsKey(player.toString());
    }

    public int getPermissionLevel(UUID player) {
        return members.getOrDefault(player.toString(), 0);
    }

    public void addMember(UUID player) {
        members.putIfAbsent(player.toString(), 0);
    }

    public void removeMember(UUID player) {
        members.remove(player.toString());
    }

    public int getMemberCount() {
        return members.size();
    }
}
