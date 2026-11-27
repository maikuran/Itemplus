package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class CrimsonMageEntity extends SolarHostileEntity {
    private int flameLanceCooldown = 100;

    public CrimsonMageEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--flameLanceCooldown <= 0) {
            if (target != null && distanceToSqr(target) <= 17.0 * 17.0) {
                if (hurtTarget(target, 7.0f, 0.18, 0.12)) target.igniteForSeconds(4.0F);
            }
            flameLanceCooldown = 112;
        }
    }
}
