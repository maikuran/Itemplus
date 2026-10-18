
package com.sakalti.sakaplus.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class NyankoEntity extends HostileEntity {

    public NyankoEntity(
            EntityType<? extends HostileEntity> entityType,
            World world
    ) {
        super(entityType, world);
        this.experiencePoints = 6;
    }

    @Override
    protected void initGoals() {
        // 行動AI
        this.goalSelector.add(
                1,
                new MeleeAttackGoal(this, 0.8D, false)
        );

        this.goalSelector.add(
                2,
                new WanderAroundFarGoal(this, 0.8D)
        );

        this.goalSelector.add(
                3,
                new LookAtEntityGoal(this, LivingEntity.class, 8.0F)
        );

        this.goalSelector.add(
                4,
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

        // 攻撃されたら反撃する
        this.targetSelector.add(
                2,
                new RevengeGoal(this)
        );

        // プレイヤー以外の非敵対モブも攻撃対象にする
        this.targetSelector.add(
                3,
                new ActiveTargetGoal<>(
                        this,
                        MobEntity.class,
                        10,
                        true,
                        false,
                        target ->
                                !(target instanceof HostileEntity)
                                && !(target instanceof NyankoEntity)
                )
        );
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 17.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_ARMOR, 1.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 25.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.8D);
    }
}
