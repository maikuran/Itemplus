package com.sakalti.sakaplus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.sakalti.sakaplus.dimension.SakaplusDimensions;
import com.sakalti.sakaplus.sound.SakaplusSounds;
import com.sakalti.sakaplus.registry.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.util.registry.RegistryKey; // 1.19.2以前の正しいパッケージ名

public class SakaDimensionsCommand {

    private static final String KRUZIVILIM_VISITED_TAG = "has_visited_kruzivilim";
    private static final String CRYOVILIM_VISITED_TAG = "has_visited_cryovilim";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("sakaplus")
            .requires(source -> source.hasPermissionLevel(0))
            
            .then(CommandManager.literal("kruzivilim")
                .then(CommandManager.literal("iku")
                    .executes(ctx -> teleportKruzivilim(ctx.getSource(), true)))
                .then(CommandManager.literal("modoru")
                    .executes(ctx -> teleportKruzivilim(ctx.getSource(), false)))
            )
            
            .then(CommandManager.literal("cryovilim")
                .then(CommandManager.literal("iku")
                    .executes(ctx -> teleportCryovilim(ctx.getSource(), true)))
                .then(CommandManager.literal("modoru")
                    .executes(ctx -> teleportCryovilim(ctx.getSource(), false)))
            )
        );
    }

    private static int teleportKruzivilim(ServerCommandSource source, boolean toKruzivilim) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendError(Text.of("§cこのコマンドはプレイヤー専用です。"));
            return 0;
        }

        // getScoreboardTags() に修正
        if (toKruzivilim && !player.getScoreboardTags().contains(KRUZIVILIM_VISITED_TAG)) {
            int required = 3;
            int netheriteCount = countItems(player, Items.NETHERITE_INGOT);
            if (netheriteCount < required) {
                player.sendMessage(Text.of("§c初回移動にはネザライトインゴットが3個必要です。現在: " + netheriteCount + "個"), false);
                return 0;
            }
            removeItems(player, Items.NETHERITE_INGOT, required);
            player.addScoreboardTag(KRUZIVILIM_VISITED_TAG); // addScoreboardTag() に修正
            player.sendMessage(Text.of("§6ネザライトインゴットを3個捧げ、扉が開かれました…"), false);
        }

        return executeTeleport(source, player, 
            toKruzivilim ? SakaplusDimensions.KRUZIVILIM_DIMENSION_KEY : World.OVERWORLD, 
            toKruzivilim ? "§6クルジヴィリムへ移動しました。" : "§6元の世界に戻りました。");
    }

    private static int teleportCryovilim(ServerCommandSource source, boolean toCryovilim) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendError(Text.of("§cこのコマンドはプレイヤー専用です。"));
            return 0;
        }

        // getScoreboardTags() に修正
        if (toCryovilim && !player.getScoreboardTags().contains(CRYOVILIM_VISITED_TAG)) {
            int required = 24;
            int ingotCount = countItems(player, ModItems.PYRONIUM_INGOT);
            if (ingotCount < required) {
                player.sendMessage(Text.of("§c初回移動にはパイロニウムインゴットが24個必要です。現在: " + ingotCount + "個"), false);
                return 0;
            }
            removeItems(player, ModItems.PYRONIUM_INGOT, required);
            player.addScoreboardTag(CRYOVILIM_VISITED_TAG); // addScoreboardTag() に修正
            player.sendMessage(Text.of("§aパイロニウムインゴットを24個捧げ、扉が開かれました…"), false);
        }

        return executeTeleport(source, player, 
            toCryovilim ? SakaplusDimensions.CRYOVILIM_DIMENSION_KEY : World.OVERWORLD, 
            toCryovilim ? "§bクライオヴィリムへ移動しました。" : "§6元の世界に戻りました。");
    }

    private static int executeTeleport(ServerCommandSource source, ServerPlayerEntity player, RegistryKey<World> targetDimension, String successMessage) {
        ServerWorld currentWorld = source.getWorld();
        ServerWorld targetWorld = player.getServer().getWorld(targetDimension);
        
        if (targetWorld == null) {
            player.sendMessage(Text.of("§cテレポート先ワールドが見つかりません。"), false);
            return 0;
        }

        currentWorld.playSound(null, player.getX(), player.getY(), player.getZ(), 
            SakaplusSounds.StaffUse, SoundCategory.PLAYERS, 1.0F, 1.2F);
        currentWorld.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1.0D, player.getZ(), 
            30, 0.5D, 0.5D, 0.5D, 0.5D);

        BlockPos currentPos = player.getBlockPos();
        int targetY = targetWorld.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, currentPos.getX(), currentPos.getZ());

        if (targetY <= targetWorld.getBottomY()) {
            targetY = currentPos.getY();
        }

        player.teleport(targetWorld, currentPos.getX(), targetY, currentPos.getZ(), player.getYaw(), player.getPitch());
        
        targetWorld.playSound(null, player.getX(), player.getY(), player.getZ(), 
            SakaplusSounds.StaffUse, SoundCategory.PLAYERS, 1.0F, 1.0F);
        targetWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL, player.getX(), player.getY() + 1.0D, player.getZ(), 
            30, 0.5D, 0.5D, 0.5D, 0.5D);

        player.sendMessage(Text.of(successMessage), false);
        return 1;
    }

    private static int countItems(ServerPlayerEntity player, Item item) {
        return player.getInventory().main.stream()
            .filter(stack -> stack != null && stack.getItem() == item)
            .mapToInt(stack -> stack.getCount())
            .sum();
    }

    private static void removeItems(ServerPlayerEntity player, Item item, int amount) {
        int remaining = amount;
        for (int i = 0; i < player.getInventory().main.size(); i++) {
            var stack = player.getInventory().main.get(i);
            if (stack != null && stack.getItem() == item) {
                int take = Math.min(stack.getCount(), remaining);
                stack.decrement(take);
                remaining -= take;
                if (remaining <= 0) break;
            }
        }
    }
}
