
package com.sakalti.sakaplus.player.skill;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class WalkSkillLogic {
    public static final Identifier REQUEST =
        new Identifier("sakaplus", "walk_skill_request");
    public static final Identifier SYNC =
        new Identifier("sakaplus", "walk_skill_sync");
    public static final Identifier DOUBLE_JUMP =
        new Identifier("sakaplus", "walk_double_jump");
    public static final Identifier AIR_DASH =
        new Identifier("sakaplus", "walk_air_dash");

    private static final String STATE_ID = "sakaplus_walk_skill";

    public static final String[] STAGES = {
        "Double Jump", "Light Feet", "High Jumper", "Soft Landing",
        "Water Runner", "Aqua Breathing", "Wall Climber", "Air Dash",
        "Swift Runner", "Sprint Haste", "Sky Walker", "Long Stride",
        "Night Walker", "Moving Recovery", "Super Jump",
        "Wind Runner", "Flame Walker", "Aqua Master",
        "Blink Step", "Limitless Movement"
    };

    public static final String[] DESCRIPTIONS = {
        "空中でもう一度ジャンプできる",
        "移動速度が少し上昇する",
        "ジャンプ力が上昇する",
        "落下中に落下速度が低下する",
        "水中で移動速度が上昇する",
        "水中呼吸ができる",
        "壁に向かってしゃがむと壁を登れる",
        "空中で専用キーを押すと前方へダッシュ",
        "移動速度がさらに上昇する",
        "走っている間、採掘速度が上昇する",
        "空中で落下速度が低下する",
        "歩行中に少しずつ回復する",
        "暗視を獲得する",
        "歩行中に満腹度を少し回復する",
        "ジャンプ力がさらに上昇する",
        "移動速度が大きく上昇する",
        "走行中に火炎耐性を得る",
        "水中移動速度がさらに上昇する",
        "専用キーで前方へ短距離移動する",
        "移動中に速度・跳躍・落下耐性を得る"
    };

    private static final Map<UUID, Integer> lastWalkStat = new HashMap<>();
    private static final Map<UUID, Integer> jumpCooldown = new HashMap<>();
    private static final Map<UUID, Integer> dashCooldown = new HashMap<>();

    private WalkSkillLogic() {}

    public static void initialize() {
        ServerPlayNetworking.registerGlobalReceiver(
            REQUEST,
            (server, player, handler, buf, responseSender) ->
                server.execute(() -> sync(player))
        );

        ServerPlayNetworking.registerGlobalReceiver(
            DOUBLE_JUMP,
            (server, player, handler, buf, responseSender) ->
                server.execute(() -> doDoubleJump(player))
        );

        ServerPlayNetworking.registerGlobalReceiver(
            AIR_DASH,
            (server, player, handler, buf, responseSender) ->
                server.execute(() -> doAirDash(player))
        );

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player :
                server.getPlayerManager().getPlayerList()) {
                tickPlayer(player);
            }
        });
    }

    private static void tickPlayer(ServerPlayerEntity player) {
        UUID uuid = player.getUuid();
        int level = getLevel(player);

        // 歩行距離から経験値を獲得
        int currentWalk = player.getStatHandler().getStat(
            Stats.CUSTOM,
            Stats.WALK_ONE_CM
        );

        int previousWalk = lastWalkStat.getOrDefault(uuid, currentWalk);
        int difference = Math.max(0, currentWalk - previousWalk);
        lastWalkStat.put(uuid, currentWalk);

        if (difference >= 100) {
            addXp(player, difference / 100);
        }

        // クールダウン
        jumpCooldown.computeIfPresent(
            uuid,
            (key, value) -> value > 0 ? value - 1 : null
        );

        dashCooldown.computeIfPresent(
            uuid,
            (key, value) -> value > 0 ? value - 1 : null
        );

        boolean moving =
            player.getVelocity().horizontalLengthSquared() > 0.0025;

        // 2: Light Feet
        if (level >= 10) {
            effect(player, StatusEffects.SPEED, 40, 0);
        }

        // 3: High Jumper
        if (level >= 15) {
            effect(player, StatusEffects.JUMP_BOOST, 40, 0);
        }

        // 4: Soft Landing
        if (level >= 20 && player.fallDistance > 2.0F) {
            effect(player, StatusEffects.SLOW_FALLING, 30, 0);
        }

        // 5: Water Runner
        if (level >= 25 && player.isTouchingWater()) {
            effect(player, StatusEffects.DOLPHINS_GRACE, 40, 0);
        }

        // 6: Aqua Breathing
        if (level >= 30 && player.isTouchingWater()) {
            effect(player, StatusEffects.WATER_BREATHING, 40, 0);
        }

        // 7: Wall Climber
        if (
            level >= 35 &&
            player.isSneaking() &&
            player.horizontalCollision
        ) {
            player.setVelocity(
                player.getVelocity().x,
                Math.max(player.getVelocity().y, 0.20),
                player.getVelocity().z
            );

            player.velocityModified = true;
        }

        // 9: Swift Runner
        if (level >= 45 && moving) {
            effect(player, StatusEffects.SPEED, 40, 1);
        }

        // 10: Sprint Haste
        if (level >= 50 && player.isSprinting()) {
            effect(player, StatusEffects.HASTE, 40, 0);
        }

        // 11: Sky Walker
        if (level >= 55 && !player.isOnGround()) {
            effect(player, StatusEffects.SLOW_FALLING, 30, 0);
        }

        // 12: Long Stride - 移動中の回復
        if (level >= 60 && moving && player.age % 60 == 0) {
            player.heal(1.0F);
        }

        // 13: Night Walker
        if (level >= 65) {
            effect(player, StatusEffects.NIGHT_VISION, 220, 0);
        }

        // 14: Moving Recovery - 移動中に満腹度回復
        if (level >= 70 && moving && player.age % 100 == 0) {
            player.getHungerManager().setFoodLevel(
                Math.min(
                    20,
                    player.getHungerManager().getFoodLevel() + 1
                )
            );
        }

        // 15: Super Jump
        if (level >= 75) {
            effect(player, StatusEffects.JUMP_BOOST, 40, 1);
        }

        // 16: Wind Runner
        if (level >= 80) {
            effect(player, StatusEffects.SPEED, 40, 2);
        }

        // 17: Flame Walker
        if (level >= 85 && player.isSprinting()) {
            effect(player, StatusEffects.FIRE_RESISTANCE, 60, 0);
        }

        // 18: Aqua Master
        if (level >= 90 && player.isTouchingWater()) {
            effect(player, StatusEffects.DOLPHINS_GRACE, 60, 1);
        }

        // 20: Limitless Movement
        if (level >= 100 && moving) {
            effect(player, StatusEffects.SPEED, 40, 3);
            effect(player, StatusEffects.JUMP_BOOST, 40, 2);

            if (!player.isOnGround()) {
                effect(player, StatusEffects.SLOW_FALLING, 40, 0);
            }
        }

        if (player.age % 40 == 0) {
            sync(player);
        }
    }

    private static void doDoubleJump(ServerPlayerEntity player) {
        if (getLevel(player) < 5) return;
        if (
            player.isOnGround() ||
            player.isClimbing() ||
            player.isFallFlying()
        ) return;

        if (jumpCooldown.getOrDefault(player.getUuid(), 0) > 0) return;

        player.setVelocity(
            player.getVelocity().x,
            0.48,
            player.getVelocity().z
        );

        player.velocityModified = true;
        player.fallDistance = 0;
        jumpCooldown.put(player.getUuid(), 12);
    }

    private static void doAirDash(ServerPlayerEntity player) {
        int level = getLevel(player);

        if (level < 40) return;
        if (player.isOnGround() || player.isFallFlying()) return;

        if (dashCooldown.getOrDefault(player.getUuid(), 0) > 0) return;

        double power =
            level >= 95 ? 1.8 :
            level >= 80 ? 1.4 : 1.0;

        player.setVelocity(
            player.getRotationVector().x * power,
            Math.max(player.getVelocity().y, 0.05),
            player.getRotationVector().z * power
        );

        player.velocityModified = true;
        player.fallDistance = 0;
        dashCooldown.put(player.getUuid(), 35);
    }

    public static void addXp(ServerPlayerEntity player, long amount) {
        if (amount <= 0) return;

        WalkData data = getData((ServerWorld) player.getWorld());
        String id = player.getUuidAsString();

        long oldXp = data.getXp(id);

        if (getLevelFromXp(oldXp) >= 100) return;

        long newXp =
            Math.min(Long.MAX_VALUE - amount, oldXp) + amount;

        data.setXp(id, newXp);
        data.markDirty();

        int oldLevel = getLevelFromXp(oldXp);
        int newLevel = getLevelFromXp(newXp);

        if (newLevel > oldLevel) {
            player.sendMessage(
                net.minecraft.text.Text.literal(
                    "§bWalk Skill §7Lv." + newLevel
                ),
                true
            );

            if (newLevel % 5 == 0) {
                int index = newLevel / 5 - 1;

                player.sendMessage(
                    net.minecraft.text.Text.literal(
                        "§6[Walk Skill] §e解放: " + STAGES[index]
                    ),
                    false
                );
            }
        }

        sync(player);
    }

    private static void effect(
        PlayerEntity player,
        net.minecraft.entity.effect.StatusEffect type,
        int duration,
        int amplifier
    ) {
        player.addStatusEffect(
            new StatusEffectInstance(
                type,
                duration,
                amplifier,
                true,
                false,
                true
            )
        );
    }

    public static int getLevel(ServerPlayerEntity player) {
        return getLevelFromXp(
            getData((ServerWorld) player.getWorld())
                .getXp(player.getUuidAsString())
        );
    }

    public static long getXp(ServerPlayerEntity player) {
        return getData((ServerWorld) player.getWorld())
            .getXp(player.getUuidAsString());
    }

    public static long getRequiredXp(int level) {
        return 100L + 25L * level * level;
    }

    public static long getCurrentLevelXp(long totalXp) {
        long used = 0;
        int level = 1;

        while (
            level < 100 &&
            totalXp >= used + getRequiredXp(level)
        ) {
            used += getRequiredXp(level);
            level++;
        }

        return totalXp - used;
    }

    public static long getLevelRequiredXp(int level) {
        return level >= 100 ? 0 : getRequiredXp(level);
    }

    private static int getLevelFromXp(long totalXp) {
        int level = 1;
        long remaining = totalXp;

        while (level < 100) {
            long required = getRequiredXp(level);

            if (remaining < required) break;

            remaining -= required;
            level++;
        }

        return level;
    }

    public static void sync(ServerPlayerEntity player) {
        if (player.networkHandler == null) return;

        int level = getLevel(player);
        long totalXp = getXp(player);

        PacketByteBuf buf =
            net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create();

        buf.writeInt(level);
        buf.writeLong(getCurrentLevelXp(totalXp));
        buf.writeLong(getLevelRequiredXp(level));
        buf.writeLong(totalXp);

        ServerPlayNetworking.send(player, SYNC, buf);
    }

    private static WalkData getData(ServerWorld world) {
        return world.getServer()
            .getOverworld()
            .getPersistentStateManager()
            .getOrCreate(
                WalkData::fromNbt,
                WalkData::new,
                STATE_ID
            );
    }

    private static class WalkData extends PersistentState {
        private final NbtCompound players = new NbtCompound();

        long getXp(String uuid) {
            return players.getLong(uuid);
        }

        void setXp(String uuid, long xp) {
            players.putLong(uuid, xp);
        }

        @Override
        public NbtCompound writeNbt(NbtCompound nbt) {
            nbt.put("players", players);
            return nbt;
        }

        static WalkData fromNbt(NbtCompound nbt) {
            WalkData data = new WalkData();

            if (nbt.contains("players")) {
                NbtCompound saved = nbt.getCompound("players");

                for (String key : saved.getKeys()) {
                    data.players.putLong(key, saved.getLong(key));
                }
            }

            return data;
        }
    }
}
