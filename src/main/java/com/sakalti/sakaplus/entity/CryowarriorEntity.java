
package com.sakalti.sakaplus.entity;

import com.sakalti.sakaplus.effect.ModEffects;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class CryowarriorEntity extends HostileEntity {

    private static final int ADAPT_UPDATE_INTERVAL = 10;
    private static final int ADAPT_DURATION = 30;

    private int adaptTick = 0;

    public CryowarriorEntity(
            EntityType<? extends CryowarriorEntity> type,
            World world
    ) {
        super(type, world);
        this.experiencePoints = 12;
    }

    // Attributes

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 67.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.27D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 9.5D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.25D);
    }

    // Goals

    @Override
    protected void initGoals() {

        // 攻撃対象を探す
        this.targetSelector.add(
                1,
                new ActiveTargetGoal<>(
                        this,
                        PlayerEntity.class,
                        10,
                        true,
                        false,
                        null
                )
        );

        // 近接攻撃・追跡
        this.goalSelector.add(
                2,
                new MeleeAttackGoal(
                        this,
                        1.15D,
                        true
                )
        );

        // プレイヤーを見る
        this.goalSelector.add(
                3,
                new LookAtEntityGoal(
                        this,
                        PlayerEntity.class,
                        12.0F
                )
        );

        // 周囲を歩く
        this.goalSelector.add(
                5,
                new WanderAroundFarGoal(
                        this,
                        0.8D
                )
        );

        // 周囲を見回す
        this.goalSelector.add(
                6,
                new LookAroundGoal(this)
        );
    }

    // Self Adaptation

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            return;
        }

        adaptTick++;

        if (adaptTick >= ADAPT_UPDATE_INTERVAL) {
            adaptTick = 0;
            updateAdapt();
        }
    }

    private void updateAdapt() {

        if (!this.isAlive()) {
            return;
        }

        double maxHealth = this.getMaxHealth();

        if (maxHealth <= 0.0D) {
            return;
        }

        double healthRatio = this.getHealth() / maxHealth;

        int adaptLevel;

        // HP 75%以上 : Lv.5
        if (healthRatio >= 0.75D) {
            adaptLevel = 4;
        }
        // HP 45%超～75%未満 : Lv.4
        else if (healthRatio > 0.45D) {
            adaptLevel = 3;
        }
        // HP 45%以下 : Lv.3
        else {
            adaptLevel = 2;
        }

        StatusEffectInstance current =
                this.getStatusEffect(ModEffects.ADAPT);

        // 同じレベルの効果が十分残っているなら更新しない
        if (current != null
                && current.getAmplifier() == adaptLevel
                && current.getDuration() > ADAPT_UPDATE_INTERVAL + 2) {
            return;
        }

        this.removeStatusEffect(ModEffects.ADAPT);

        this.addStatusEffect(
                new StatusEffectInstance(
                        ModEffects.ADAPT,
                        ADAPT_DURATION,
                        adaptLevel,
                        false,
                        true,
                        true
                )
        );
    }
}
