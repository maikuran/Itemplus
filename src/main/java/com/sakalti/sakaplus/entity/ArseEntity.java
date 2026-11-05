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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class ArseEntity extends HostileEntity {

    // ビーム設定
    private static final int BEAM_INTERVAL = 15;
    private static final double BEAM_RANGE = 20.0D;
    private static final double MELEE_RANGE = 0.5D;

    // 固体砒素の金属灰色
    private static final int ARSENIC_BEAM_COLOR = 0xA6A6A6;

    // 有毒フィールド
    private static final double TOXIC_RANGE = 4.0D;
    private static final int TOXIC_INTERVAL = 20;

    private int beamCooldown = BEAM_INTERVAL;
    private int toxicCooldown = TOXIC_INTERVAL;

    public ArseEntity(
            EntityType<? extends ArseEntity> type,
            World world
    ) {
        super(type, world);

        this.moveControl = new FlightMoveControl(this, 10, true);
        this.setNoGravity(true);
        this.experiencePoints = 15;
    }
    public boolean isFireImmune() {
      return true;
    }
　　
    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 112.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.32D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.15D);
    }

    @Override
    protected void initGoals() {

        this.targetSelector.add(
                1,
                new ActiveTargetGoal<>(this, PlayerEntity.class, true)
        );

        this.goalSelector.add(
                3,
                new ArseBeamGoal(this)
        );

        this.goalSelector.add(
                5,
                new FlyGoal(this, 1.0D)
        );
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

        if (toxicCooldown > 0) {
            toxicCooldown--;
        }

        if (toxicCooldown <= 0) {
            applyToxicField();
            toxicCooldown = TOXIC_INTERVAL;
        }
    }

    // Arsenic Beam

    private void fireArsenicBeam(LivingEntity target) {

        if (this.getWorld().isClient
                || target == null
                || !target.isAlive()) {
            return;
        }

        BeamAttack.shootAt(
                this,
                target,
                1.5F,
                12.0F,
                ARSENIC_BEAM_COLOR
        );

        beamCooldown = BEAM_INTERVAL;
    }

    // Toxic Field

    private void applyToxicField() {

        if (this.getWorld().isClient) {
            return;
        }

        for (Entity entity : this.getWorld().getOtherEntities(
                this,
                this.getBoundingBox().expand(TOXIC_RANGE),
                entity -> entity instanceof LivingEntity
                        && entity != this
                        && entity.isAlive()
                        && !this.isTeammate(entity)
        )) {

            LivingEntity living = (LivingEntity) entity;

            living.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.POISON,
                            120,
                            1
                    )
            );
        }
    }

    // Beam Goal

    private static class ArseBeamGoal extends Goal {

        private final ArseEntity mob;

        private ArseBeamGoal(ArseEntity mob) {
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

            double distanceSq = mob.squaredDistanceTo(target);

            return distanceSq <= BEAM_RANGE * BEAM_RANGE
                    && distanceSq > MELEE_RANGE * MELEE_RANGE
                    && mob.beamCooldown <= 0;
        }

        @Override
        public boolean shouldContinue() {

            LivingEntity target = mob.getTarget();

            if (target == null || !target.isAlive()) {
                return false;
            }

            double distanceSq = mob.squaredDistanceTo(target);

            return distanceSq <= BEAM_RANGE * BEAM_RANGE
                    && distanceSq > MELEE_RANGE * MELEE_RANGE;
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

            double distanceSq = mob.squaredDistanceTo(target);

            if (distanceSq <= BEAM_RANGE * BEAM_RANGE
                    && distanceSq > MELEE_RANGE * MELEE_RANGE
                    && mob.beamCooldown <= 0) {

                mob.fireArsenicBeam(target);
            }
        }
    }
}
