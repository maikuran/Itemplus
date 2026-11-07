package com.sakalti.sakaplus.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;

import com.sakalti.sakaplus.sound.SakaplusSounds;

public class SavannuEntity extends HostileEntity {

    public SavannuEntity(
            EntityType<? extends HostileEntity> entityType,
            World world
    ) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.5D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 0.8D));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(9, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));

        this.targetSelector.add(2, new ActiveTargetGoal<>(
                this,
                LivingEntity.class,
                10,
                true,
                false,
                entity -> !(entity instanceof SavannuEntity)
                        && !(entity instanceof PlayerEntity)
        ));

        this.targetSelector.add(3, new ActiveTargetGoal<>(
                this, PlayerEntity.class, true
        ));
    }

    // 水中でも泳げるようにする
    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SakaplusSounds.KruziantAmbient;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SakaplusSounds.KruziantHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SakaplusSounds.KruziantDeath;
    }
}
