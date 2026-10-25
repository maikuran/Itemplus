
package com.sakalti.sakaplus.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class ShyronEntity extends HostileEntity {

    public ShyronEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 4.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.5D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        this.goalSelector.add(2, new net.minecraft.entity.ai.goal.MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(7, new net.minecraft.entity.ai.goal.WanderAroundFarGoal(this, 0.8D));
        this.goalSelector.add(8, new net.minecraft.entity.ai.goal.LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(9, new net.minecraft.entity.ai.goal.LookAroundGoal(this));

        this.targetSelector.add(1, new net.minecraft.entity.ai.goal.RevengeGoal(this));
        this.targetSelector.add(2, new net.minecraft.entity.ai.goal.ActiveTargetGoal<>(
                this,
                LivingEntity.class,
                10,
                true,
                false,
                entity -> !(entity instanceof ShyronEntity)
                        && !(entity instanceof PlayerEntity)
        ));
        this.targetSelector.add(3, new net.minecraft.entity.ai.goal.ActiveTargetGoal<>(
                this, PlayerEntity.class, true
        ));
    }

    // 攻撃によって敵を倒した場合の処理
    @Override
    public boolean tryAttack(Entity target) {
        boolean successful = super.tryAttack(target);

        if (successful
                && !this.getWorld().isClient
                && target instanceof LivingEntity
                && !target.isAlive()) {

            this.heal(15.0F);
            this.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED,
                    200,
                    1
            ));
        }

        return successful;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SakaplusSounds.ShyronAmbient;
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.entity.damage.DamageSource source) {
        return SakaplusSounds.ShyronHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SakaplusSounds.ShyronDeath;
    }
}
