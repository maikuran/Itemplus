
package com.sakalti.sakaplus.player.skill;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
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
    private static long killCurrentXp = 0;
    private static long killRequiredXp = 0;
    private static long killTotalXp = 0;

    private static int walkLevel = 1;
    private static long walkCurrentXp = 0;
    private static long walkRequiredXp = 0;
    private static long walkTotalXp = 0;

    private static boolean initialized = false;
    private static boolean previousJumpDown = false;

    private SkillUI() {
    }

    // =====================================================
    // CLIENT REGISTRATION
    // =====================================================

    public static void registerClientNetworking() {

        if (initialized) return;
        initialized = true;

        // K: Skill UI
        openKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.sakaplus.skill_ui",
                        GLFW.GLFW_KEY_K,
                        "category.sakaplus"
                )
        );

        // G: Air Dash
        dashKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.sakaplus.air_dash",
                        GLFW.GLFW_KEY_G,
                        "category.sakaplus"
                )
        );

        // H: Blink Step
        blinkKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.sakaplus.blink_step",
                        GLFW.GLFW_KEY_H,
                        "category.sakaplus"
                )
        );

        // =================================================
        // KILL SYNC
        // =================================================

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

        // =================================================
        // WALK SYNC
        // =================================================

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

        // =================================================
        // CLIENT TICK
        // =================================================

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null || client.world == null) {
                previousJumpDown = false;
                return;
            }

            // Open skill screen
            while (openKey.wasPressed()) {
                client.setScreen(new SkillScreen());
                requestData();
            }

            // Double Jump
            boolean jumpDown = client.options.jumpKey.isPressed();

            if (jumpDown
                    && !previousJumpDown
                    && !client.player.isOnGround()) {

                if (walkLevel >= 5) {
                    ClientPlayNetworking.send(
                            DOUBLE_JUMP,
                            PacketByteBufs.empty()
                    );
                }
            }

            previousJumpDown = jumpDown;

            // Air Dash
            while (dashKey.wasPressed()) {

                if (walkLevel >= 40) {
                    ClientPlayNetworking.send(
                            AIR_DASH,
                            PacketByteBufs.empty()
                    );
                }
            }

            // Blink Step
            // 現在は空中ダッシュと同じパケットを使用
            while (blinkKey.wasPressed()) {

                if (walkLevel >= 95) {
                    ClientPlayNetworking.send(
                            AIR_DASH,
                            PacketByteBufs.empty()
                    );
                }
            }
        });
    }

    // =====================================================
    // OPEN SCREEN
    // =====================================================

    public static void open() {

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) return;

        client.setScreen(new SkillScreen());
        requestData();
    }

    // =====================================================
    // REQUEST DATA
    // =====================================================

    private static void requestData() {

        if (ClientPlayNetworking.canSend(KILL_REQUEST)) {
            ClientPlayNetworking.send(
                    KILL_REQUEST,
                    PacketByteBufs.empty()
            );
        }

        if (ClientPlayNetworking.canSend(WALK_REQUEST)) {
            ClientPlayNetworking.send(
                    WALK_REQUEST,
                    PacketByteBufs.empty()
            );
        }
    }

    // =====================================================
    // SKILL SCREEN
    // =====================================================

    private static class SkillScreen extends Screen {

        protected SkillScreen() {
            super(Text.literal("Sakaplus Skills"));
        }

        @Override
        public void render(
                MatrixStack matrices,
                int mouseX,
                int mouseY,
                float delta
        ) {

            renderBackground(matrices);

            int centerX = width / 2;
            int top = 20;

            // Main panel
            fill(
                    matrices,
                    centerX - 190,
                    top,
                    centerX + 190,
                    height - 20,
                    0xE8202028
            );

            // Title
            drawCentered(
                    matrices,
                    "§lSakaplus Skills",
                    centerX,
                    top + 10,
                    0xFFFFFF
            );

            // Kill Skill
            drawSkill(
                    matrices,
                    centerX - 175,
                    top + 38,
                    "§cKILL SKILL",
                    killLevel,
                    killCurrentXp,
                    killRequiredXp,
                    killTotalXp,
                    KillSkillLogic.STAGES,
                    KillSkillLogic.DESCRIPTIONS,
                    0xFFFFB34A
            );

            // Walk Skill
            drawSkill(
                    matrices,
                    centerX + 15,
                    top + 38,
                    "§bWALK SKILL",
                    walkLevel,
                    walkCurrentXp,
                    walkRequiredXp,
                    walkTotalXp,
                    WalkSkillLogic.STAGES,
                    WalkSkillLogic.DESCRIPTIONS,
                    0xFF55CFFF
            );

            // Controls
            drawCentered(
                    matrices,
                    "Space: Double Jump    G: Air Dash    H: Blink Step    K: Close",
                    centerX,
                    height - 35,
                    0xFFBBBBBB
            );

            super.render(matrices, mouseX, mouseY, delta);
        }

        // =================================================
        // DRAW SKILL
        // =================================================

        private void drawSkill(
                MatrixStack matrices,
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

            // Skill title
            textRenderer.drawWithShadow(
                    matrices,
                    title,
                    x,
                    y,
                    color
            );

            // Level
            textRenderer.drawWithShadow(
                    matrices,
                    "Level: " + level + " / 100",
                    x,
                    y + 18,
                    0xFFFFFFFF
            );

            // Total XP
            textRenderer.drawWithShadow(
                    matrices,
                    "Total XP: " + totalXp,
                    x,
                    y + 32,
                    0xFFCCCCCC
            );

            // XP bar
            int barWidth = 165;
            int barY = y + 49;

            fill(
                    matrices,
                    x,
                    barY,
                    x + barWidth,
                    barY + 8,
                    0xFF444444
            );

            if (requiredXp > 0) {

                int filled = (int) Math.min(
                        barWidth,
                        barWidth * (double) currentXp / requiredXp
                );

                fill(
                        matrices,
                        x,
                        barY,
                        x + filled,
                        barY + 8,
                        color
                );
            }

            // XP text
            textRenderer.drawWithShadow(
                    matrices,
                    requiredXp > 0
                            ? currentXp + " / " + requiredXp + " XP"
                            : "MAX LEVEL",
                    x,
                    barY + 12,
                    0xFFAAAAAA
            );

            // Ability list
            int listY = barY + 32;
            int rowHeight = 22;

            for (int i = 0; i < 20; i++) {

                int requiredLevel = (i + 1) * 5;

                boolean unlocked = level >= requiredLevel;

                String prefix = unlocked ? "§a✓ " : "§7🔒 ";

                String stageText =
                        prefix
                        + "Lv."
                        + requiredLevel
                        + " "
                        + stages[i];

                int rowY = listY + i * rowHeight;

                // Ability name
                textRenderer.drawWithShadow(
                        matrices,
                        stageText,
                        x,
                        rowY,
                        unlocked ? 0xFFFFFFFF : 0xFF888888
                );

                // Ability description
                if (unlocked) {

                    textRenderer.drawWithShadow(
                            matrices,
                            "§7" + descriptions[i],
                            x + 8,
                            rowY + 10,
                            0xFFAAAAAA
                    );
                }
            }
        }

        // =================================================
        // CENTERED TEXT
        // =================================================

        private void drawCentered(
                MatrixStack matrices,
                String text,
                int centerX,
                int y,
                int color
        ) {

            int textWidth = textRenderer.getWidth(text);

            textRenderer.drawWithShadow(
                    matrices,
                    text,
                    centerX - textWidth / 2,
                    y,
                    color
            );
        }

        // =================================================
        // SCREEN SETTINGS
        // =================================================

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
