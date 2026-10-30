
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

import com.mojang.blaze3d.systems.RenderSystem;

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

            if (client.player == null || client.world == null) {
                previousJumpDown = false;
                return;
            }

            while (openKey.wasPressed()) {
                client.setScreen(new SkillScreen());
                requestData();
            }

            boolean jumpDown = client.options.jumpKey.isPressed();

            if (jumpDown
                    && !previousJumpDown
                    && !client.player.isOnGround()
                    && walkLevel >= 5) {

                ClientPlayNetworking.send(
                        DOUBLE_JUMP,
                        PacketByteBufs.empty()
                );
            }

            previousJumpDown = jumpDown;

            while (dashKey.wasPressed()) {
                if (walkLevel >= 40) {
                    ClientPlayNetworking.send(
                            AIR_DASH,
                            PacketByteBufs.empty()
                    );
                }
            }

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

        private static final int PANEL_MARGIN = 10;
        private static final int CARD_HEIGHT = 640;
        private static final int CARD_GAP = 16;
        private static final int SCROLL_SPEED = 35;

        private int scrollOffset = 0;
        private int maxScroll = 0;

        private int viewportTop;
        private int viewportBottom;

        private int panelLeft;
        private int panelRight;
        private int panelWidth;

        private boolean twoColumns;

        protected SkillScreen() {
            super(Text.literal("Sakaplus Skills"));
        }

        // =================================================
        // INIT
        // =================================================

        @Override
        protected void init() {
            super.init();

            scrollOffset = 0;
            calculateLayout();
        }

        // =================================================
        // LAYOUT
        // =================================================

        private void calculateLayout() {

            panelWidth = Math.max(
                    0,
                    Math.min(width - PANEL_MARGIN * 2, 780)
            );

            panelLeft = (width - panelWidth) / 2;
            panelRight = panelLeft + panelWidth;

            twoColumns = panelWidth >= 560;

            viewportTop = 48;
            viewportBottom = height - 45;

            if (viewportBottom < viewportTop) {
                viewportBottom = viewportTop;
            }

            int contentHeight = twoColumns
                    ? CARD_HEIGHT
                    : CARD_HEIGHT * 2 + CARD_GAP;

            maxScroll = Math.max(
                    0,
                    contentHeight - (viewportBottom - viewportTop)
            );

            scrollOffset = Math.max(
                    0,
                    Math.min(scrollOffset, maxScroll)
            );
        }

        // =================================================
        // RENDER
        // =================================================

        @Override
        public void render(
                MatrixStack matrices,
                int mouseX,
                int mouseY,
                float delta
        ) {

            renderBackground(matrices);

            calculateLayout();

            int centerX = width / 2;

            // Main panel

            fill(
                    matrices,
                    panelLeft,
                    10,
                    panelRight,
                    height - 10,
                    0xEE171923
            );

            // Border

            fill(
                    matrices,
                    panelLeft,
                    10,
                    panelRight,
                    12,
                    0xFF55AADD
            );

            // Title

            drawCentered(
                    matrices,
                    "§l§bSAKAPLUS SKILLS",
                    centerX,
                    19,
                    0xFFFFFFFF
            );

            // =================================================
            // SCISSOR
            // =================================================

            MinecraftClient client = MinecraftClient.getInstance();

            double scale = client.getWindow().getScaleFactor();

            int scissorX = (int) (panelLeft * scale);

            int scissorY = (int) (
                    (height - viewportBottom) * scale
            );

            int scissorWidth = (int) (
                    (panelRight - panelLeft) * scale
            );

            int scissorHeight = (int) (
                    (viewportBottom - viewportTop) * scale
            );

            RenderSystem.enableScissor(
                    scissorX,
                    scissorY,
                    scissorWidth,
                    scissorHeight
            );

            // =================================================
            // SKILL CARDS
            // =================================================

            int contentTop = viewportTop - scrollOffset;

            if (twoColumns) {

                int gap = 16;

                int columnWidth = (panelWidth - 40 - gap) / 2;

                int leftX = panelLeft + 20;
                int rightX = leftX + columnWidth + gap;

                drawSkill(
                        matrices,
                        leftX,
                        contentTop,
                        columnWidth,
                        "§cKILL SKILL",
                        killLevel,
                        killCurrentXp,
                        killRequiredXp,
                        killTotalXp,
                        KillSkillLogic.STAGES,
                        KillSkillLogic.DESCRIPTIONS,
                        0xFFFFB34A
                );

                drawSkill(
                        matrices,
                        rightX,
                        contentTop,
                        columnWidth,
                        "§bWALK SKILL",
                        walkLevel,
                        walkCurrentXp,
                        walkRequiredXp,
                        walkTotalXp,
                        WalkSkillLogic.STAGES,
                        WalkSkillLogic.DESCRIPTIONS,
                        0xFF55CFFF
                );

            } else {

                int cardWidth = panelWidth - 30;
                int cardX = panelLeft + 15;

                drawSkill(
                        matrices,
                        cardX,
                        contentTop,
                        cardWidth,
                        "§cKILL SKILL",
                        killLevel,
                        killCurrentXp,
                        killRequiredXp,
                        killTotalXp,
                        KillSkillLogic.STAGES,
                        KillSkillLogic.DESCRIPTIONS,
                        0xFFFFB34A
                );

                drawSkill(
                        matrices,
                        cardX,
                        contentTop + CARD_HEIGHT + CARD_GAP,
                        cardWidth,
                        "§bWALK SKILL",
                        walkLevel,
                        walkCurrentXp,
                        walkRequiredXp,
                        walkTotalXp,
                        WalkSkillLogic.STAGES,
                        WalkSkillLogic.DESCRIPTIONS,
                        0xFF55CFFF
                );
            }

            RenderSystem.disableScissor();

            // =================================================
            // FOOTER
            // =================================================

            fill(
                    matrices,
                    panelLeft,
                    height - 43,
                    panelRight,
                    height - 10,
                    0xFF11141C
            );

            drawCentered(
                    matrices,
                    "Space: Double Jump   G: Air Dash   H: Blink Step   K: Close",
                    centerX,
                    height - 32,
                    0xFFBBBBBB
            );

            // =================================================
            // SCROLL BAR
            // =================================================

            if (maxScroll > 0) {

                int trackX = panelRight - 7;
                int trackTop = viewportTop;
                int trackBottom = viewportBottom;

                int trackHeight = trackBottom - trackTop;

                fill(
                        matrices,
                        trackX,
                        trackTop,
                        trackX + 3,
                        trackBottom,
                        0xFF444444
                );

                int contentHeight = twoColumns
                        ? CARD_HEIGHT
                        : CARD_HEIGHT * 2 + CARD_GAP;

                int thumbHeight = Math.max(
                        20,
                        trackHeight * trackHeight / contentHeight
                );

                int thumbTravel = trackHeight - thumbHeight;

                int thumbY = trackTop
                        + (int) (
                        (double) scrollOffset
                                / maxScroll
                                * thumbTravel
                );

                fill(
                        matrices,
                        trackX,
                        thumbY,
                        trackX + 3,
                        thumbY + thumbHeight,
                        0xFF55AADD
                );

                drawCentered(
                        matrices,
                        "§7Mouse Wheel: Scroll   "
                                + (scrollOffset + 1)
                                + " / "
                                + (maxScroll + 1),
                        centerX,
                        height - 47,
                        0xFFAAAAAA
                );
            }

            super.render(matrices, mouseX, mouseY, delta);
        }

        // =================================================
        // DRAW SKILL
        // =================================================

        private void drawSkill(
                MatrixStack matrices,
                int x,
                int y,
                int cardWidth,
                String title,
                int level,
                long currentXp,
                long requiredXp,
                long totalXp,
                String[] stages,
                String[] descriptions,
                int color
        ) {

            fill(
                    matrices,
                    x,
                    y,
                    x + cardWidth,
                    y + CARD_HEIGHT,
                    0xCC252A36
            );

            fill(
                    matrices,
                    x,
                    y,
                    x + cardWidth,
                    y + 2,
                    color
            );

            int textX = x + 10;
            int textWidth = Math.max(20, cardWidth - 20);

            textRenderer.drawWithShadow(
                    matrices,
                    textRenderer.trimToWidth(title, textWidth),
                    textX,
                    y + 12,
                    color
            );

            textRenderer.drawWithShadow(
                    matrices,
                    "Level: " + level + " / 100",
                    textX,
                    y + 31,
                    0xFFFFFFFF
            );

            textRenderer.drawWithShadow(
                    matrices,
                    textRenderer.trimToWidth(
                            "Total XP: " + totalXp,
                            textWidth
                    ),
                    textX,
                    y + 46,
                    0xFFCCCCCC
            );

            int barY = y + 65;
            int barWidth = textWidth;

            fill(
                    matrices,
                    textX,
                    barY,
                    textX + barWidth,
                    barY + 9,
                    0xFF444444
            );

            if (requiredXp > 0) {

                double progress = Math.max(
                        0.0,
                        Math.min(
                                1.0,
                                (double) currentXp / requiredXp
                        )
                );

                int filled = (int) (barWidth * progress);

                fill(
                        matrices,
                        textX,
                        barY,
                        textX + filled,
                        barY + 9,
                        color
                );
            }

            String xpText = requiredXp > 0
                    ? currentXp + " / " + requiredXp + " XP"
                    : "MAX LEVEL";

            textRenderer.drawWithShadow(
                    matrices,
                    textRenderer.trimToWidth(xpText, textWidth),
                    textX,
                    barY + 14,
                    0xFFAAAAAA
            );

            int listY = y + 94;
            int rowHeight = 27;

            for (int i = 0; i < 20; i++) {

                int requiredLevel = (i + 1) * 5;

                boolean unlocked = level >= requiredLevel;

                int rowY = listY + i * rowHeight;

                String stageName = i < stages.length
                        ? stages[i]
                        : "Unknown";

                String stageText =
                        (unlocked ? "§a✓ " : "§7🔒 ")
                                + "Lv."
                                + requiredLevel
                                + " "
                                + stageName;

                textRenderer.drawWithShadow(
                        matrices,
                        textRenderer.trimToWidth(
                                stageText,
                                textWidth
                        ),
                        textX,
                        rowY,
                        unlocked ? 0xFFFFFFFF : 0xFF888888
                );

                if (unlocked && i < descriptions.length) {

                    String description = "§7" + descriptions[i];

                    textRenderer.drawWithShadow(
                            matrices,
                            textRenderer.trimToWidth(
                                    description,
                                    textWidth - 8
                            ),
                            textX + 8,
                            rowY + 11,
                            0xFFAAAAAA
                    );
                }

                if (i < 19) {

                    fill(
                            matrices,
                            textX,
                            rowY + 24,
                            textX + textWidth,
                            rowY + 25,
                            0x33445566
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
        // MOUSE SCROLL
        // =================================================

        @Override
        public boolean mouseScrolled(
                double mouseX,
                double mouseY,
                double amount
        ) {

            if (mouseX >= panelLeft
                    && mouseX <= panelRight
                    && mouseY >= viewportTop
                    && mouseY <= viewportBottom
                    && maxScroll > 0) {

                scrollOffset -= (int) Math.round(
                        amount * SCROLL_SPEED
                );

                scrollOffset = Math.max(
                        0,
                        Math.min(scrollOffset, maxScroll)
                );

                return true;
            }

            return super.mouseScrolled(
                    mouseX,
                    mouseY,
                    amount
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
