
package com.sakalti.sakaplus.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class BeamAttack {

    private BeamAttack() {
    }

    public static BeamProjectileEntity shoot(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb
    ) {
        World world = shooter.getWorld();

        if (world.isClient) {
            return null;
        }

        BeamProjectileEntity beam =
                new BeamProjectileEntity(world, shooter);

        beam.setColor(rgb);
        beam.setDamage(damage);
        beam.setBeamVelocity(shooter, speed);

        world.spawnEntity(beam);
        return beam;
    }

    public static BeamProjectileEntity shoot(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb,
            int lifeTicks
    ) {
        BeamProjectileEntity beam =
                shoot(shooter, speed, damage, rgb);

        if (beam != null) {
            beam.setLifeTicks(lifeTicks);
        }

        return beam;
    }

    // OPTIONAL RESET VERSION

    public static BeamProjectileEntity shoot(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb,
            boolean resetInvulnerability
    ) {
        BeamProjectileEntity beam =
                shoot(shooter, speed, damage, rgb);

        if (beam != null) {
            beam.setResetInvulnerability(resetInvulnerability);
        }

        return beam;
    }

    public static BeamProjectileEntity shootAt(
            LivingEntity shooter,
            LivingEntity target,
            float speed,
            float damage,
            int rgb
    ) {
        World world = shooter.getWorld();

        if (world.isClient || target == null || !target.isAlive()) {
            return null;
        }

        Vec3d start = new Vec3d(
                shooter.getX(),
                shooter.getEyeY() - 0.1D,
                shooter.getZ()
        );

        Vec3d targetPos = new Vec3d(
                target.getX(),
                target.getBodyY(0.5D),
                target.getZ()
        );

        Vec3d direction = targetPos.subtract(start);

        if (direction.lengthSquared() < 1.0E-8D) {
            return null;
        }

        direction = direction.normalize();

        BeamProjectileEntity beam =
                new BeamProjectileEntity(world, shooter);

        beam.setPosition(start.x, start.y, start.z);
        beam.setColor(rgb);
        beam.setDamage(damage);

        beam.setVelocity(
                direction.x * speed,
                direction.y * speed,
                direction.z * speed
        );

        world.spawnEntity(beam);
        return beam;
    }

    // OPTIONAL RESET VERSION FOR TARGETED SHOT

    public static BeamProjectileEntity shootAt(
            LivingEntity shooter,
            LivingEntity target,
            float speed,
            float damage,
            int rgb,
            boolean resetInvulnerability
    ) {
        BeamProjectileEntity beam = shootAt(
                shooter,
                target,
                speed,
                damage,
                rgb
        );

        if (beam != null) {
            beam.setResetInvulnerability(resetInvulnerability);
        }

        return beam;
    }
}
