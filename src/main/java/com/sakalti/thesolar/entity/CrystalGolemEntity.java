package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class CrystalGolemEntity extends SolarHostileEntity {
    private int shardPulseCooldown = 115;

    public CrystalGolemEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--shardPulseCooldown <= 0) {
            if (target != null && distanceToSqr(target) <= 6.5 * 6.5) {
                pulseNearbyPlayers(5.5, 9.0f, 0.62, 0.25);
            }
            shardPulseCooldown = 125;
        }
    }
}
