
package com.sakalti.sakaplus.player.skill;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public final class SkillUI {
    private static final Identifier KILL_REQUEST =
        new Identifier("sakaplus", "kill_skill_request");
    private static final Identifier KILL_SYNC =
        new Identifier("sakaplus", "kill_skill_sync");

    private static final Identifier WALK_REQUEST =
        new Identifier("sakaplus", "walk_skill_request");
    private static final Identifier WALK_SYNC =
        new Identifier("sakaplus", "walk_skill_sync");

    private static final Identifier DOUBLE_JUMP =
        new Identifier("sakaplus", "walk_double_jump");
    private static final Identifier AIR_DASH =
        new Identifier("sakaplus", "walk_air_dash");

    private static KeyBinding openKey;
    private static KeyBinding dashKey;
    private static KeyBinding blinkKey;

    private static int killLevel = 1;
    private static long killCurrentXp;
    private static long killRequiredXp;
    private static long killTotalXp;

    private static int walkLevel = 1;
    private static long walkCurrentXp;
    private static long walkRequiredXp;
    private static long walkTotalXp;

    private static boolean initialized;
    private static boolean previousJumpDown;

    private SkillUI() {}

    public static void registerClientNetworking() {
        if (initialized) return;
        initialized = true;

        openKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.sakaplus.skill_ui",
                GLFW.GLFW_KEY_K,
                "category.sakaplus"
            )
        );

        dashKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.sakaplus.air_dash",
                GLFW.GLFW_KEY_G,
                "category.sakaplus"
            )
        );

        blinkKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.sakaplus.blink_step",
                GLFW.GLFW_KEY_H,
                "category.sakaplus"
            )
        );

        ClientPlayNetworking.registerGlobalReceiver(
            KILL_SYNC,
            (client, handler, buf, responseSender) -> {
                int level = buf.readInt();
                long current = buf.readLong();
                long required = buf.readLong();
                long total = buf.readLong();

                client.execute(() -> {
                    killLevel = level;
                    killCurrentXp = current;
                    killRequiredXp = required;
                    killTotalXp = total;
                });
            }
        );

        ClientPlayNetworking.registerGlobalReceiver(
            WALK_SYNC,
            (client, handler, buf, responseSender) -> {
                int level = buf.readInt();
                long current = buf.readLong();
                long required = buf.readLong();
                long total = buf.readLong();

                client.execute(() -> {
                    walkLevel = level;
                    walkCurrentXp = current;
                    walkRequiredXp = required;
                    walkTotalXp = total;
                });
            }
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            while (openKey.wasPressed()) {
                client.setScreen(new SkillScreen());
                requestData();
            }

            // Spaceキーを押した瞬間だけ送信
            boolean jumpDown = client.options.jumpKey.isPressed();

            if (jumpDown && !previousJumpDown && !client.player.isOnGround()) {
                ClientPlayNetworking.send(DOUBLE_JUMP, PacketByteBufs.empty());
            }

            previousJumpDown = jumpDown;

            while (dashKey.wasPressed()) {
                ClientPlayNetworking.send(AIR_DASH, PacketByteBufs.empty());
            }

            while (blinkKey.wasPressed()) {
                if (walkLevel >= 95) {
                    ClientPlayNetworking.send(AIR_DASH, PacketByteBufs.empty());
                }
            }
        });
    }

    public static void open() {
        MinecraftClient.getInstance().setScreen(new SkillScreen());
        requestData();
    }

    private static void requestData() {
        if (ClientPlayNetworking.canSend(KILL_REQUEST)) {
            ClientPlayNetworking.send(KILL_REQUEST, PacketByteBufs.empty());
        }

        if (ClientPlayNetworking.canSend(WALK_REQUEST)) {
            ClientPlayNetworking.send(WALK_REQUEST, PacketByteBufs.empty());
        }
    }

    private static class SkillScreen extends Screen {
        protected SkillScreen() {
            super(Text.literal("Sakaplus Skills"));
        }

        @Override
        public void render(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta
        ) {
            renderBackground(context);

            int centerX = width / 2;
            int top = 28;

            context.fill(
                centerX - 190, top,
                centerX + 190, height - 25,
                0xE8202028
            );

            context.drawCenteredTextWithShadow(
                textRenderer,
                "§lSakaplus Skills",
                centerX,
                top + 10,
                0xFFFFFF
            );

            drawSkill(
                context,
                centerX - 175,
                top + 38,
                "§cKILL SKILL",
                killLevel,
                killCurrentXp,
                killRequiredXp,
                killTotalXp,
                KillSkillLogic.STAGES,
                KillSkillLogic.DESCRIPTIONS,
                0xFFB34A
            );

            drawSkill(
                context,
                centerX + 15,
                top + 38,
                "§bWALK SKILL",
                walkLevel,
                walkCurrentXp,
                walkRequiredXp,
                walkTotalXp,
                WalkSkillLogic.STAGES,
                WalkSkillLogic.DESCRIPTIONS,
                0x55CFFF
            );

            context.drawCenteredTextWithShadow(
                textRenderer,
                "Space: Double Jump    G: Air Dash    K: Close",
                centerX,
                height - 40,
                0xBBBBBB
            );

            super.render(context, mouseX, mouseY, delta);
        }

        private void drawSkill(
            DrawContext context,
            int x,
            int y,
            String title,
            int level,
            long currentXp,
            long requiredXp,
            long totalXp,
            String[] stages,
            String[] descriptions,
            int color
        ) {
            context.drawTextWithShadow(
                textRenderer, title, x, y, color
            );

            context.drawTextWithShadow(
                textRenderer,
                "Level: " + level + " / 100",
                x,
                y + 18,
                0xFFFFFF
            );

            context.drawTextWithShadow(
                textRenderer,
                "Total XP: " + totalXp,
                x,
                y + 32,
                0xCCCCCC
            );

            int barWidth = 165;
            int barY = y + 49;

            context.fill(x, barY, x + barWidth, barY + 8, 0xFF444444);

            if (requiredXp > 0) {
                int filled = (int) Math.min(
                    barWidth,
                    barWidth * (double) currentXp / requiredXp
                );

                context.fill(
                    x, barY,
                    x + filled, barY + 8,
                    color
                );
            }

            context.drawTextWithShadow(
                textRenderer,
                requiredXp > 0
                    ? currentXp + " / " + requiredXp + " XP"
                    : "MAX LEVEL",
                x,
                barY + 12,
                0xAAAAAA
            );

            int listY = barY + 31;

            for (int i = 0; i < 20; i++) {
                int requiredLevel = (i + 1) * 5;
                boolean unlocked = level >= requiredLevel;

                String prefix = unlocked ? "§a✓ " : "§7🔒 ";
                String stageText = prefix + "Lv." + requiredLevel + " " + stages[i];

                context.drawTextWithShadow(
                    textRenderer,
                    stageText,
                    x,
                    listY + i * 12,
                    unlocked ? 0xFFFFFF : 0x888888
                );

                if (unlocked) {
                    context.drawTextWithShadow(
                        textRenderer,
                        descriptions[i],
                        x + 8,
                        listY + i * 12 + 9,
                        0xAAAAAA
                    );
                }
            }
        }

        @Override
        public boolean shouldPause() {
            return false;
        }

        @Override
        public void close() {
            MinecraftClient.getInstance().setScreen(null);
        }
    }
}
