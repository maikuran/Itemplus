
package com.sakalti.sakaplus.entity;

import com.sakalti.sakaplus.sound.SakaplusSounds;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class WarperdunEntity extends PathAwareEntity implements Monster {

    private final ServerBossBar bossBar;

    // 衝撃波のクールダウン
    private int shockwaveTick = 0;

    // 衝撃波の間隔：100 ticks = 5秒
    private static final int SHOCKWAVE_INTERVAL = 100;

    // 衝撃波の範囲
    private static final double SHOCKWAVE_RANGE = 7.0D;

    // 衝撃波のダメージ
    private static final float SHOCKWAVE_DAMAGE = 14.0F;

    public WarperdunEntity(
            EntityType<? extends PathAwareEntity> entityType,
            World world
    ) {
        super(entityType, world);

        this.experiencePoints = 500;

        this.bossBar = new ServerBossBar(
                Text.literal("Warperdun"),
                BossBar.Color.RED,
                BossBar.Style.PROGRESS
        );
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1500.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 8.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6D);
    }

    // =========================
    // AI
    // =========================

    @Override
    protected void initGoals() {

        // 近接攻撃
        this.goalSelector.add(
                1,
                new MeleeAttackGoal(this, 1.2D, true)
        );

        // プレイヤーを見る
        this.goalSelector.add(
                2,
                new LookAtEntityGoal(this, PlayerEntity.class, 24.0F)
        );

        // 周囲を見回す
        this.goalSelector.add(
                3,
                new LookAroundGoal(this)
        );

        // プレイヤーを攻撃対象にする
        this.targetSelector.add(
                1,
                new ActiveTargetGoal<>(
                        this,
                        PlayerEntity.class,
                        true
                )
        );

        // 攻撃してきた相手に反撃
        this.targetSelector.add(
                2,
                new RevengeGoal(this).setGroupRevenge()
        );

        // その他の非Monster生物も攻撃
        this.targetSelector.add(
                3,
                new ActiveTargetGoal<>(
                        this,
                        MobEntity.class,
                        10,
                        true,
                        false,
                        target -> !(target instanceof Monster)
                )
        );
    }

    // =========================
    // Tick
    // =========================

    @Override
    public void tick() {
        super.tick();

        if (this.world.isClient) {
            return;
        }

        // ボスバー更新
        this.bossBar.setPercent(
                Math.max(
                        0.0F,
                        Math.min(
                                1.0F,
                                this.getHealth() / this.getMaxHealth()
                        )
                )
        );

        LivingEntity target = this.getTarget();

        if (target == null || !target.isAlive()) {
            shockwaveTick = 0;
            return;
        }

        shockwaveTick++;

        // 5秒ごとに虚空衝撃波
        if (shockwaveTick >= SHOCKWAVE_INTERVAL) {

            shockwaveTick = 0;

            // 対象が遠すぎる場合は発動しない
            if (this.squaredDistanceTo(target) <= 20.0D * 20.0D) {
                this.performShockwave();
            }
        }
    }

    // =========================
    // 虚空衝撃波
    // =========================

    private void performShockwave() {

        // 咆哮・衝撃波の音
        this.world.playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SakaplusSounds.BEAST_AMBIENT,
                SoundCategory.HOSTILE,
                3.0F,
                0.65F
        );

        // 周囲の対象を取得
        Box area = this.getBoundingBox().expand(SHOCKWAVE_RANGE);

        for (LivingEntity entity : this.world.getEntitiesByClass(
                LivingEntity.class,
                area,
                entity ->
                        entity != this
                        && entity.isAlive()
                        && !(entity instanceof Monster)
        )) {

            // 範囲外は無視
            if (this.squaredDistanceTo(entity)
                    > SHOCKWAVE_RANGE * SHOCKWAVE_RANGE) {
                continue;
            }

            // ダメージ
            entity.damage(
                    DamageSource.mob(this),
                    SHOCKWAVE_DAMAGE
            );

            // 吹き飛ばし方向
            double dx = entity.getX() - this.getX();
            double dz = entity.getZ() - this.getZ();

            double distance = Math.sqrt(dx * dx + dz * dz);

            if (distance > 0.001D) {
                dx /= distance;
                dz /= distance;

                // 強力なノックバック
                entity.takeKnockback(
                        1.8D,
                        -dx,
                        -dz
                );
            }

            // 少し上方向にも吹き飛ばす
            entity.addVelocity(0.0D, 0.35D, 0.0D);
            entity.velocityModified = true;
        }
    }

    // =========================
    // サウンド
    // =========================

    @Override
    protected SoundEvent getAmbientSound() {
        return SakaplusSounds.BEAST_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SakaplusSounds.BEAST_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SakaplusSounds.BEAST_DEATH;
    }

    // =========================
    // その他
    // =========================

    @Override
    public boolean canTarget(LivingEntity target) {
        return super.canTarget(target);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    // =========================
    // BossBar
    // =========================

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);

        if (!this.world.isClient) {
            this.bossBar.clearPlayers();
        }
    }
}
