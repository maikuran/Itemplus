package com.sakalti.sakaplus.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class KuloogieEntity extends HostileEntity {

    public KuloogieEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 15;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 82.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 6.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10.0D);
    }

    @Override
    protected void initGoals() {
        // 攻撃・移動 AI
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(4, new LookAroundGoal(this));

        // プレイヤーを攻撃対象にする
        this.targetSelector.add(1, new ActiveTargetGoal<>(
                this,
                PlayerEntity.class,
                true
        ));

        // 攻撃された場合、反撃する
        this.targetSelector.add(2, new RevengeGoal(this).setGroupRevenge());
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);

        if (success && target instanceof LivingEntity living) {
            // 移動速度低下
            living.addStatusEffect(
                    new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0)
            );

            // 毒
            living.addStatusEffect(
                    new StatusEffectInstance(StatusEffects.POISON, 60, 0)
            );
        }

        return success;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        Entity attacker = source.getAttacker();

        if (attacker instanceof LivingEntity living) {
            this.setTarget(living);
        }

        return super.damage(source, amount);
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        return super.canTarget(target);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return false;
    }
}
