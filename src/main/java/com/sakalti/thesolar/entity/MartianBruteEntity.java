package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class MartianBruteEntity extends SolarHostileEntity {
    private int redQuakeCooldown = 125;

    public MartianBruteEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--redQuakeCooldown <= 0) {
            if (target != null && distanceToSqr(target) <= 7.5 * 7.5) {
                pulseNearbyPlayers(6.0, 11.0f, 0.90, 0.30);
            }
            redQuakeCooldown = 138;
        }
    }
}
