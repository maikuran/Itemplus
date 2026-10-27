
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
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
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

public class CryofreezerEntity extends HostileEntity {

    private static final int BEAM_INTERVAL = 30;
    private static final double BEAM_RANGE = 20.0D;
    private static final double MELEE_RANGE = 3.0D;

    private static final int ICE_BEAM_COLOR = 0x66CCFF;

    private static final double AURA_RANGE = 4.0D;
    private static final int AURA_INTERVAL = 60;

    private int beamCooldown = BEAM_INTERVAL;
    private int auraCooldown = AURA_INTERVAL;

    public CryofreezerEntity(
            EntityType<? extends CryofreezerEntity> type,
            World world
    ) {
        super(type, world);

        this.moveControl = new FlightMoveControl(this, 10, true);
        this.setNoGravity(true);
        this.experiencePoints = 12;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 76.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.32D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.15D);
    }

    @Override
    protected void initGoals() {

        this.targetSelector.add(
                1,
                new ActiveTargetGoal<>(this, PlayerEntity.class, true)
        );

        this.goalSelector.add(
                2,
                new MeleeAttackGoal(this, 1.15D, true)
        );

        this.goalSelector.add(
                3,
                new CryofreezerBeamGoal(this)
        );

        this.goalSelector.add(
                5,
                new FlyGoal(this, 1.0D)
        );
    }

    // -----------------------------
    // Sounds
    // -----------------------------

    @Override
    protected SoundEvent getAmbientSound() {
        return SakaplusSounds.ShyronAmbient;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SakaplusSounds.ShyronHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SakaplusSounds.ShyronDeath;
    }

    // -----------------------------
    // Navigation
    // -----------------------------

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new BirdNavigation(this, world);
    }

    // -----------------------------
    // Tick
    // -----------------------------

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

        if (auraCooldown > 0) {
            auraCooldown--;
        }

        // 氷結オーラ
        if (auraCooldown <= 0) {
            applyCryoAura();
            auraCooldown = AURA_INTERVAL;
        }
    }

    // -----------------------------
    // Melee attack
    // -----------------------------

    @Override
    public boolean tryAttack(Entity target) {

        boolean successful = super.tryAttack(target);

        if (successful && target instanceof LivingEntity) {

            LivingEntity living = (LivingEntity) target;

            // 近接攻撃による氷結
            living.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.SLOWNESS,
                            40,
                            1
                    )
            );
        }

        return successful;
    }

    // -----------------------------
    // Ice Beam
    // -----------------------------

    private void fireIceBeam(LivingEntity target) {

        if (this.getWorld().isClient
                || target == null
                || !target.isAlive()) {
            return;
        }

        BeamAttack.shootAt(
                this,
                target,
                1.5F,
                9.6F,
                ICE_BEAM_COLOR
        );

        // 氷結ビームの追加効果
        target.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.SLOWNESS,
                        80,
                        3
                )
        );

        // 弱体化
        target.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.WEAKNESS,
                        60,
                        0
                )
        );

        beamCooldown = BEAM_INTERVAL;
    }

    // -----------------------------
    // Cryo Aura
    // -----------------------------

    private void applyCryoAura() {

        if (this.getWorld().isClient) {
            return;
        }

        for (Entity entity : this.getWorld().getOtherEntities(
                this,
                this.getBoundingBox().expand(AURA_RANGE),
                entity -> entity instanceof LivingEntity
                        && entity.isAlive()
                        && !this.isTeammate(entity)
        )) {

            if (!(entity instanceof LivingEntity)) {
                continue;
            }

            LivingEntity living = (LivingEntity) entity;

            // 周囲の敵を凍結
            living.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.SLOWNESS,
                            80,
                            1
                    )
            );
        }
    }

    // -----------------------------
    // Beam Goal
    // -----------------------------

    private static class CryofreezerBeamGoal extends Goal {

        private final CryofreezerEntity mob;

        private CryofreezerBeamGoal(CryofreezerEntity mob) {
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

                mob.fireIceBeam(target);
            }
        }
    }
}
