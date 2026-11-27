package com.sakalti.thesolar;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;

/** Server-authoritative passive bonuses for lunar equipment. */
public final class LunarGearAbilities {
    private LunarGearAbilities() {}
    public static void register() {
        ServerTickEvents.END_LEVEL_TICK.register(world -> {
            for (Player player : world.players()) {
                Item held = player.getMainHandItem().getItem();
                if (held == DimensionMod.itemById("selenite_sword"))
                    player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 60, 0, true, false, true));
                if (held == DimensionMod.itemById("selenite_pickaxe") || held == DimensionMod.itemById("selenite_axe")
                        || held == DimensionMod.itemById("selenite_shovel") || held == DimensionMod.itemById("selenite_hoe"))
                    player.addEffect(new MobEffectInstance(MobEffects.HASTE, 60, 0, true, false, true));
                if (held == DimensionMod.itemById("lunarium_sword"))
                    player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 60, 1, true, false, true));
                if (held == DimensionMod.itemById("lunarium_pickaxe") || held == DimensionMod.itemById("lunarium_axe")
                        || held == DimensionMod.itemById("lunarium_shovel") || held == DimensionMod.itemById("lunarium_hoe"))
                    player.addEffect(new MobEffectInstance(MobEffects.HASTE, 60, 1, true, false, true));
                int selenite = count(player, "selenite");
                int lunarium = count(player, "lunarium");
                if (selenite >= 4) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 260, 0, true, false, true));
                    player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 80, 0, true, false, true));
                } else if (selenite >= 2) {
                    player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 60, 0, true, false, true));
                }
                if (lunarium >= 4) {
                    player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 100, 0, true, false, true));
                    player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 100, 0, true, false, true));
                } else if (lunarium >= 2) {
                    player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 60, 0, true, false, true));
                }
                if (world.dimension().equals(DimensionMod.MOON_DIMENSION)
                        && (player.getItemBySlot(EquipmentSlot.FEET).getItem() == DimensionMod.itemById("selenite_boots")
                        || player.getItemBySlot(EquipmentSlot.FEET).getItem() == DimensionMod.itemById("lunarium_boots"))) {
                    player.addEffect(new MobEffectInstance(MobEffects.SPEED, 80, 0, true, false, true));
                }
            }
        });
    }
    private static int count(Player player, String prefix) {
        int n = 0;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            Item item = player.getItemBySlot(slot).getItem();
            if (item == DimensionMod.itemById(prefix + "_helmet") || item == DimensionMod.itemById(prefix + "_chestplate")
                    || item == DimensionMod.itemById(prefix + "_leggings") || item == DimensionMod.itemById(prefix + "_boots")) n++;
        }
        return n;
    }
}
