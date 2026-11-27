package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class MoonStalkerEntity extends SolarHostileEntity {
    private int ambushCooldown = 85;

    public MoonStalkerEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--ambushCooldown <= 0) {
            if (target != null && distanceToSqr(target) > 5.0 * 5.0 && distanceToSqr(target) < 15.0 * 15.0) {
                leapToward(target, 0.48, 0.16);
            }
            ambushCooldown = 95;
        }
    }
}
