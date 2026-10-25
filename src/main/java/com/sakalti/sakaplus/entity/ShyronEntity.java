
package com.sakalti.sakaplus.entity;

import com.sakalti.sakaplus.sound.SakaplusSounds;

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
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class ShyronEntity extends HostileEntity {

    public ShyronEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 20;
    }

    // =========================================================
    // Attributes
    // =========================================================

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 4.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.5D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0D);
    }

    // =========================================================
    // AI Goals
    // =========================================================

    @Override
    protected void initGoals() {

        // Movement and attack goals
        this.goalSelector.add(1,
                new MeleeAttackGoal(this, 1.25D, true));

        this.goalSelector.add(2,
                new WanderAroundFarGoal(this, 1.0D));

        this.goalSelector.add(3,
                new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));

        this.goalSelector.add(4,
                new LookAroundGoal(this));

        // =====================================================
        // Target Goals
        // =====================================================

        // Attack players
        this.targetSelector.add(1,
                new ActiveTargetGoal<>(
                        this,
                        PlayerEntity.class,
                        true
                ));

        // Attack all living entities except:
        // - Shyron
        // - Players (already handled above)
        this.targetSelector.add(2,
                new ActiveTargetGoal<>(
                        this,
                        LivingEntity.class,
                        10,
                        true,
                        false,
                        entity ->
                                !(entity instanceof ShyronEntity)
                                && !(entity instanceof PlayerEntity)
                ));

        // Group revenge
        this.targetSelector.add(3,
                new RevengeGoal(this).setGroupRevenge());
    }

    // =========================================================
    // Prey Kill Effect
    // =========================================================

    @Override
    public void onKilledOther(ServerWorld world, LivingEntity target) {
        super.onKilledOther(world, target);

        // Restore 15 HP after killing prey
        this.heal(15.0F);

        // Speed II for 10 seconds
        this.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.SPEED,
                        200,
                        1
                )
        );
    }

    // =========================================================
    // Sounds
    // =========================================================

    @Override
    protected SoundEvent getAmbientSound() {
        return SakaplusSounds.ShyronAmbient;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SakaplusSounds.ShyronHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SakaplusSounds.ShyronDeath;
    }
}
