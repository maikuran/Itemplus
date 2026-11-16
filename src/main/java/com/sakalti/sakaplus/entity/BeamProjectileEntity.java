
package com.sakalti.sakaplus.entity;

import java.lang.reflect.Field;

import com.sakalti.sakaplus.registry.ModBeamEntities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class BeamProjectileEntity extends ProjectileEntity {

    private static final Field LAST_DAMAGE_TAKEN_FIELD;

    static {
        Field field = null;

        try {
            field = LivingEntity.class.getDeclaredField("lastDamageTaken");
        } catch (NoSuchFieldException e1) {
            try {
                field = LivingEntity.class.getDeclaredField("field_6253");
            } catch (NoSuchFieldException ignored) {
            }
        }

        if (field != null) {
            field.setAccessible(true);
        }

        LAST_DAMAGE_TAKEN_FIELD = field;
    }

    // =========================================================
    // DATA TRACKER
    // =========================================================

    private static final TrackedData<Integer> BEAM_COLOR =
            DataTracker.registerData(
                    BeamProjectileEntity.class,
                    TrackedDataHandlerRegistry.INTEGER
            );

    private static final TrackedData<Float> BEAM_DAMAGE =
            DataTracker.registerData(
                    BeamProjectileEntity.class,
                    TrackedDataHandlerRegistry.FLOAT
            );

    private static final TrackedData<Boolean> RESET_INVULNERABILITY =
            DataTracker.registerData(
                    BeamProjectileEntity.class,
                    TrackedDataHandlerRegistry.BOOLEAN
            );

    private static final TrackedData<Float> ARMOR_MULTIPLIER =
            DataTracker.registerData(
                    BeamProjectileEntity.class,
                    TrackedDataHandlerRegistry.FLOAT
            );

    // =========================================================
    // VARIABLES
    // =========================================================

    private int lifeTicks = 60;

    // HIT EFFECT
    private StatusEffect hitEffect = null;

    private int hitEffectDuration = 0;

    // Amplifier: 0 = Level I, 1 = Level II
    private int hitEffectAmplifier = 0;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public BeamProjectileEntity(
            EntityType<? extends BeamProjectileEntity> type,
            World world
    ) {
        super(type, world);
        this.setNoGravity(true);
    }

    public BeamProjectileEntity(World world, LivingEntity owner) {
        this(ModBeamEntities.BEAM_PROJECTILE, world);

        this.setOwner(owner);

        this.setPosition(
                owner.getX(),
                owner.getEyeY() - 0.1D,
                owner.getZ()
        );
    }

    // =========================================================
    // INIT DATA TRACKER
    // =========================================================

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(BEAM_COLOR, 0x78DCFF);
        this.dataTracker.startTracking(BEAM_DAMAGE, 6.0F);
        this.dataTracker.startTracking(RESET_INVULNERABILITY, false);
        this.dataTracker.startTracking(ARMOR_MULTIPLIER, 0.0F);
    }

    // =========================================================
    // COLOR
    // =========================================================

    public void setColor(int rgb) {
        this.dataTracker.set(BEAM_COLOR, rgb & 0xFFFFFF);
    }

    public int getColor() {
        return this.dataTracker.get(BEAM_COLOR);
    }

    // =========================================================
    // DAMAGE
    // =========================================================

    public void setDamage(float damage) {
        this.dataTracker.set(
                BEAM_DAMAGE,
                Math.max(0.0F, damage)
        );
    }

    public float getBeamDamage() {
        return this.dataTracker.get(BEAM_DAMAGE);
    }

    // =========================================================
    // INVULNERABILITY
    // =========================================================

    public void setResetInvulnerability(boolean reset) {
        this.dataTracker.set(RESET_INVULNERABILITY, reset);
    }

    public boolean shouldResetInvulnerability() {
        return this.dataTracker.get(RESET_INVULNERABILITY);
    }

    // =========================================================
    // ARMOR DAMAGE
    // =========================================================

    public void setArmorDamageMultiplier(float multiplier) {
        this.dataTracker.set(
                ARMOR_MULTIPLIER,
                Math.max(0.0F, multiplier)
        );
    }

    public float getArmorDamageMultiplier() {
        return this.dataTracker.get(ARMOR_MULTIPLIER);
    }

    // =========================================================
    // LIFETIME
    // =========================================================

    public void setLifeTicks(int ticks) {
        this.lifeTicks = Math.max(1, ticks);
    }

    // =========================================================
    // HIT EFFECT CONFIGURATION
    // =========================================================

    /**
     * Sets an effect to apply when this beam successfully damages
     * a LivingEntity.
     *
     * @param effect Effect to apply
     * @param duration Duration in ticks
     * @param amplifier Effect level minus one
     */
    public void setHitEffect(
            StatusEffect effect,
            int duration,
            int amplifier
    ) {
        this.hitEffect = effect;

        this.hitEffectDuration = Math.max(0, duration);

        this.hitEffectAmplifier = Math.max(0, amplifier);
    }

    /**
     * Disables the additional hit effect.
     */
    public void clearHitEffect() {
        this.hitEffect = null;
        this.hitEffectDuration = 0;
        this.hitEffectAmplifier = 0;
    }

    public StatusEffect getHitEffect() {
        return this.hitEffect;
    }

    public int getHitEffectDuration() {
        return this.hitEffectDuration;
    }

    public int getHitEffectAmplifier() {
        return this.hitEffectAmplifier;
    }

    /**
     * Applies the configured effect to the target.
     */
    private void applyHitEffect(LivingEntity target) {
        if (this.hitEffect == null) {
            return;
        }

        if (this.hitEffectDuration <= 0) {
            return;
        }

        target.addStatusEffect(
                new StatusEffectInstance(
                        this.hitEffect,
                        this.hitEffectDuration,
                        this.hitEffectAmplifier
                )
        );
    }

    // =========================================================
    // VELOCITY
    // =========================================================

    public void setBeamVelocity(LivingEntity shooter, float speed) {
        this.setVelocity(
                shooter,
                shooter.getPitch(),
                shooter.getYaw(),
                0.0F,
                speed,
                0.0F
        );
    }

    // =========================================================
    // TICK
    // =========================================================

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient
                && this.age >= this.lifeTicks) {
            this.discard();
            return;
        }

        Vec3d start = this.getPos();
        Vec3d velocity = this.getVelocity();
        Vec3d end = start.add(velocity);

        // BLOCK RAYCAST

        HitResult blockHit = this.getWorld().raycast(
                new RaycastContext(
                        start,
                        end,
                        RaycastContext.ShapeType.COLLIDER,
                        RaycastContext.FluidHandling.NONE,
                        this
                )
        );

        Vec3d entityEnd = end;

        if (blockHit.getType() != HitResult.Type.MISS) {
            entityEnd = blockHit.getPos();
        }

        // ENTITY RAYCAST

        Box searchBox = this.getBoundingBox()
                .stretch(velocity)
                .expand(0.3D);

        EntityHitResult entityHit = ProjectileUtil.raycast(
                this,
                start,
                entityEnd,
                searchBox,
                this::canHit,
                start.squaredDistanceTo(entityEnd)
        );

        if (entityHit != null) {
            this.onCollision(entityHit);
            return;
        }

        // BLOCK COLLISION

        if (blockHit.getType() != HitResult.Type.MISS) {
            this.onCollision(blockHit);
            return;
        }

        this.setPosition(end);
    }

    // =========================================================
    // HIT FILTER
    // =========================================================

    @Override
    protected boolean canHit(Entity entity) {
        return entity != this
                && entity != this.getOwner()
                && entity.isAlive()
                && entity.isAttackable()
                && super.canHit(entity);
    }

    // =========================================================
    // COLLISION
    // =========================================================

    @Override
    protected void onCollision(HitResult hit) {
        if (this.getWorld().isClient) {
            return;
        }

        // ENTITY HIT

        if (hit.getType() == HitResult.Type.ENTITY) {

            Entity target = ((EntityHitResult) hit).getEntity();

            if (target instanceof LivingEntity living
                    && target.isAlive()) {

                float armor = living.getArmor();

                float bonusDamage =
                        armor * this.getArmorDamageMultiplier();

                float finalDamage =
                        this.getBeamDamage() + bonusDamage;

                DamageSource damageSource =
                        new ProjectileDamageSource(
                                "sakalti_beam",
                                this,
                                this.getOwner()
                        ).setProjectile();

                // RESET INVULNERABILITY

                if (this.shouldResetInvulnerability()) {
                    living.timeUntilRegen = 0;
                    living.hurtTime = 0;

                    resetLastDamageTaken(living);
                }

                // DAMAGE

                boolean damaged = living.damage(
                        damageSource,
                        finalDamage
                );

                // APPLY EFFECT ONLY IF DAMAGE SUCCEEDS

                if (damaged && living.isAlive()) {
                    this.applyHitEffect(living);
                }
            }

            this.discard();
            return;
        }

        // BLOCK HIT

        if (hit.getType() == HitResult.Type.BLOCK) {
            this.discard();
        }
    }

    // =========================================================
    // RESET LAST DAMAGE
    // =========================================================

    private static void resetLastDamageTaken(LivingEntity living) {
        if (LAST_DAMAGE_TAKEN_FIELD != null) {
            try {
                LAST_DAMAGE_TAKEN_FIELD.setFloat(
                        living,
                        0.0F
                );
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    // =========================================================
    // NBT READ
    // =========================================================

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("BeamColor")) {
            this.setColor(nbt.getInt("BeamColor"));
        }

        if (nbt.contains("BeamDamage")) {
            this.setDamage(nbt.getFloat("BeamDamage"));
        }

        if (nbt.contains("BeamLife")) {
            this.lifeTicks = Math.max(
                    1,
                    nbt.getInt("BeamLife")
            );
        }

        if (nbt.contains("ResetInvulnerability")) {
            this.setResetInvulnerability(
                    nbt.getBoolean("ResetInvulnerability")
            );
        }

        if (nbt.contains("ArmorMultiplier")) {
            this.setArmorDamageMultiplier(
                    nbt.getFloat("ArmorMultiplier")
            );
        }

        // HIT EFFECT READ

        if (nbt.contains("HitEffect")) {
            Identifier effectId = Identifier.tryParse(
                    nbt.getString("HitEffect")
            );

            if (effectId != null) {
                StatusEffect effect =
                        Registry.STATUS_EFFECT
                                .getOrEmpty(effectId)
                                .orElse(null);

                if (effect != null) {
                    this.setHitEffect(
                            effect,
                            nbt.getInt("HitEffectDuration"),
                            nbt.getInt("HitEffectAmplifier")
                    );
                }
            }
        }
    }

    // =========================================================
    // NBT WRITE
    // =========================================================

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putInt("BeamColor", this.getColor());

        nbt.putFloat(
                "BeamDamage",
                this.getBeamDamage()
        );

        nbt.putInt("BeamLife", this.lifeTicks);

        nbt.putBoolean(
                "ResetInvulnerability",
                this.shouldResetInvulnerability()
        );

        nbt.putFloat(
                "ArmorMultiplier",
                this.getArmorDamageMultiplier()
        );

        // HIT EFFECT WRITE

        if (this.hitEffect != null) {
            Identifier effectId =
                    Registry.STATUS_EFFECT.getId(this.hitEffect);

            if (effectId != null) {
                nbt.putString(
                        "HitEffect",
                        effectId.toString()
                );

                nbt.putInt(
                        "HitEffectDuration",
                        this.hitEffectDuration
                );

                nbt.putInt(
                        "HitEffectAmplifier",
                        this.hitEffectAmplifier
                );
            }
        }
    }

    // =========================================================
    // SPAWN PACKET
    // =========================================================

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}
