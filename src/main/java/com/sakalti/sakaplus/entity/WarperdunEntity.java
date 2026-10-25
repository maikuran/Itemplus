
package com.sakalti.sakaplus.entity;

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
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class WarperdunEntity extends PathAwareEntity implements Monster {

    private final ServerBossBar bossBar;
    private int shootTick = 0;

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
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6D);
    }

    @Override
    protected void initGoals() {
        // 行動AI
        this.goalSelector.add(
                1,
                new MeleeAttackGoal(this, 1.2D, true)
        );

        this.goalSelector.add(
                2,
                new LookAtEntityGoal(this, PlayerEntity.class, 16.0F)
        );

        this.goalSelector.add(
                3,
                new LookAroundGoal(this)
        );

        // プレイヤーを自動的に攻撃対象にする
        this.targetSelector.add(
                1,
                new ActiveTargetGoal<>(
                        this,
                        PlayerEntity.class,
                        true
                )
        );

        // 攻撃してきた相手に反撃する
        this.targetSelector.add(
                2,
                new RevengeGoal(this).setGroupRevenge()
        );

        // プレイヤー以外の生物にも敵対する
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

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isClient) {
            this.shootTick++;

            // 10ティックごとに8方向へ矢を発射
            if (this.shootTick >= 25) {
                this.shootRadialArrows();
                this.shootTick = 0;
            }

            // ボスバーの体力表示を更新
            this.bossBar.setPercent(
                    this.getHealth() / this.getMaxHealth()
            );
        }
    }

    private void shootRadialArrows() {
        int count = 8;
        double arrowSpeed = 2.0D;

        for (int i = 0; i < count; i++) {
            double angle = 2.0D * Math.PI * i / count;

            double dx = Math.cos(angle);
            double dz = Math.sin(angle);
            double dy = 0.15D;

            ArrowEntity arrow = new ArrowEntity(
                    this.world,
                    this.getX(),
                    this.getY() + 1.0D,
                    this.getZ()
            ) {
                @Override
                protected void onEntityHit(
                        EntityHitResult entityHitResult
                ) {
                    super.onEntityHit(entityHitResult);

                    if (entityHitResult.getEntity() instanceof MobEntity target) {
                        target.hurtTime = 0;
                    }
                }
            };

            arrow.setVelocity(
                    dx,
                    dy,
                    dz,
                    (float) arrowSpeed,
                    0.0F
            );

            arrow.setOwner(this);

            arrow.pickupType =
                    PersistentProjectileEntity.PickupPermission.DISALLOWED;

            arrow.setInvisible(true);
            arrow.setDamage(3.0D);

            this.world.spawnEntity(arrow);
        }
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        return super.canTarget(target);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

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
