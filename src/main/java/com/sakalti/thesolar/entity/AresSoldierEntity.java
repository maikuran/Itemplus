package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class AresSoldierEntity extends SolarHostileEntity {
    private int shieldRushCooldown = 88;

    public AresSoldierEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--shieldRushCooldown <= 0) {
            if (target != null && distanceToSqr(target) > 3.0 * 3.0 && distanceToSqr(target) <= 9.0 * 9.0) {
                leapToward(target, 0.40, 0.12);
            }
            if (target != null && distanceToSqr(target) <= 4.5 * 4.5) {
                hurtTarget(target, 5.0f, 0.72, 0.22);
            }
            shieldRushCooldown = 98;
        }
    }
}
