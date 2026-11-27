package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class RedStalkerEntity extends SolarHostileEntity {
    private int sandAmbushCooldown = 72;

    public RedStalkerEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--sandAmbushCooldown <= 0) {
            if (target != null && distanceToSqr(target) >= 4.0 * 4.0 && distanceToSqr(target) <= 11.0 * 11.0) {
                leapToward(target, 0.43, 0.28);
                hurtTarget(target, 3.0f, 0.45, 0.18);
            }
            sandAmbushCooldown = 82;
        }
    }
}
