package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class AstralWispEntity extends SolarHostileEntity {
    private int starBoltCooldown = 90;

    public AstralWispEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--starBoltCooldown <= 0) {
            if (target != null && distanceToSqr(target) <= 18.0 * 18.0) {
                hurtTarget(target, 5.5f, 0.18, 0.20);
            }
            starBoltCooldown = 100;
        }
    }
}
