package com.sakalti.sakaplus.entity;

import java.util.EnumSet;

import com.sakalti.sakaplus.sound.SakaplusSounds;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class QuartzehEntity extends HostileEntity {

    // ビーム設定
    private static final int BEAM_INTERVAL = 22;
    private static final double BEAM_RANGE = 20.0D;
    private static final int BEAM_COLOR = 0xFFFFFF;

    private int beamCooldown = BEAM_INTERVAL;

    public QuartzehEntity(
            EntityType<? extends QuartzehEntity> type,
            World world
    ) {
        super(type, world);

        this.moveControl = new FlightMoveControl(this, 10, true);
        this.setNoGravity(true);
        this.experiencePoints = 15;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.32D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 10.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.15D);
    }

    @Override
    protected void initGoals() {

        // 近接攻撃Goalは登録しない
        this.goalSelector.add(
                3,
                new QuartzehBeamGoal(this)
        );

        this.goalSelector.add(
                5,
                new FlyGoal(this, 1.0D)
        );
    }

    // 攻撃してきた敵をターゲットにする
    @Override
    public boolean damage(DamageSource source, float amount) {

        boolean damaged = super.damage(source, amount);

        if (damaged && !this.getWorld().isClient) {

            Entity attacker = source.getAttacker();

            if (attacker instanceof LivingEntity
                    && attacker != this
                    && attacker.isAlive()
                    && !this.isTeammate(attacker)) {

                this.setTarget((LivingEntity) attacker);
            }
        }

        return damaged;
    }

    // Sounds

    @Override
    protected SoundEvent getAmbientSound() {
        return SakaplusSounds.ArseAmbient;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SakaplusSounds.ArseHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SakaplusSounds.ArseDeath;
    }

    // Navigation

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new BirdNavigation(this, world);
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

    // White Beam

    private void fireWhiteBeam(LivingEntity target) {

        if (this.getWorld().isClient
                || target == null
                || !target.isAlive()) {
            return;
        }

        BeamAttack.shootAt(
                this,
                target,
                1.5F,
                10.0F,
                BEAM_COLOR
        );

        beamCooldown = BEAM_INTERVAL;
    }

    // Beam Goal

    private static class QuartzehBeamGoal extends Goal {

        private final QuartzehEntity mob;

        private QuartzehBeamGoal(QuartzehEntity mob) {
            this.mob = mob;

            this.setControls(
                    EnumSet.of(Goal.Control.LOOK)
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

            if (mob.squaredDistanceTo(target)
                    <= BEAM_RANGE * BEAM_RANGE
                    && mob.beamCooldown <= 0) {

                mob.fireWhiteBeam(target);
            }
        }
    }
}
