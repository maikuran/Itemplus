
package com.sakalti.sakaplus.entity;

import java.util.EnumSet;

import com.sakalti.sakaplus.sound.SakaplusSounds;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class KryosonEntity extends HostileEntity {

    private static final int BEAM_INTERVAL = 10;
    private static final double BEAM_RANGE = 20.0D;

    private static final int BEAM_COLOR = 0xFFFF00;

    private int beamCooldown = BEAM_INTERVAL;

    public KryosonEntity(
            EntityType<? extends KryosonEntity> type,
            World world
    ) {
        super(type, world);

        this.moveControl = new FlightMoveControl(this, 10, true);
        this.setNoGravity(true);

        this.experiencePoints = 20;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 160.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.32D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.2D);
    }

    @Override
    protected void initGoals() {

        // 攻撃されたプレイヤーに反撃する
        this.targetSelector.add(
                1,
                new ActiveTargetGoal<>(
                        this,
                        PlayerEntity.class,
                        10,
                        true,
                        false,
                        player -> this.getAttacker() == player
                )
        );

        // 黄色ビーム攻撃
        this.goalSelector.add(
                2,
                new KryosonBeamGoal(this)
        );

        // 飛行
        this.goalSelector.add(
                5,
                new FlyGoal(this, 1.0D)
        );
    }

    // Sounds

    @Override
    protected SoundEvent getAmbientSound() {
        return SakaplusSounds.KryosonAmbient;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SakaplusSounds.KryosonHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SakaplusSounds.KryosonDeath;
    }

    // Navigation

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new BirdNavigation(this, world);
    }

    // Swimming and flying

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    public void travel(net.minecraft.util.math.Vec3d movementInput) {
        if (this.isTouchingWater()) {
            this.setNoGravity(false);
            super.travel(movementInput);
            this.setNoGravity(true);
        } else {
            this.setNoGravity(true);
            super.travel(movementInput);
        }
    }

    // Tick

    @Override
    public void tick() {
        super.tick();

        this.setNoGravity(true);

        if (this.getWorld().isClient) {
            return;
        }

        if (beamCooldown > 0) {
            beamCooldown--;
        }
    }

    // No melee attack

    @Override
    public boolean tryAttack(Entity target) {
        return false;
    }

    // Yellow Beam

    private void fireYellowBeam(LivingEntity target) {

        if (this.getWorld().isClient
                || target == null
                || !target.isAlive()) {
            return;
        }

        BeamAttack.shootAt(
                this,
                target,
                16.0F,
                10.5F,
                BEAM_COLOR
        );

        beamCooldown = BEAM_INTERVAL;
    }

    // Beam Goal

    private static class KryosonBeamGoal extends Goal {

        private final KryosonEntity mob;

        private KryosonBeamGoal(KryosonEntity mob) {
            this.mob = mob;

            this.setControls(
                    EnumSet.of(
                            Goal.Control.LOOK
                    )
            );
        }

        @Override
        public boolean canStart() {

            LivingEntity target = mob.getTarget();

            if (target == null || !target.isAlive()) {
                return false;
            }

            return mob.squaredDistanceTo(target)
                    <= BEAM_RANGE * BEAM_RANGE
                    && mob.beamCooldown <= 0;
        }

        @Override
        public boolean shouldContinue() {

            LivingEntity target = mob.getTarget();

            return target != null
                    && target.isAlive()
                    && mob.squaredDistanceTo(target)
                    <= BEAM_RANGE * BEAM_RANGE;
        }

        @Override
        public void tick() {

            LivingEntity target = mob.getTarget();

            if (target == null || !target.isAlive()) {
                return;
            }

            mob.getLookControl().lookAt(
                    target,
                    30.0F,
                    30.0F
            );

            if (mob.beamCooldown <= 0
                    && mob.squaredDistanceTo(target)
                    <= BEAM_RANGE * BEAM_RANGE) {

                mob.fireYellowBeam(target);
            }
        }
    }
}
