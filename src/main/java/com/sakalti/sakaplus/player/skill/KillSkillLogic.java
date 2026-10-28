
package com.sakalti.sakaplus.player.skill;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;

public final class KillSkillLogic {

    public static final Identifier REQUEST =
        new Identifier("sakaplus", "kill_skill_request");

    public static final Identifier SYNC =
        new Identifier("sakaplus", "kill_skill_sync");

    private static final String STATE_ID = "sakaplus_kill_skill";

    public static final String[] STAGES = {
        "Finisher", "Blood Recovery", "Battle Momentum", "Combat Focus",
        "Absorption", "Regeneration", "Iron Body", "Full Stomach",
        "Flame Guard", "Battle Haste", "Warrior Jump", "Lucky Hunter",
        "Night Hunter", "Deep Breath", "Power Strike", "Rapid Recovery",
        "Greater Absorption", "Killer Speed", "Unbreakable", "Killing Frenzy"
    };

    public static final String[] DESCRIPTIONS = {
        "敵を倒すと追加Kill経験値を獲得",
        "敵を倒すと体力を1回復",
        "敵を倒すと短時間移動速度上昇",
        "敵を倒すと短時間攻撃力上昇",
        "敵を倒すと吸収体力を獲得",
        "敵を倒すと再生能力を獲得",
        "敵を倒すと耐性を獲得",
        "敵を倒すと満腹度を回復",
        "敵を倒すと火炎耐性を獲得",
        "敵を倒すと採掘速度上昇",
        "敵を倒すと跳躍力上昇",
        "敵を倒すと幸運を獲得",
        "敵を倒すと暗視を獲得",
        "敵を倒すと水中呼吸を獲得",
        "敵を倒すと強い攻撃力上昇",
        "敵を倒すと強い再生能力を獲得",
        "敵を倒すと強い吸収体力を獲得",
        "敵を倒すと強い移動速度上昇",
        "敵を倒すと強い耐性を獲得",
        "敵を倒すと全戦闘効果を獲得"
    };

    private KillSkillLogic() {}

    public static void initialize() {

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(
            (world, killer, victim) -> {

                if (!(killer instanceof ServerPlayerEntity player)) return;
                if (!(victim instanceof LivingEntity)) return;

                int victimHealth = Math.max(
                    1,
                    (int) Math.ceil(victim.getMaxHealth())
                );

                addXp(player, 5L + victimHealth);
                applyKillRewards(player);
            }
        );

        // 定期的にHealth Boostを確認
        ServerTickEvents.END_SERVER_TICK.register(server -> {

            for (ServerPlayerEntity player :
                server.getPlayerManager().getPlayerList()) {

                if (player.age % 20 == 0) {
                    updateHealthBoost(player);
                }

                if (getLevel(player) < 100) {
                    sync(player);
                }
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(
            REQUEST,
            (server, player, handler, buf, responseSender) ->
                server.execute(() -> sync(player))
        );
    }

    // =========================================
    // XP
    // =========================================

    public static void addXp(
        ServerPlayerEntity player,
        long amount
    ) {
        if (amount <= 0) return;

        KillData data = getData((ServerWorld) player.getWorld());
        String id = player.getUuidAsString();

        long xp = data.getXp(id);

        if (getLevelFromXp(xp) >= 100) return;

        xp = Math.min(Long.MAX_VALUE - amount, xp) + amount;

        data.setXp(id, xp);
        data.markDirty();

        sync(player);

        int oldLevel = getLevelFromXp(xp - amount);
        int newLevel = getLevelFromXp(xp);

        // HP増加効果を更新
        updateHealthBoost(player);

        // 10レベルごとにHPを4回復
        if (
            newLevel >= 30 &&
            newLevel / 10 > oldLevel / 10
        ) {
            player.heal(4.0F);
        }

        if (newLevel > oldLevel) {

            player.sendMessage(
                net.minecraft.text.Text.literal(
                    "§cKill Skill §7Lv." + newLevel
                ),
                true
            );

            if (newLevel % 5 == 0) {

                int index = newLevel / 5 - 1;

                player.sendMessage(
                    net.minecraft.text.Text.literal(
                        "§6[Kill Skill] §e解放: " + STAGES[index]
                    ),
                    false
                );
            }
        }
    }

    private static void addXpWithoutReward(
        ServerPlayerEntity player,
        long amount
    ) {
        KillData data = getData((ServerWorld) player.getWorld());

        String id = player.getUuidAsString();

        long xp = data.getXp(id);

        if (getLevelFromXp(xp) >= 100) return;

        xp = Math.min(Long.MAX_VALUE - amount, xp) + amount;

        data.setXp(id, xp);
        data.markDirty();

        sync(player);
    }

    // =========================================
    // HEALTH BOOST
    // =========================================

    private static void updateHealthBoost(
        ServerPlayerEntity player
    ) {
        int level = getLevel(player);

        // Lv.30未満は体力増強なし
        if (level < 30) {

            if (player.hasStatusEffect(StatusEffects.HEALTH_BOOST)) {
                player.removeStatusEffect(StatusEffects.HEALTH_BOOST);
            }

            return;
        }

        // Lv.30から10レベルごとに強化
        int amplifier = Math.min(
            7,
            (level - 30) / 10
        );

        StatusEffectInstance current =
            player.getStatusEffect(StatusEffects.HEALTH_BOOST);

        if (
            current == null ||
            current.getAmplifier() != amplifier ||
            current.getDuration() < 1200
        ) {
            player.addStatusEffect(
                new StatusEffectInstance(
                    StatusEffects.HEALTH_BOOST,
                    Integer.MAX_VALUE,
                    amplifier,
                    true,
                    true,
                    true
                )
            );
        }
    }

    // =========================================
    // KILL REWARDS
    // =========================================

    private static void applyKillRewards(
        ServerPlayerEntity player
    ) {
        int level = getLevel(player);

        // 1: Finisher
        if (level >= 5) {
            addXpWithoutReward(player, 2);
        }

        // 2: Blood Recovery
        if (level >= 10) {
            player.heal(1.0F);
        }

        // 3: Battle Momentum
        if (level >= 15) {
            effect(player, StatusEffects.SPEED, 80, 0);
        }

        // 4: Combat Focus
        if (level >= 20) {
            effect(player, StatusEffects.STRENGTH, 80, 0);
        }

        // 5: Absorption
        if (level >= 25) {
            effect(player, StatusEffects.ABSORPTION, 160, 0);
        }

        // 6: Regeneration
        if (level >= 30) {
            effect(player, StatusEffects.REGENERATION, 100, 0);
        }

        // 7: Iron Body
        if (level >= 35) {
            effect(player, StatusEffects.RESISTANCE, 100, 0);
        }

        // 8: Full Stomach
        if (level >= 40) {

            player.getHungerManager().setFoodLevel(
                Math.min(
                    20,
                    player.getHungerManager().getFoodLevel() + 2
                )
            );

            player.getHungerManager().setSaturationLevel(
                Math.min(
                    20.0F,
                    player.getHungerManager().getSaturationLevel() + 2.0F
                )
            );
        }

        // 9: Flame Guard
        if (level >= 45) {
            effect(player, StatusEffects.FIRE_RESISTANCE, 200, 0);
        }

        // 10: Battle Haste
        if (level >= 50) {
            effect(player, StatusEffects.HASTE, 100, 0);
        }

        // 11: Warrior Jump
        if (level >= 55) {
            effect(player, StatusEffects.JUMP_BOOST, 100, 0);
        }

        // 12: Lucky Hunter
        if (level >= 60) {
            effect(player, StatusEffects.LUCK, 200, 0);
        }

        // 13: Night Hunter
        if (level >= 65) {
            effect(player, StatusEffects.NIGHT_VISION, 300, 0);
        }

        // 14: Deep Breath
        if (level >= 70) {
            effect(player, StatusEffects.WATER_BREATHING, 200, 0);
        }

        // 15: Power Strike
        if (level >= 75) {
            effect(player, StatusEffects.STRENGTH, 160, 1);
        }

        // 16: Rapid Recovery
        if (level >= 80) {
            effect(player, StatusEffects.REGENERATION, 160, 1);
        }

        // 17: Greater Absorption
        if (level >= 85) {
            effect(player, StatusEffects.ABSORPTION, 240, 1);
        }

        // 18: Killer Speed
        if (level >= 90) {
            effect(player, StatusEffects.SPEED, 160, 1);
        }

        // 19: Unbreakable
        if (level >= 95) {
            effect(player, StatusEffects.RESISTANCE, 160, 1);
        }

        // 20: Killing Frenzy
        if (level >= 100) {
            effect(player, StatusEffects.STRENGTH, 300, 1);
            effect(player, StatusEffects.SPEED, 300, 1);
            effect(player, StatusEffects.REGENERATION, 300, 1);
            effect(player, StatusEffects.RESISTANCE, 300, 0);
        }
    }

    // =========================================
    // STATUS EFFECT
    // =========================================

    private static void effect(
        PlayerEntity player,
        StatusEffect type,
        int duration,
        int amplifier
    ) {
        player.addStatusEffect(
            new StatusEffectInstance(
                type,
                duration,
                amplifier,
                true,
                true,
                true
            )
        );
    }

    // =========================================
    // LEVEL / XP CALCULATION
    // =========================================

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

    public static boolean hasStage(
        ServerPlayerEntity player,
        int stage
    ) {
        return getLevel(player) >= stage * 5;
    }

    // =========================================
    // NETWORK SYNC
    // =========================================

    public static void sync(ServerPlayerEntity player) {

        if (player.networkHandler == null) return;

        int level = getLevel(player);
        long totalXp = getXp(player);

        long current = getCurrentLevelXp(totalXp);
        long required = getLevelRequiredXp(level);

        net.minecraft.network.PacketByteBuf buf =
            net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create();

        buf.writeInt(level);
        buf.writeLong(current);
        buf.writeLong(required);
        buf.writeLong(totalXp);

        ServerPlayNetworking.send(player, SYNC, buf);
    }

    // =========================================
    // PERSISTENT DATA
    // =========================================

    private static KillData getData(ServerWorld world) {

        return world.getServer()
            .getOverworld()
            .getPersistentStateManager()
            .getOrCreate(
                KillData::fromNbt,
                KillData::new,
                STATE_ID
            );
    }

    private static class KillData extends PersistentState {

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

        static KillData fromNbt(NbtCompound nbt) {

            KillData data = new KillData();

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
