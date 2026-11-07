
package com.sakalti.sakaplus.entity;

import java.util.EnumSet;
import com.sakalti.sakaplus.sound.SakaplusSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class GoblinEntity extends HostileEntity {

    private static final int BEAM_INTERVAL = 17;
    private static final double BEAM_RANGE = 20.0D;

    private static final int BEAM_COLOR = 0xAA00FF;
    private static final float BEAM_DAMAGE = 5.0F;
    private static final float BEAM_SPEED = 1.5F;

    private int beamCooldown = BEAM_INTERVAL;

    public GoblinEntity(
            EntityType<? extends GoblinEntity> type,
            World world
    ) {
        super(type, world);
        this.experiencePoints = 3;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0D);
    }

    @Override
    protected void initGoals() {

        // 自分を攻撃した相手に反撃
        this.targetSelector.add(
                1,
                new RevengeGoal(this)
        );

        // プレイヤー
        this.targetSelector.add(
                2,
                new ActiveTargetGoal<>(
                        this,
                        PlayerEntity.class,
                        true
                )
        );

        // 他の敵対モブ
        this.targetSelector.add(
                3,
                new ActiveTargetGoal<>(
                        this,
                        HostileEntity.class,
                        10,
                        true,
                        false,
                        target -> target != this
                )
        );

        // その他のモブ
        this.targetSelector.add(
                4,
                new ActiveTargetGoal<>(
                        this,
                        MobEntity.class,
                        10,
                        true,
                        false,
                        target -> target != this
                                && !(target instanceof GoblinEntity)
                                && !(target instanceof HostileEntity)
                )
        );

        // ビーム攻撃専用
        this.goalSelector.add(
                2,
                new GoblinBeamGoal(this)
        );
    }
    @Override
    protected SoundEvent getAmbientSound() {
        return SakaplusSounds.GoblinAmbient;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SakaplusSounds.GoblinHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SakaplusSounds.GoblinDeath;
    }
    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            return;
        }

        if (beamCooldown > 0) {
            beamCooldown--;
        }
    }

    private void firePurpleBeam(LivingEntity target) {

        if (this.getWorld().isClient
                || target == null
                || !target.isAlive()) {
            return;
        }

        BeamAttack.shootAt(
                this,
                target,
                BEAM_SPEED,
                BEAM_DAMAGE,
                BEAM_COLOR
        );

        beamCooldown = BEAM_INTERVAL;
    }

    private static class GoblinBeamGoal extends Goal {

        private final GoblinEntity mob;

        private GoblinBeamGoal(GoblinEntity mob) {
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

            if (mob.beamCooldown <= 0) {
                mob.firePurpleBeam(target);
            }
        }
    }
}
