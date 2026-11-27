package com.sakalti.thesolar.entity;

import com.sakalti.thesolar.DimensionMod;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SolarHostileEntity extends PathfinderMob {
    private static final EntityDataAccessor<Integer> ATTACK_TYPE = SynchedEntityData.defineId(
            SolarHostileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACK_TICKS = SynchedEntityData.defineId(
            SolarHostileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COMBAT_LEVEL = SynchedEntityData.defineId(
            SolarHostileEntity.class, EntityDataSerializers.INT);

    private final double phaseTwoHealthRatio;
    private boolean phaseTwo;

    public SolarHostileEntity(EntityType<? extends SolarHostileEntity> type, Level level) {
        this(type, level, 0.0);
    }

    public SolarHostileEntity(EntityType<? extends SolarHostileEntity> type, Level level, double phaseTwoHealthRatio) {
        super(type, level);
        this.phaseTwoHealthRatio = phaseTwoHealthRatio;
        this.xpReward = phaseTwoHealthRatio > 0 ? 35 : 8;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACK_TYPE, 0);
        builder.define(ATTACK_TICKS, 0);
        builder.define(COMBAT_LEVEL, 1);
    }

    public static AttributeSupplier.Builder createAttributes(double health, double speed, double damage, double range) {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, health)
                .add(Attributes.MOVEMENT_SPEED, speed)
                .add(Attributes.ATTACK_DAMAGE, damage)
                .add(Attributes.FOLLOW_RANGE, range)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.15);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.15, false));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;
        int telegraphTicks = entityData.get(ATTACK_TICKS);
        if (telegraphTicks > 0) {
            entityData.set(ATTACK_TICKS, telegraphTicks - 1);
            if (telegraphTicks <= 1) entityData.set(ATTACK_TYPE, 0);
            else if (tickCount % 4 == 0 && level() instanceof ServerLevel server) emitTelegraphRing(server, telegraphTicks);
        }
        if (phaseTwoHealthRatio > 0 && !phaseTwo
                && getHealth() <= getMaxHealth() * phaseTwoHealthRatio) {
            phaseTwo = true;
            if (getAttribute(Attributes.MOVEMENT_SPEED) != null) {
                getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(
                        getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() * 1.32);
            }
            if (getAttribute(Attributes.ATTACK_DAMAGE) != null) {
                getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(
                        getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() * 1.60);
            }
            heal(getMaxHealth() * 0.18f);
        }
        tickSpecialAbility();
    }

    private void emitTelegraphRing(ServerLevel server, int ticksLeft) {
        ParticleOptions particle = this instanceof SeleneEntity ? ParticleTypes.END_ROD : ParticleTypes.FLAME;
        double progress = 1.0 - (ticksLeft / 26.0);
        double radius = 2.2 + progress * 2.8;
        int points = 18;
        for (int i = 0; i < points; i++) {
            double angle = (Math.PI * 2.0 * i) / points;
            double x = getX() + Math.cos(angle) * radius;
            double z = getZ() + Math.sin(angle) * radius;
            server.sendParticles(particle, x, getY() + 0.12, z, 1, 0.015, 0.025, 0.015, 0.0);
        }
    }

    protected void beginAttackTelegraph(int type, int ticks) {
        if (level().isClientSide() || ticks <= 0) return;
        entityData.set(ATTACK_TYPE, type);
        entityData.set(ATTACK_TICKS, ticks);
        if (this instanceof SeleneEntity) {
            level().playSound(null, blockPosition(), DimensionMod.SELENE_TELEGRAPH_SOUND, SoundSource.HOSTILE, 0.9f, 0.82f);
        } else if (this instanceof AresEntity) {
            level().playSound(null, blockPosition(), DimensionMod.ARES_TELEGRAPH_SOUND, SoundSource.HOSTILE, 1.0f, 0.72f);
        }
    }

    public void setCombatLevel(int level) {
        entityData.set(COMBAT_LEVEL, Math.max(1, level));
    }

    public int getCombatLevel() {
        return entityData.get(COMBAT_LEVEL);
    }

    public int getAttackType() {
        return entityData.get(ATTACK_TYPE);
    }

    public int getAttackTicks() {
        return entityData.get(ATTACK_TICKS);
    }

    /** Server-side hook for species and boss-specific combat behaviour. */
    protected void tickSpecialAbility() {}

    protected LivingEntity combatTarget() {
        return getTarget();
    }

    protected boolean hurtTarget(LivingEntity target, float damage, double knockback, double lift) {
        if (target == null || !target.isAlive()) return false;
        boolean hurt = level() instanceof ServerLevel server
                && target.hurtServer(server, damageSources().mobAttack(this), damage);
        if (hurt && knockback > 0.0) {
            double dx = target.getX() - getX();
            double dz = target.getZ() - getZ();
            double length = Math.sqrt(dx * dx + dz * dz);
            if (length > 0.001) target.push(dx / length * knockback, lift, dz / length * knockback);
        }
        return hurt;
    }

    protected void pulseNearbyPlayers(double radius, float damage, double knockback, double lift) {
        for (Player player : level().getEntitiesOfClass(Player.class,
                getBoundingBox().inflate(radius), Player::isAlive)) {
            hurtTarget(player, damage, knockback, lift);
        }
    }

    protected void leapToward(LivingEntity target, double horizontalPower, double verticalPower) {
        if (target == null) return;
        double dx = target.getX() - getX();
        double dz = target.getZ() - getZ();
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length > 0.001) {
            setDeltaMovement(getDeltaMovement().add(dx / length * horizontalPower,
                    verticalPower, dz / length * horizontalPower));
        }
    }

    public boolean isInSecondPhase() {
        return phaseTwo;
    }
}
