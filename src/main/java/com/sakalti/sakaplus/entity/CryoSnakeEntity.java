
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
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.sound.SoundEvent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;

import com.sakalti.sakaplus.sound.SakaplusSounds;

public class CryoSnakeEntity extends HostileEntity {

    public CryoSnakeEntity(
            EntityType<? extends HostileEntity> entityType,
            World world
    ) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 88.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 7.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.5D)
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
                entity -> !(entity instanceof CryoSnakeEntity)
                        && !(entity instanceof PlayerEntity)
        ));

        this.targetSelector.add(3, new ActiveTargetGoal<>(
                this, PlayerEntity.class, true
        ));
    }

    // 攻撃を受けたとき、攻撃者に毒IVを10秒間付与
    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean successful = super.damage(source, amount);

        if (successful && !this.getWorld().isClient) {
            Entity attacker = source.getAttacker();

            if (attacker instanceof LivingEntity
                    && attacker != this) {

                LivingEntity livingAttacker = (LivingEntity) attacker;

                livingAttacker.addStatusEffect(
                        new StatusEffectInstance(
                                StatusEffects.POISON,
                                200,
                                3,
                                false,
                                true
                        ),
                        this
                );
            }
        }

        return successful;
    }

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
}
