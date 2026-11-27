package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class MoonBeastEntity extends SolarHostileEntity {
    private int lunarPounceCooldown = 92;

    public MoonBeastEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--lunarPounceCooldown <= 0) {
            if (target != null && distanceToSqr(target) >= 5.0 * 5.0 && distanceToSqr(target) <= 13.0 * 13.0) {
                leapToward(target, 0.52, 0.32);
            }
            lunarPounceCooldown = 102;
        }
    }
}
