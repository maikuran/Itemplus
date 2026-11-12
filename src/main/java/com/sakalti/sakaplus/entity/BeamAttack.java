
package com.sakalti.sakaplus.entity;

import com.sakalti.sakaplus.enchant.ModEnchantments;
import com.sakalti.sakaplus.item.Gun;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class BeamAttack {

    private BeamAttack() {
    }

    private static float applyMagnumBonus(
            LivingEntity shooter,
            float baseDamage
    ) {
        if (!(shooter instanceof PlayerEntity)) {
            return baseDamage;
        }

        PlayerEntity player = (PlayerEntity) shooter;
        ItemStack handStack = player.getMainHandStack();

        if (!(handStack.getItem() instanceof Gun)) {
            return baseDamage;
        }

        int magnumLevel = EnchantmentHelper.getLevel(
                ModEnchantments.MAGNUM,
                handStack
        );

        magnumLevel = Math.min(magnumLevel, 5);

        return baseDamage * (1.0F + magnumLevel * 0.05F);
    }

    // NORMAL SHOOT

    public static BeamProjectileEntity shoot(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb
    ) {
        return shoot(
                shooter, speed, damage, rgb,
                false, 0.0F, 0.0F
        );
    }

    // LIFE TICKS SHOOT

    public static BeamProjectileEntity shoot(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb,
            int lifeTicks
    ) {
        BeamProjectileEntity beam = shoot(
                shooter, speed, damage, rgb
        );

        if (beam != null) {
            beam.setLifeTicks(lifeTicks);
        }

        return beam;
    }

    // RESET INVULNERABILITY SHOOT

    public static BeamProjectileEntity shoot(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb,
            boolean resetInvulnerability
    ) {
        return shoot(
                shooter, speed, damage, rgb,
                resetInvulnerability, 0.0F, 0.0F
        );
    }

    // YAW / PITCH SHOOT

    public static BeamProjectileEntity shoot(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb,
            float yawOffset,
            float pitchOffset
    ) {
        return shoot(
                shooter, speed, damage, rgb,
                false, yawOffset, pitchOffset
        );
    }

    // YAW / PITCH + RESET

    public static BeamProjectileEntity shoot(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb,
            boolean resetInvulnerability,
            float yawOffset,
            float pitchOffset
    ) {
        return shootCustom(
                shooter,
                speed,
                damage,
                rgb,
                resetInvulnerability,
                yawOffset,
                pitchOffset,
                0.0D,
                0.0F
        );
    }

    // CUSTOM SHOOT
    // startYOffset: vertical launch position
    // armorMultiplier: additional damage per armor point

    public static BeamProjectileEntity shootCustom(
            LivingEntity shooter,
            float speed,
            float damage,
            int rgb,
            boolean resetInvulnerability,
            float yawOffset,
            float pitchOffset,
            double startYOffset,
            float armorMultiplier
    ) {
        World world = shooter.getWorld();

        if (world.isClient) {
            return null;
        }

        BeamProjectileEntity beam =
                new BeamProjectileEntity(world, shooter);

        beam.setColor(rgb);
        beam.setDamage(applyMagnumBonus(shooter, damage));

        beam.setResetInvulnerability(resetInvulnerability);
        beam.setArmorDamageMultiplier(armorMultiplier);

        float yaw = shooter.getYaw() + yawOffset;
        float pitch = shooter.getPitch() + pitchOffset;

        float yawRad = yaw * ((float) Math.PI / 180.0F);
        float pitchRad = pitch * ((float) Math.PI / 180.0F);

        float x = -((float) Math.sin(yawRad))
                * ((float) Math.cos(pitchRad));

        float y = -((float) Math.sin(pitchRad));

        float z = ((float) Math.cos(yawRad))
                * ((float) Math.cos(pitchRad));

        Vec3d direction = new Vec3d(x, y, z).normalize();

        Vec3d start = new Vec3d(
                shooter.getX(),
                shooter.getEyeY() - 0.1D + startYOffset,
                shooter.getZ()
        );

        beam.setPosition(start.x, start.y, start.z);

        beam.setVelocity(
                direction.x * speed,
                direction.y * speed,
                direction.z * speed
        );

        world.spawnEntity(beam);

        return beam;
    }

    // TARGET SHOOT

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
        beam.setDamage(applyMagnumBonus(shooter, damage));

        beam.setVelocity(
                direction.x * speed,
                direction.y * speed,
                direction.z * speed
        );

        world.spawnEntity(beam);

        return beam;
    }

    // TARGET SHOOT + RESET

    public static BeamProjectileEntity shootAt(
            LivingEntity shooter,
            LivingEntity target,
            float speed,
            float damage,
            int rgb,
            boolean resetInvulnerability
    ) {
        BeamProjectileEntity beam = shootAt(
                shooter, target, speed, damage, rgb
        );

        if (beam != null) {
            beam.setResetInvulnerability(resetInvulnerability);
        }

        return beam;
    }
}
