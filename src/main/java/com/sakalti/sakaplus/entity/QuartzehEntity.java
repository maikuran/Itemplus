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
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
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

    // 飛行モブのため落下ダメージを無効化
    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
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
        // 行動AI
        this.goalSelector.add(2, new QuartzehBeamGoal(this));
        this.goalSelector.add(5, new FlyGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));

        // ターゲット選択AI（プレイヤーを自動索敵）
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    // 攻撃してきた敵をターゲットにする
    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean damaged = super.damage(source, amount);

        // 1.19.2では this.world を使用
        if (damaged && !this.world.isClient) {
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
        BirdNavigation navigation = new BirdNavigation(this, world);
        navigation.setCanPathThroughDoors(false);
        navigation.setCanSwim(true);
        navigation.setCanEnterOpenDoors(true);
        return navigation;
    }

    // Tick
    @Override
    public void tick() {
        super.tick();
        this.setNoGravity(true);

        if (this.world.isClient) {
            return;
        }

        if (beamCooldown > 0) {
            beamCooldown--;
        }
    }

    // White Beam
    private void fireWhiteBeam(LivingEntity target) {
        if (this.world.isClient || target == null || !target.isAlive()) {
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

    // Beam Goal（攻撃＋接近AI）
    private static class QuartzehBeamGoal extends Goal {

        private final QuartzehEntity mob;

        private QuartzehBeamGoal(QuartzehEntity mob) {
            this.mob = mob;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        @Override
        public boolean canStart() {
            LivingEntity target = mob.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean shouldContinue() {
            return canStart();
        }

        @Override
        public void stop() {
            mob.getNavigation().stop();
        }

        @Override
        public void tick() {
            LivingEntity target = mob.getTarget();
            if (target == null || !target.isAlive()) {
                return;
            }

            double distanceSq = mob.squaredDistanceTo(target);
            boolean canSee = mob.canSee(target);

            mob.getLookControl().lookAt(target, 30.0F, 30.0F);

            // 射程外、または壁の裏にいる場合はターゲットに近づく
            if (distanceSq > (BEAM_RANGE * BEAM_RANGE) * 0.75D || !canSee) {
                mob.getNavigation().startMovingTo(target, 1.2D);
            } else {
                mob.getNavigation().stop();
            }

            // 射程内かつ視線が通っていればビーム発射
            if (distanceSq <= BEAM_RANGE * BEAM_RANGE && canSee && mob.beamCooldown <= 0) {
                mob.fireWhiteBeam(target);
            }
        }
    }
}
