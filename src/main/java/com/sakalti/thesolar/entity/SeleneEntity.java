package com.sakalti.thesolar.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public final class SeleneEntity extends SolarHostileEntity {
    private int moonbeamCooldown = 72;
    private int crystalWaveCooldown = 145;
    private int lunarMendCooldown = 230;
    private int moonbeamWindup;
    private int crystalWaveWindup;
    private int lunarMendWindup;

    public SeleneEntity(EntityType<? extends SolarHostileEntity> type, Level level) {
        super(type, level, 0.45);
    }

    @Override
    protected void tickSpecialAbility() {
        LivingEntity target = combatTarget();
        if (target == null || !target.isAlive()) return;

        if (moonbeamWindup > 0) {
            if (--moonbeamWindup == 0 && distanceToSqr(target) <= 28.0 * 28.0) {
                hurtTarget(target, isInSecondPhase() ? 14.0f : 9.0f, 0.42, 0.28);
            }
        } else if (--moonbeamCooldown <= 0) {
            moonbeamWindup = 26;
            beginAttackTelegraph(1, moonbeamWindup);
            moonbeamCooldown = isInSecondPhase() ? 54 : 82;
        }

        if (crystalWaveWindup > 0) {
            if (--crystalWaveWindup == 0 && distanceToSqr(target) <= 10.0 * 10.0) {
                pulseNearbyPlayers(7.0, isInSecondPhase() ? 12.0f : 8.0f, 0.95, 0.46);
            }
        } else if (--crystalWaveCooldown <= 0) {
            crystalWaveWindup = 22;
            beginAttackTelegraph(2, crystalWaveWindup);
            crystalWaveCooldown = isInSecondPhase() ? 108 : 154;
        }

        if (lunarMendWindup > 0) {
            if (--lunarMendWindup == 0 && getHealth() < getMaxHealth() * 0.82f) {
                heal(isInSecondPhase() ? 20.0f : 12.0f);
            }
        } else if (--lunarMendCooldown <= 0) {
            if (getHealth() < getMaxHealth() * 0.82f) {
                lunarMendWindup = 30;
                beginAttackTelegraph(3, lunarMendWindup);
            }
            lunarMendCooldown = isInSecondPhase() ? 190 : 245;
        }
    }
}
