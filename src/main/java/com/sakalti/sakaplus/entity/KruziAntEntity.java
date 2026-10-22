package com.sakalti.sakaplus.entity;

import com.sakalti.sakaplus.entity.BeamAttack;

import java.util.EnumSet;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.FlyingMoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.navigation.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class KruziAntEntity extends HostileEntity {

    private static final int BEAM_INTERVAL = 30;
    private static final double BEAM_RANGE = 20.0D;
    private static final double MELEE_RANGE = 3.0D;
    private static final int GOLD_BEAM_COLOR = 0xFFD700;

    private int beamCooldown = BEAM_INTERVAL;

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
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.1D);
    }

    @Override
    protected void initGoals() {
        // 通常の飛行・徘徊
        this.goalSelector.add(5, new FlyGoal(this, 1.0D));

        // 近距離では通常の近接攻撃で追跡する
        this.goalSelector.add(
                2,
                new MeleeAttackGoal(this, 1.15D, true)
        );

        // 中・遠距離では金色のビームを発射する
        this.goalSelector.add(3, new KruziAntBeamGoal(this));

        // プレイヤーを敵として認識
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

        if (!this.getWorld().isClient && this.beamCooldown > 0) {
            this.beamCooldown--;
        }
    }

    private void fireGoldBeam(LivingEntity target) {
        if (this.getWorld().isClient
                || target == null
                || !target.isAlive()) {
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

            // ビーム攻撃自体は移動を占有しない
            this.setControls(EnumSet.of(Goal.Control.LOOK));
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.mob.getTarget();

            if (target == null || !target.isAlive()) {
                return false;
            }

            double distanceSq = this.mob.squaredDistanceTo(target);

            return distanceSq <= BEAM_RANGE * BEAM_RANGE
                    && distanceSq > MELEE_RANGE * MELEE_RANGE
                    && this.mob.beamCooldown <= 0;
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity target = this.mob.getTarget();

            return target != null
                    && target.isAlive()
                    && this.mob.squaredDistanceTo(target)
                            <= BEAM_RANGE * BEAM_RANGE
                    && this.mob.squaredDistanceTo(target)
                            > MELEE_RANGE * MELEE_RANGE;
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();

            if (target == null || !target.isAlive()) {
                return;
            }

            this.mob.getLookControl().lookAt(
                    target,
                    30.0F,
                    30.0F
            );

            double distanceSq = this.mob.squaredDistanceTo(target);

            if (distanceSq <= BEAM_RANGE * BEAM_RANGE
                    && distanceSq > MELEE_RANGE * MELEE_RANGE
                    && this.mob.beamCooldown <= 0) {
                this.mob.fireGoldBeam(target);
            }
        }
    }
}
