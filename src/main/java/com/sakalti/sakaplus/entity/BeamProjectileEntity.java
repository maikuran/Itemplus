
package com.sakalti.sakaplus.entity;

import com.sakalti.sakaplus.registry.ModBeamEntities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class BeamProjectileEntity extends ProjectileEntity {

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
    }

    public void setColor(int rgb) {
        this.dataTracker.set(BEAM_COLOR, rgb & 0xFFFFFF);
    }

    public int getColor() {
        return this.dataTracker.get(BEAM_COLOR);
    }

    public void setDamage(float damage) {
        this.dataTracker.set(BEAM_DAMAGE, Math.max(0.0F, damage));
    }

    public float getBeamDamage() {
        return this.dataTracker.get(BEAM_DAMAGE);
    }

    public void setLifeTicks(int ticks) {
        this.lifeTicks = Math.max(1, ticks);
    }

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

        if (this.getWorld().isClient) {
            int rgb = this.getColor();

            this.getWorld().addParticle(
                    net.minecraft.particle.ParticleTypes.ENTITY_EFFECT,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    ((rgb >> 16) & 255) / 255.0D,
                    ((rgb >> 8) & 255) / 255.0D,
                    (rgb & 255) / 255.0D
            );
        }

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

    @Override
    protected boolean canHit(Entity entity) {
        return entity != this
                && entity != this.getOwner()
                && entity.isAlive()
                && entity.isAttackable()
                && super.canHit(entity);
    }

    @Override
    protected void onCollision(HitResult hit) {
        if (this.getWorld().isClient) {
            return;
        }

        if (hit.getType() == HitResult.Type.ENTITY) {
            Entity target = ((EntityHitResult) hit).getEntity();
            Entity owner = this.getOwner();

            if (target instanceof LivingEntity && target.isAlive()) {
                DamageSource damageSource = new DamageSource(
                        "sakalti_beam",
                        this,
                        owner
                );

                target.damage(damageSource, this.getBeamDamage());
            }

            this.discard();
            return;
        }

        if (hit.getType() == HitResult.Type.BLOCK) {
            this.discard();
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("BeamColor")) {
            this.setColor(nbt.getInt("BeamColor"));
        }

        if (nbt.contains("BeamDamage")) {
            this.setDamage(nbt.getFloat("BeamDamage"));
        }

        if (nbt.contains("BeamLife")) {
            this.lifeTicks = Math.max(1, nbt.getInt("BeamLife"));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("BeamColor", this.getColor());
        nbt.putFloat("BeamDamage", this.getBeamDamage());
        nbt.putInt("BeamLife", this.lifeTicks);
    }
}
