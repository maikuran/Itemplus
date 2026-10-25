package com.sakalti.sakaplus.entity;

import com.sakalti.sakaplus.sound.SakaplusSounds;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ShyronEntity extends HostileEntity {

    public ShyronEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 20;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0D)       // HP: 60
                .add(EntityAttributes.GENERIC_ARMOR, 4.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.5D)     // ダメージ: 6.5
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0D);
    }

    @Override
    protected void initGoals() {
        // 優先度1: ターゲットへの近接攻撃
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.25D, true));
        // 優先度2: 周囲を徘徊
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D));
        // 優先度3: プレイヤーを凝視
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(4, new LookAroundGoal(this));

        // ターゲット選定1: プレイヤーに敵対
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        
        // 💡 ユニークな生態: 自分以外のすべての生物（LivingEntity）を捕食対象として襲う
        // 同族（ShyronEntity）は襲わないように条件から除外
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, LivingEntity.class, 10, true, false, 
                (entity) -> entity instanceof LivingEntity && !(entity instanceof ShyronEntity) && !(entity instanceof PlayerEntity)));

        // 攻撃されたら反撃（周囲の仲間もリンクして一斉に襲いかかる）
        this.targetSelector.add(3, new RevengeGoal(this).setGroupRevenge());
    }

    // 💡 ユニークな生態: 獲物を倒した（捕食した）時の処理
    @Override
    public void onTargetKilled(ServerWorld world, LivingEntity target) {
        super.onTargetKilled(world, target);
        
        // 捕食成功の報酬: HPを15回復
        this.heal(15.0F);
        
        // 興奮状態: 10秒間（200個のゲームチック）移動速度上昇IIを自身に付与
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 1));
    }

    // 🔊 登録した独自の環境音（Ambient）を設定
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SakaplusSounds.ShyronAmbient;
    }

    // 🔊 登録した独自の被ダメージ音（Hurt）を設定
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SakaplusSounds.ShyronHurt;
    }

    // 🔊 登録した独自の死亡音（Death）を設定
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SakaplusSounds.ShyronDeath;
    }
}
