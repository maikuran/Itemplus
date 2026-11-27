package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class AncientAutomatonEntity extends SolarHostileEntity {
    private int seismicPulseCooldown = 135;

    public AncientAutomatonEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--seismicPulseCooldown <= 0) {
            if (target != null && distanceToSqr(target) <= 7.0 * 7.0) {
                pulseNearbyPlayers(6.0, 10.0f, 0.85, 0.32);
            }
            seismicPulseCooldown = 145;
        }
    }
}
