package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class AresEntity extends SolarHostileEntity {
    private int quakeCooldown = 96;
    private int spearCooldown = 128;
    private int quakeWindup;
    private int spearWindup;

    public AresEntity(EntityType<? extends SolarHostileEntity> type, Level level) {
        super(type, level, 0.55);
    }

    @Override
    protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (target == null || !target.isAlive()) return;

        if (quakeWindup > 0) {
            if (--quakeWindup == 0 && distanceToSqr(target) <= 12.0 * 12.0) {
                pulseNearbyPlayers(9.0, isInSecondPhase() ? 18.0f : 12.0f, 1.2, 0.62);
            }
        } else if (--quakeCooldown <= 0) {
            quakeWindup = 28;
            beginAttackTelegraph(4, quakeWindup);
            quakeCooldown = isInSecondPhase() ? 62 : 102;
        }

        if (spearWindup > 0) {
            if (--spearWindup == 0) {
                double distance = distanceToSqr(target);
                if (distance >= 5.0 * 5.0 && distance <= 29.0 * 29.0) {
                    hurtTarget(target, isInSecondPhase() ? 15.0f : 10.0f, 0.72, 0.34);
                    target.igniteForSeconds(isInSecondPhase() ? 6.0F : 4.0F);
                }
            }
        } else if (--spearCooldown <= 0) {
            spearWindup = 24;
            beginAttackTelegraph(5, spearWindup);
            spearCooldown = isInSecondPhase() ? 78 : 132;
        }
    }
}
