package com.sakalti.sakaplus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.sakalti.sakaplus.dimension.SakaplusDimensions;
import com.sakalti.sakaplus.sound.SakaplusSounds;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class KruzivilimCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("kruzivilim")
            // 権限レベル0（一般プレイヤー）でも実行可能に設定
            .requires(source -> source.hasPermissionLevel(0))
            .then(CommandManager.literal("iku")
                .executes(ctx -> teleport(ctx.getSource(), true)))
            .then(CommandManager.literal("modoru")
                .executes(ctx -> teleport(ctx.getSource(), false)))
        );
    }

    private static int teleport(ServerCommandSource source, boolean toKruzivilim) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendError(Text.of("§cこのコマンドはプレイヤー専用です。"));
            return 0;
        }

        // 必要な金インゴットの数
        int required = 28;
        int goldCount = countItems(player, Items.GOLD_INGOT);
        if (goldCount < required) {
            player.sendMessage(Text.of("§c金インゴットが28個必要です。現在: " + goldCount + "個"), false);
            return 0;
        }

        // ServerCommandSource から現在の ServerWorld を安全に取得（エラー修正箇所）
        ServerWorld currentWorld = source.getWorld();
        ServerWorld targetWorld = player.getServer().getWorld(
            toKruzivilim ? SakaplusDimensions.KRUZIVILIM_DIMENSION_KEY : World.OVERWORLD
        );
        
        if (targetWorld == null) {
            player.sendMessage(Text.of("§cテレポート先ワールドが見つかりません。"), false);
            return 0;
        }

        // 移動前の世界でテレポート演出（音とパーティクル）
        currentWorld.playSound(null, player.getX(), player.getY(), player.getZ(), 
            SakaplusSounds.StaffUse, SoundCategory.PLAYERS, 1.0F, 1.2F);
        currentWorld.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1.0D, player.getZ(), 
            30, 0.5D, 0.5D, 0.5D, 0.5D);

        // 消費処理
        removeItems(player, Items.GOLD_INGOT, required);

        // 安全対策：移動先世界のX, Z座標における「一番高い地表（Y座標）」を取得して窒息防止
        BlockPos currentPos = player.getBlockPos();
        int targetY = targetWorld.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, currentPos.getX(), currentPos.getZ());

        // 地形生成が間に合わない、または奈落だった場合の安全処理
        if (targetY <= targetWorld.getBottomY()) {
            targetY = currentPos.getY();
        }

        // テレポート実行
        player.teleport(targetWorld, currentPos.getX(), targetY, currentPos.getZ(), player.getYaw(), player.getPitch());
        
        // 移動後の世界でも演出
        targetWorld.playSound(null, player.getX(), player.getY(), player.getZ(), 
            SakaplusSounds.StaffUse, SoundCategory.PLAYERS, 1.0F, 1.0F);
        targetWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL, player.getX(), player.getY() + 1.0D, player.getZ(), 
            30, 0.5D, 0.5D, 0.5D, 0.5D);

        player.sendMessage(Text.of(toKruzivilim ? "§6クルジヴィリムへ移動しました。" : "§6元の世界に戻りました。"), false);
        return 1;
    }

    private static int countItems(ServerPlayerEntity player, net.minecraft.item.Item item) {
        return player.getInventory().main.stream()
            .filter(stack -> stack != null && stack.getItem() == item)
            .mapToInt(stack -> stack.getCount())
            .sum();
    }

    private static void removeItems(ServerPlayerEntity player, net.minecraft.item.Item item, int amount) {
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
