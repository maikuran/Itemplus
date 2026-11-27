package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class BloodWyrmEntity extends SolarHostileEntity {
    private int burrowRushCooldown = 120;

    public BloodWyrmEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--burrowRushCooldown <= 0) {
            if (target != null && distanceToSqr(target) <= 13.0 * 13.0) {
                leapToward(target, 0.55, 0.34);
                pulseNearbyPlayers(3.5, 8.0f, 0.70, 0.30);
            }
            burrowRushCooldown = 135;
        }
    }
}
