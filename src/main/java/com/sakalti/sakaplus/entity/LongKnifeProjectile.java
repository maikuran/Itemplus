
package com.sakalti.sakaplus.entity;

import com.sakalti.sakaplus.registry.ModBeamEntities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class LongKnifeProjectile extends ProjectileEntity {

    private static final float DAMAGE = 12.5F;
    private static final int LIFE_TICKS = 60;

    public LongKnifeProjectile(
            EntityType<? extends LongKnifeProjectile> type,
            World world
    ) {
        super(type, world);
        this.setNoGravity(true);
    }

    public LongKnifeProjectile(World world, LivingEntity owner) {
        this(ModBeamEntities.LONG_KNIFE_PROJECTILE, world);

        this.setOwner(owner);

        this.setPosition(
                owner.getX(),
                owner.getEyeY() - 0.1D,
                owner.getZ()
        );
    }

    public void setKnifeVelocity(LivingEntity shooter, float speed) {
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

        if (!this.getWorld().isClient && this.age >= LIFE_TICKS) {
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

            if (target instanceof LivingEntity && target.isAlive()) {

                LivingEntity living = (LivingEntity) target;

                DamageSource damageSource =
                        new DamageSource("sakaplus_long_knife");

                living.damage(damageSource, DAMAGE);
            }

            this.discard();
            return;
        }

        if (hit.getType() == HitResult.Type.BLOCK) {
            this.discard();
        }
    }
}
