package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class MartianScoutEntity extends SolarHostileEntity {
    private int plasmaShotCooldown = 75;

    public MartianScoutEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--plasmaShotCooldown <= 0) {
            if (target != null && distanceToSqr(target) <= 20.0 * 20.0) {
                hurtTarget(target, 6.0f, 0.22, 0.12);
            }
            plasmaShotCooldown = 85;
        }
    }
}
