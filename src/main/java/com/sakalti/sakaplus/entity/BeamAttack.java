
package com.sakalti.sakaplus.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class BeamAttack {

    private BeamAttack() {
    }

    // 通常射撃
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

    // 寿命指定射撃
    public static BeamProjectileEntity shoot(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb,
            int lifeTicks
    ) {
        BeamProjectileEntity beam = shoot(
                shooter,
                speed,
                damage,
                rgb
        );

        if (beam != null) {
            beam.setLifeTicks(lifeTicks);
        }

        return beam;
    }

    // 無敵時間リセット指定射撃
    public static BeamProjectileEntity shoot(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb,
            boolean resetInvulnerability
    ) {
        BeamProjectileEntity beam = shoot(
                shooter,
                speed,
                damage,
                rgb
        );

        if (beam != null) {
            beam.setResetInvulnerability(resetInvulnerability);
        }

        return beam;
    }

    // 方向指定射撃（Yaw・Pitch）
    public static BeamProjectileEntity shoot(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb,
            float yawOffset,
            float pitchOffset
    ) {
        return shoot(
                shooter,
                speed,
                damage,
                rgb,
                false,
                yawOffset,
                pitchOffset
        );
    }

    // 方向指定 + 無敵時間リセット
    public static BeamProjectileEntity shoot(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb,
            boolean resetInvulnerability,
            float yawOffset,
            float pitchOffset
    ) {
        World world = shooter.getWorld();

        if (world.isClient) {
            return null;
        }

        BeamProjectileEntity beam =
                new BeamProjectileEntity(world, shooter);

        beam.setColor(rgb);
        beam.setDamage(damage);

        // プレイヤーの視線方向を取得
        float yaw = shooter.getYaw() + yawOffset;
        float pitch = shooter.getPitch() + pitchOffset;

        // Yaw・Pitchから射撃方向を計算
        float yawRad = yaw * ((float) Math.PI / 180.0F);
        float pitchRad = pitch * ((float) Math.PI / 180.0F);

        float x = -((float) Math.sin(yawRad))
                * ((float) Math.cos(pitchRad));

        float y = -((float) Math.sin(pitchRad));

        float z = ((float) Math.cos(yawRad))
                * ((float) Math.cos(pitchRad));

        Vec3d direction = new Vec3d(x, y, z).normalize();

        // 発射位置
        Vec3d start = new Vec3d(
                shooter.getX(),
                shooter.getEyeY() - 0.1D,
                shooter.getZ()
        );

        beam.setPosition(start.x, start.y, start.z);

        // 速度設定
        beam.setVelocity(
                direction.x * speed,
                direction.y * speed,
                direction.z * speed
        );

        beam.setResetInvulnerability(resetInvulnerability);

        world.spawnEntity(beam);

        return beam;
    }

    // 対象指定射撃
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

    // 対象指定射撃 + 無敵時間リセット
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
