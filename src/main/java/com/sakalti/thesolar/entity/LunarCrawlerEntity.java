package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class LunarCrawlerEntity extends SolarHostileEntity {
    private int pounceCooldown = 55;

    public LunarCrawlerEntity(EntityType<? extends SolarHostileEntity> type, Level level) { super(type, level); }

    @Override protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (--pounceCooldown <= 0) {
            if (target != null && distanceToSqr(target) >= 3.0 * 3.0 && distanceToSqr(target) <= 9.0 * 9.0) {
                leapToward(target, 0.34, 0.22);
            }
            pounceCooldown = 65;
        }
    }
}
