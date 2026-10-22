
package com.sakalti.sakaplus.entity;

import com.sakalti.sakaplus.entity.BeamAttack;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.FlyingMoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.navigation.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.EnumSet;

public class KruziAntEntity extends HostileEntity {

    private static final int BEAM_INTERVAL = 30;
    private static final double ATTACK_RANGE = 20.0D;
    private static final int GOLD_BEAM_COLOR = 0xFFD700;

    private int beamCooldown = 30;

    public KruziAntEntity(
            EntityType<? extends KruziAntEntity> type,
            World world
    ) {
        super(type, world);

        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.setNoGravity(true);
        this.experiencePoints = 8;
    }

    public static DefaultAttributeContainer.Builder createKruziAntAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 64.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.32D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.1D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new KruziAntBeamGoal(this));
        this.targetSelector.add(
                1,
                new ActiveTargetGoal<>(
                        this,
                        PlayerEntity.class,
                        true
                )
        );
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new BirdNavigation(this, world);
    }

    @Override
    public void tick() {
        super.tick();

        this.setNoGravity(true);

        if (!this.getWorld().isClient) {
            if (this.beamCooldown > 0) {
                this.beamCooldown--;
            }
        }
    }

    private void fireGoldBeam(LivingEntity target) {
        if (this.getWorld().isClient || target == null || !target.isAlive()) {
            return;
        }

        BeamAttack.shootAt(
                this,
                target,
                1.5F,
                8.0F,
                GOLD_BEAM_COLOR
        );

        this.beamCooldown = BEAM_INTERVAL;
    }

    private static class KruziAntBeamGoal extends Goal {

        private final KruziAntEntity mob;

        KruziAntBeamGoal(KruziAntEntity mob) {
            this.mob = mob;
            this.setControls(EnumSet.of(
                    Goal.Control.MOVE,
                    Goal.Control.LOOK
            ));
        }

        @Override
        public boolean canStart() {
            return this.mob.getTarget() != null
                    && this.mob.getTarget().isAlive();
        }

        @Override
        public boolean shouldContinue() {
            return this.canStart();
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();

            if (target == null || !target.isAlive()) {
                this.mob.getNavigation().stop();
                return;
            }

            double distanceSq = this.mob.squaredDistanceTo(target);

            this.mob.getLookControl().lookAt(
                    target,
                    30.0F,
                    30.0F
            );

            // 遠いときは追跡し、近いときは射撃位置を保つ
            if (distanceSq > 64.0D) {
                this.mob.getNavigation().startMovingTo(
                        target,
                        1.15D
                );
            } else {
                this.mob.getNavigation().stop();
            }

            if (distanceSq <= ATTACK_RANGE * ATTACK_RANGE
                    && this.mob.beamCooldown <= 0) {
                this.mob.fireGoldBeam(target);
            }
        }
    }
}
