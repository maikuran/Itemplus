package com.sakalti.sakaplus.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import java.util.List;

public class OganesonEntity extends BlazeEntity {
    private int shootTick = 0;

    public OganesonEntity(EntityType<? extends BlazeEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 5;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return BlazeEntity.createBlazeAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 18.0D)
            .add(EntityAttributes.GENERIC_ARMOR, 2.0D)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.36D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new FlyGoal(this, 1.0));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(3, new LookAtEntityGoal(this, LivingEntity.class, 8.0F));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PiglinEntity.class, true));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.0, false));
    }

    @Override
    public void tick() {
        super.tick();
        if ((this.getTarget() == null || !this.getTarget().isAlive()) && !this.getWorld().isClient()) {
            this.getWorld().getEntitiesByClass(PiglinEntity.class, this.getBoundingBox().expand(16), piglin -> true)
                .stream().findFirst().ifPresent(this::setTarget);
        }
        if (!this.getWorld().isClient()) {
            shootTick++;
            if (shootTick >= 10) {
                shootRadialArrows(); // メソッド名はそのままに中身を軽量化
                shootTick = 0;
            }
        }
    }

    private void shootRadialArrows() {
        // 【軽量化の肝】矢をスポーンさせず、半径16ブロック以内のピグリンを直接取得
        double radius = 16.0D;
        List<PiglinEntity> targets = this.getWorld().getEntitiesByClass(
            PiglinEntity.class, 
            this.getBoundingBox().expand(radius), 
            piglin -> piglin.isAlive()
        );

        for (PiglinEntity target : targets) {
            // 直接「このMobが攻撃した」判定のMobダメージを与える
            target.damage(this.getDamageSources().mobAttack(this), 1.0F);
            
            // 無敵時間を即座にリセットして連続ヒットを可能にする
            target.hurtTime = 0;
            target.timeUntilRegen = 0;
        }

        // 全方位へ円状に広がる「火の粉（FLAME）」の演出（エンティティではないため極めて軽量）
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            int particleCount = 16;
            for (int i = 0; i < particleCount; i++) {
                double angle = 2 * Math.PI * i / particleCount;
                double dx = Math.cos(angle) * 0.5; // 広がる速度X
                double dz = Math.sin(angle) * 0.5; // 広がる速度Z
                
                // 火の粉パーティクルを円状に飛ばす
                serverWorld.spawnParticles(
                    ParticleTypes.FLAME, 
                    this.getX(), this.getY() + 1.0D, this.getZ(), 
                    1, dx, 0.1, dz, 0.2
                );
            }
        }
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }
}
