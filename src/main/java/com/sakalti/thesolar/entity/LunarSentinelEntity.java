package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class LunarSentinelEntity extends SolarHostileEntity {
    private int guardianPulseCooldown = 105;

    public LunarSentinelEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--guardianPulseCooldown <= 0) {
            if (target != null && distanceToSqr(target) <= 16.0 * 16.0) {
                hurtTarget(target, 7.0f, 0.30, 0.16);
            }
            guardianPulseCooldown = 115;
        }
    }
}
