package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class MartianBeastEntity extends SolarHostileEntity {
    private int crimsonChargeCooldown = 105;

    public MartianBeastEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--crimsonChargeCooldown <= 0) {
            if (target != null && distanceToSqr(target) >= 4.0 * 4.0 && distanceToSqr(target) <= 12.0 * 12.0) {
                leapToward(target, 0.58, 0.24);
            }
            if (target != null && distanceToSqr(target) <= 5.0 * 5.0) {
                hurtTarget(target, 8.0f, 0.80, 0.24);
            }
            crimsonChargeCooldown = 118;
        }
    }
}
