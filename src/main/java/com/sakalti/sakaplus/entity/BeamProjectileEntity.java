
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
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
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
            // 開発環境 (Yarn)
            field = LivingEntity.class.getDeclaredField("lastDamageTaken");
        } catch (NoSuchFieldException e1) {
            try {
                // 本番ビルド環境 (Fabric 1.19.2 Intermediary)
                field = LivingEntity.class.getDeclaredField("field_6253");
            } catch (NoSuchFieldException ignored) {
            }
        }
        if (field != null) {
            field.setAccessible(true);
        }
        LAST_DAMAGE_TAKEN_FIELD = field;
    }

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

    private int lifeTicks = 60;

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

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(BEAM_COLOR, 0x78DCFF);
        this.dataTracker.startTracking(BEAM_DAMAGE, 6.0F);
        this.dataTracker.startTracking(RESET_INVULNERABILITY, false);
        this.dataTracker.startTracking(ARMOR_MULTIPLIER, 0.0F);
    }

    // COLOR

    public void setColor(int rgb) {
        this.dataTracker.set(BEAM_COLOR, rgb & 0xFFFFFF);
    }

    public int getColor() {
        return this.dataTracker.get(BEAM_COLOR);
    }

    // DAMAGE

    public void setDamage(float damage) {
        this.dataTracker.set(
                BEAM_DAMAGE,
                Math.max(0.0F, damage)
        );
    }

    public float getBeamDamage() {
        return this.dataTracker.get(BEAM_DAMAGE);
    }

    // INVULNERABILITY

    public void setResetInvulnerability(boolean reset) {
        this.dataTracker.set(RESET_INVULNERABILITY, reset);
    }

    public boolean shouldResetInvulnerability() {
        return this.dataTracker.get(RESET_INVULNERABILITY);
    }

    // ARMOR DAMAGE

    public void setArmorDamageMultiplier(float multiplier) {
        this.dataTracker.set(
                ARMOR_MULTIPLIER,
                Math.max(0.0F, multiplier)
        );
    }

    public float getArmorDamageMultiplier() {
        return this.dataTracker.get(ARMOR_MULTIPLIER);
    }

    // LIFETIME

    public void setLifeTicks(int ticks) {
        this.lifeTicks = Math.max(1, ticks);
    }

    // VELOCITY

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

        if (!this.getWorld().isClient && this.age >= this.lifeTicks) {
            this.discard();
            return;
        }

        Vec3d start = this.getPos();
        Vec3d velocity = this.getVelocity();
        Vec3d end = start.add(velocity);

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

        if (hit.getType() == HitResult.Type.ENTITY) {
            Entity target = ((EntityHitResult) hit).getEntity();

            if (target instanceof LivingEntity living && target.isAlive()) {
                float armor = living.getArmor();
                float bonusDamage = armor * this.getArmorDamageMultiplier();
                float finalDamage = this.getBeamDamage() + bonusDamage;

                DamageSource damageSource = new ProjectileDamageSource(
                        "sakalti_beam",
                        this,
                        this.getOwner()
                ).setProjectile();

                if (this.shouldResetInvulnerability()) {
                    living.timeUntilRegen = 0;
                    living.hurtTime = 0;
                    resetLastDamageTaken(living);
                }

                living.damage(damageSource, finalDamage);
            }

            this.discard();
            return;
        }

        if (hit.getType() == HitResult.Type.BLOCK) {
            this.discard();
        }
    }

    private static void resetLastDamageTaken(LivingEntity living) {
        if (LAST_DAMAGE_TAKEN_FIELD != null) {
            try {
                LAST_DAMAGE_TAKEN_FIELD.setFloat(living, 0.0F);
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    // =========================================================
    // NBT READ & WRITE
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
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putInt("BeamColor", this.getColor());
        nbt.putFloat("BeamDamage", this.getBeamDamage());
        nbt.putInt("BeamLife", this.lifeTicks);
        nbt.putBoolean("ResetInvulnerability", this.shouldResetInvulnerability());
        nbt.putFloat("ArmorMultiplier", this.getArmorDamageMultiplier());
    }
// =========================================================
@Override
public Packet<?> createSpawnPacket() {
return new EntitySpawnS2CPacket(this);
}
}
