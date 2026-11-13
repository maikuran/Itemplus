
package com.sakalti.sakaplus.entity;

import com.sakalti.sakaplus.registry.ModBeamEntities;
import com.sakalti.sakaplus.sound.SakaplusSounds;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VolcanicKingEntity extends PathAwareEntity implements Monster {

    private final ServerBossBar bossBar;

    // =========================
    // ナイフ攻撃
    // =========================

    private static final int KNIFE_INTERVAL = 15;
    private static final int KNIFE_COUNT = 3;
    private static final double KNIFE_RANGE = 48.0D;
    private static final double KNIFE_SPEED = 1.8D;

    private int knifeCooldown = 0;
    private int knivesRemaining = 0;

    // =========================
    // 衝撃波
    // =========================

    private static final int SHOCKWAVE_INTERVAL = 100;
    private static final double SHOCKWAVE_RANGE = 7.0D;
    private static final float SHOCKWAVE_DAMAGE = 12.5F;

    private int shockwaveCooldown = 0;

    // =========================
    // コンストラクタ
    // =========================

    public VolcanicKingEntity(
            EntityType<? extends VolcanicKingEntity> entityType,
            World world
    ) {
        super(entityType, world);

        this.experiencePoints = 1550;

        this.bossBar = new ServerBossBar(
                Text.literal("Volcanic King"),
                BossBar.Color.RED,
                BossBar.Style.PROGRESS
        );
    }

    // =========================
    // Attributes
    // =========================

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1950.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 15.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 14.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.42D);
    }

    // =========================
    // AI
    // =========================

    @Override
    protected void initGoals() {

        // 高速で近接攻撃
        this.goalSelector.add(
                1,
                new net.minecraft.entity.ai.goal.MeleeAttackGoal(
                        this,
                        1.5D,
                        true
                )
        );

        // プレイヤーを見る
        this.goalSelector.add(
                2,
                new net.minecraft.entity.ai.goal.LookAtEntityGoal(
                        this,
                        PlayerEntity.class,
                        32.0F
                )
        );

        // 周囲を見る
        this.goalSelector.add(
                3,
                new net.minecraft.entity.ai.goal.LookAroundGoal(this)
        );

        // プレイヤーを攻撃対象にする
        this.targetSelector.add(
                1,
                new net.minecraft.entity.ai.goal.ActiveTargetGoal<>(
                        this,
                        PlayerEntity.class,
                        true
                )
        );

        // 攻撃してきた相手に反撃
        this.targetSelector.add(
                2,
                new net.minecraft.entity.ai.goal.RevengeGoal(this)
                        .setGroupRevenge()
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

        // 最も近いプレイヤーを取得
        PlayerEntity target = this.world.getClosestPlayer(
                this,
                KNIFE_RANGE
        );

        if (target == null || !target.isAlive()) {
            knivesRemaining = 0;
            knifeCooldown = 0;
            shockwaveCooldown = 0;
            return;
        }

        // =========================
        // ナイフ攻撃
        // =========================

        if (knifeCooldown > 0) {
            knifeCooldown--;
        }

        if (knivesRemaining == 0 && knifeCooldown == 0) {
            knivesRemaining = KNIFE_COUNT;
        }

        if (knivesRemaining > 0 && knifeCooldown == 0) {

            this.throwKnife(target);

            knivesRemaining--;

            // 次のナイフまで0.75秒
            knifeCooldown = KNIFE_INTERVAL;
        }

        // =========================
        // 衝撃波
        // =========================

        shockwaveCooldown++;

        if (shockwaveCooldown >= SHOCKWAVE_INTERVAL) {

            shockwaveCooldown = 0;

            if (this.squaredDistanceTo(target) <= 20.0D * 20.0D) {
                this.performShockwave();
            }
        }
    }

    // =========================
    // ナイフ発射
    // =========================

    private void throwKnife(PlayerEntity target) {

        LongKnifeProjectile knife = new LongKnifeProjectile(
                this.world,
                this
        );

        // 発射位置
        knife.setPosition(
                this.getX(),
                this.getEyeY() - 0.1D,
                this.getZ()
        );

        // プレイヤーの上半身を狙う
        Vec3d direction = new Vec3d(
                target.getX() - this.getX(),
                target.getEyeY() - this.getEyeY(),
                target.getZ() - this.getZ()
        );

        if (direction.lengthSquared() < 0.0001D) {
            return;
        }

        direction = direction.normalize();

        // ナイフの速度
        knife.setVelocity(
                direction.multiply(KNIFE_SPEED)
        );

        // ワールドに追加
        this.world.spawnEntity(knife);

        // 発射音
        this.world.playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SakaplusSounds.BeastAmbient,
                SoundCategory.HOSTILE,
                0.8F,
                1.5F
        );
    }

    // =========================
    // 虚空衝撃波
    // =========================

    private void performShockwave() {

        // 衝撃波の音
        this.world.playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SakaplusSounds.BeastAmbient,
                SoundCategory.HOSTILE,
                3.0F,
                0.65F
        );

        Box area = this.getBoundingBox().expand(
                SHOCKWAVE_RANGE
        );

        for (LivingEntity entity : this.world.getEntitiesByClass(
                LivingEntity.class,
                area,
                entity ->
                        entity != this
                        && entity.isAlive()
                        && !this.isTeammate(entity)
        )) {

            if (this.squaredDistanceTo(entity)
                    > SHOCKWAVE_RANGE * SHOCKWAVE_RANGE) {
                continue;
            }

            // VolcanicKingによる12.5ダメージ
            entity.damage(
                    DamageSource.mob(this),
                    SHOCKWAVE_DAMAGE
            );

            // 鈍化IIを10秒付与
            entity.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.SLOWNESS,
                            200,
                            1
                    )
            );

            // 吹き飛ばし
            double dx = entity.getX() - this.getX();
            double dz = entity.getZ() - this.getZ();

            double distance = Math.sqrt(dx * dx + dz * dz);

            if (distance > 0.001D) {

                dx /= distance;
                dz /= distance;

                entity.takeKnockback(
                        1.8D,
                        -dx,
                        -dz
                );
            }

            entity.addVelocity(
                    0.0D,
                    0.35D,
                    0.0D
            );

            entity.velocityModified = true;
        }
    }

    // =========================
    // サウンド
    // =========================

    @Override
    protected SoundEvent getAmbientSound() {
        return SakaplusSounds.BeastAmbient;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SakaplusSounds.BeastHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SakaplusSounds.BeastDeath;
    }

    // =========================
    // その他
    // =========================

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
