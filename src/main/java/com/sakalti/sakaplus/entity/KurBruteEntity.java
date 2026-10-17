package com.sakalti.sakaplus.entity;

import net.minecraft.entity.*;
import com.sakalti.sakaplus.effect.ModEffects;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class KurBruteEntity extends PathAwareEntity {

    public KurBruteEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 6;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 45.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2, true));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(4, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge());
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);
        if (success && target instanceof LivingEntity living) {
            // 毒I（3秒）
            living.addStatusEffect(new StatusEffectInstance(ModEffects.PARALYZE, 100, 0));
        }
        return success;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        Entity attacker = source.getAttacker();
        if (attacker != null && !(attacker instanceof PlayerEntity)) {
            this.setTarget(attacker instanceof LivingEntity ? (LivingEntity) attacker : null);
        }
        return super.damage(source, amount);
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        return !(target instanceof PlayerEntity);
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
