
package com.sakalti.sakacountry.nation.command;

import com.sakalti.sakacountry.nation.Nation;
import com.sakalti.sakacountry.nation.NationSavedData;

import com.mojang.brigadier.arguments.StringArgumentType;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class NationCommands {

    public static void register() {

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {

            dispatcher.register(
                    Commands.literal("nation")

                    // 国家作成
                    .then(Commands.literal("create")
                            .then(Commands.argument(
                                    "name",
                                    StringArgumentType.greedyString()
                            )
                            .executes(context -> {

                                ServerPlayer player =
                                        context.getSource()
                                                .getPlayerOrException();

                                String name = StringArgumentType.getString(
                                        context,
                                        "name"
                                ).trim();

                                if (name.isEmpty() || name.length() > 48) {
                                    context.getSource().sendFailure(
                                            Component.literal(
                                                    "国家名は1～48文字にしてください。"
                                            )
                                    );
                                    return 0;
                                }

                                NationSavedData data =
                                        NationSavedData.get(
                                                context.getSource().getServer()
                                        );

                                if (data.findByMember(player.getUUID()) != null) {
                                    context.getSource().sendFailure(
                                            Component.literal(
                                                    "既に国家に所属しています。"
                                            )
                                    );
                                    return 0;
                                }

                                Nation nation = data.createNation(
                                        name,
                                        player.getUUID()
                                );

                                if (nation == null) {
                                    context.getSource().sendFailure(
                                            Component.literal(
                                                    "同じ名前の国家が存在します。"
                                            )
                                    );
                                    return 0;
                                }

                                context.getSource().sendSuccess(
                                        () -> Component.literal(
                                                "国家「" + name + "」を設立しました。"
                                                + " 権限LV: 0"
                                        ),
                                        false
                                );

                                return 1;
                            })
                    ))

                    // 国家一覧
                    .then(Commands.literal("list")
                            .executes(context -> {

                                NationSavedData data =
                                        NationSavedData.get(
                                                context.getSource().getServer()
                                        );

                                context.getSource().sendSuccess(
                                        () -> Component.literal(
                                                "===== 国家一覧 ====="
                                        ),
                                        false
                                );

                                for (Nation nation : data.getNations()) {
                                    context.getSource().sendSuccess(
                                            () -> Component.literal(
                                                    nation.getName()
                                                    + " | 国民: "
                                                    + nation.getMemberCount()
                                                    + " | ID: "
                                                    + nation.getId()
                                            ),
                                            false
                                    );
                                }

                                context.getSource().sendSuccess(
                                        () -> Component.literal(
                                                "国家数: " + data.getNationCount()
                                        ),
                                        false
                                );

                                return 1;
                            })
                    )

                    // 国家情報
                    .then(Commands.literal("info")
                            .then(Commands.argument(
                                    "name",
                                    StringArgumentType.greedyString()
                            )
                            .executes(context -> {

                                String name = StringArgumentType.getString(
                                        context,
                                        "name"
                                );

                                NationSavedData data =
                                        NationSavedData.get(
                                                context.getSource().getServer()
                                        );

                                Nation nation = data.findByName(name);

                                if (nation == null) {
                                    context.getSource().sendFailure(
                                            Component.literal(
                                                    "国家が見つかりません。"
                                            )
                                    );
                                    return 0;
                                }

                                context.getSource().sendSuccess(
                                        () -> Component.literal(
                                                "===== " + nation.getName() + " ====="
                                        ),
                                        false
                                );

                                context.getSource().sendSuccess(
                                        () -> Component.literal(
                                                "国家ID: " + nation.getId()
                                        ),
                                        false
                                );

                                context.getSource().sendSuccess(
                                        () -> Component.literal(
                                                "設立者UUID: "
                                                + nation.getFounderUuid()
                                        ),
                                        false
                                );

                                context.getSource().sendSuccess(
                                        () -> Component.literal(
                                                "国民数: "
                                                + nation.getMemberCount()
                                        ),
                                        false
                                );

                                return 1;
                            })
                    ))

                    // 国家加入
                    .then(Commands.literal("join")
                            .then(Commands.argument(
                                    "name",
                                    StringArgumentType.greedyString()
                            )
                            .executes(context -> {

                                ServerPlayer player =
                                        context.getSource()
                                                .getPlayerOrException();

                                NationSavedData data =
                                        NationSavedData.get(
                                                context.getSource().getServer()
                                        );

                                if (data.findByMember(player.getUUID()) != null) {
                                    context.getSource().sendFailure(
                                            Component.literal(
                                                    "既に国家に所属しています。"
                                            )
                                    );
                                    return 0;
                                }

                                Nation nation = data.findByName(
                                        StringArgumentType.getString(
                                                context,
                                                "name"
                                        )
                                );

                                if (nation == null) {
                                    context.getSource().sendFailure(
                                            Component.literal(
                                                    "国家が見つかりません。"
                                            )
                                    );
                                    return 0;
                                }

                                nation.addMember(player.getUUID());
                                data.markChanged();

                                context.getSource().sendSuccess(
                                        () -> Component.literal(
                                                nation.getName()
                                                + " に加入しました。権限LV: 0"
                                        ),
                                        false
                                );

                                return 1;
                            })
                    ))

                    // 国家脱退
                    .then(Commands.literal("leave")
                            .executes(context -> {

                                ServerPlayer player =
                                        context.getSource()
                                                .getPlayerOrException();

                                NationSavedData data =
                                        NationSavedData.get(
                                                context.getSource().getServer()
                                        );

                                Nation nation = data.findByMember(
                                        player.getUUID()
                                );

                                if (nation == null) {
                                    context.getSource().sendFailure(
                                            Component.literal(
                                                    "所属している国家がありません。"
                                            )
                                    );
                                    return 0;
                                }

                                nation.removeMember(player.getUUID());
                                data.markChanged();

                                context.getSource().sendSuccess(
                                        () -> Component.literal(
                                                nation.getName()
                                                + " から脱退しました。"
                                        ),
                                        false
                                );

                                return 1;
                            })
                    )
            );
        });
    }
}
