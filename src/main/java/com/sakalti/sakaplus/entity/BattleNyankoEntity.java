package com.sakalti.sakaplus.entity; 

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World; 

public class BattleNyankoEntity extends HostileEntity {
    
    public BattleNyankoEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 8;
    } 

    @Override
    protected void initGoals() {
        // 第2引数は移動速度倍率(double)、第3引数は「ターゲットが視界から外れても追い続けるか」(boolean)
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, false)); 
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(3, new LookAtEntityGoal(this, LivingEntity.class, 8.0F));
        this.goalSelector.add(4, new LookAroundGoal(this));
        
        // KurBruteEntityだけを狙う設定
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, KurBruteEntity.class, true));
        // 反撃ゴール
        this.targetSelector.add(2, new RevengeGoal(this).setGroupRevenge());
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 55.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0);
                // GENERIC_ATTACK_SPEED はモブに存在しないため削除しました
    }
}
