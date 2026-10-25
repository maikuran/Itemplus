package com.sakalti.sakaplus.entity;

import java.util.EnumSet;

import com.sakalti.sakaplus.sound.SakaplusSounds; // サウンドクラスのインポートを追加
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
import net.minecraft.entity.damage.DamageSource; // Hurtメソッド用のインポートを追加
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent; // SoundEvent用のインポートを追加
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

        this.moveControl = new FlightMoveControl(this, 10, true);
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
        // プレイヤーを敵として認識
        this.targetSelector.add(
                1,
                new ActiveTargetGoal<>(this, PlayerEntity.class, true)
        );

        // 近距離での近接攻撃
        this.goalSelector.add(
                2,
                new MeleeAttackGoal(this, 1.15D, true)
        );

        // 中・遠距離での金色ビーム攻撃
        this.goalSelector.add(3, new KruziAntBeamGoal(this));

        // 通常の飛行行動
        this.goalSelector.add(5, new FlyGoal(this, 1.0D));
    }

    // --- ここからサウンド設定の追加 ---

    @Override
    protected SoundEvent getAmbientSound() {
        // 通常時の鳴き声（ランダムに2種再生される）
        return SakaplusSounds.KruziantAmbient;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // ダメージを受けた時の音
        return SakaplusSounds.KruziantHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // 倒れた時の音
        return SakaplusSounds.KruziantDeath;
    }

    // --- ここまでサウンド設定の追加 ---

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

        private KruziAntBeamGoal(KruziAntEntity mob) {
            this.mob = mob;

            // ビーム攻撃中も移動を妨げない
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

            if (target == null || !target.isAlive()) {
                return false;
            }

            double distanceSq = this.mob.squaredDistanceTo(target);

            return distanceSq <= BEAM_RANGE * BEAM_RANGE
                    && distanceSq > MELEE_RANGE * MELEE_RANGE;
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
