package com.sakalti.thesolar.client;

import com.sakalti.thesolar.DimensionMod;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** Client quest journal. Progress is read from the player's synced vanilla advancement progress. */
public final class SolarQuestScreen extends Screen {
    private record Quest(String title, String detail, boolean complete) {}

    public SolarQuestScreen() {
        super(Component.translatable("screen.thesolar.quests"));
    }

    @Override
    protected void init() {
        int panelWidth = Math.min(430, this.width - 32);
        int left = (this.width - panelWidth) / 2;
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> this.onClose())
                .bounds(left + panelWidth - 92, this.height - 34, 84, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        if (this.minecraft == null || this.minecraft.player == null) return;

        int panelWidth = Math.min(430, this.width - 32);
        int left = (this.width - panelWidth) / 2;
        int top = 24;
        int right = left + panelWidth;
        int bottom = this.height - 48;

        graphics.fill(left - 2, top - 2, right + 2, bottom + 2, 0xD90A101B);
        graphics.fill(left, top, right, top + 2, 0xFF7DDCFF);
        graphics.fill(left, bottom - 2, right, bottom, 0xFF7DDCFF);
        graphics.text(this.font, Component.translatable("screen.thesolar.quests.title").getString(), left + 14, top + 12, 0xFFFFFFFF, true);

        var player = this.minecraft.player;
        int level = player.experienceLevel;
        String rank = level >= 41 ? "Solar Ascendant" : level >= 26 ? "Celestial Knight" : level >= 16 ? "Relic Hunter" : level >= 6 ? "Explorer" : "Initiate";
        boolean lunarSeal = hasItem(player.getInventory(), DimensionMod.LUNAR_SEAL);
        boolean martianSigil = hasItem(player.getInventory(), DimensionMod.MARTIAN_SIGIL);
        boolean inMoon = player.level().dimension().equals(DimensionMod.MOON_DIMENSION);
        boolean inMars = player.level().dimension().equals(DimensionMod.MARS_DIMENSION);

        // Advancement progress persists after leaving a dimension or storing a boss relic.
        boolean starCollector = isAdvancementDone("story/star_collector") || hasItem(player.getInventory(), DimensionMod.STAR_DUST);
        boolean reachedMoon = isAdvancementDone("story/to_the_moon") || inMoon;
        boolean seleneDefeated = isAdvancementDone("story/lunar_guardian") || lunarSeal;
        boolean reachedMars = isAdvancementDone("story/red_planet") || inMars;
        boolean aresDefeated = isAdvancementDone("story/fallen_god") || martianSigil;
        boolean solarComplete = isAdvancementDone("story/the_solar") || (seleneDefeated && aresDefeated);
        int divinity = Math.max(0, level / 10) + (seleneDefeated ? 2 : 0) + (aresDefeated ? 3 : 0);

        int y = top + 36;
        graphics.text(this.font, Component.translatable("screen.thesolar.quests.rank", rank, level).getString(), left + 14, y, 0xFFFFD98A, true);
        y += 15;
        graphics.text(this.font, Component.translatable("screen.thesolar.quests.divinity", divinity).getString(), left + 14, y, 0xFFB6CFFF, true);
        y += 15;
        graphics.text(this.font, Component.translatable("screen.thesolar.quests.xp").getString(), left + 14, y, 0xFF9FB8D4, false);
        y += 13;
        int xpBarWidth = right - left - 28;
        int xpFill = (int) (xpBarWidth * Math.max(0.0F, Math.min(1.0F, player.experienceProgress)));
        graphics.fill(left + 14, y, left + 14 + xpBarWidth, y + 5, 0xFF28384A);
        graphics.fill(left + 14, y, left + 14 + xpFill, y + 5, 0xFF73D8F5);
        y += 14;
        String world = inMoon ? "Moon" : inMars ? "Mars" : "Overworld / Other";
        graphics.text(this.font, Component.translatable("screen.thesolar.quests.location", world).getString(), left + 14, y, 0xFFB7C5D8, false);
        y += 22;

        graphics.fill(left + 12, y, right - 12, y + 1, 0xFF43566B);
        y += 10;
        graphics.text(this.font, Component.translatable("screen.thesolar.quests.main_story").getString(), left + 14, y, 0xFF7DDCFF, true);
        y += 18;

        List<Quest> quests = new ArrayList<>();
        quests.add(new Quest("01  The First Signal", "Collect Stardust from the Overworld", starCollector));
        quests.add(new Quest("02  The Silent Moon", "Reach the Moon and explore its ruins", reachedMoon));
        quests.add(new Quest("03  Selene, Moon Guardian", "Defeat Selene and claim the Lunar Seal", seleneDefeated));
        quests.add(new Quest("04  The Red Frontier", "Reach the Martian surface", reachedMars));
        quests.add(new Quest("05  Ares, Fallen King", "Defeat Ares and claim the Martian Sigil", aresDefeated));
        quests.add(new Quest("06  The Solar", "Unite both divine seals", solarComplete));

        for (Quest quest : quests) {
            int color = quest.complete() ? 0xFF8FE3A1 : 0xFFD0D8E3;
            String mark = quest.complete() ? "[COMPLETE] " : "[LOCKED]   ";
            graphics.text(this.font, mark + quest.title(), left + 14, y, color, true);
            y += 12;
            graphics.text(this.font, quest.detail(), left + 28, y, 0xFF9AAABD, false);
            y += 19;
            if (y > bottom - 30) break;
        }
        graphics.text(this.font, Component.translatable("screen.thesolar.quests.controls").getString(), left + 14, bottom - 18, 0xFF7D8FA6, false);
    }

    private boolean isAdvancementDone(String path) {
        if (this.minecraft == null || this.minecraft.getConnection() == null) return false;
        var advancements = this.minecraft.getConnection().getAdvancements();
        var holder = advancements.get(DimensionMod.id(path));
        if (holder == null) return false;
        var progress = advancements.progress().get(holder);
        return progress != null && progress.isDone();
    }

    private static boolean hasItem(Inventory inventory, Item item) {
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty() && stack.is(item)) return true;
        }
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
