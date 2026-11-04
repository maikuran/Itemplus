package com.sakalti.sakaplus.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.sakalti.sakaplus.enchant.ModEnchantments;
import com.sakalti.sakaplus.item.Gun;

public final class BeamAttack {

    private BeamAttack() {
    }

    // =========================================================
    // MAGNUM DAMAGE BONUS
    // =========================================================

    /**
     * Magnum enchantment damage calculation.
     *
     * Lv1 = +5%
     * Lv2 = +10%
     * Lv3 = +15%
     * Lv4 = +20%
     * Lv5 = +25%
     */
    private static float applyMagnumBonus(
            LivingEntity shooter,
            float baseDamage
    ) {

        // プレイヤー以外には適用しない
        if (!(shooter instanceof PlayerEntity)) {
            return baseDamage;
        }

        PlayerEntity player = (PlayerEntity) shooter;

        // メインハンドのアイテムを取得
        ItemStack handStack = player.getMainHandStack();

        // 銃以外には適用しない
        if (!(handStack.getItem() instanceof Gun)) {
            return baseDamage;
        }

        // Magnumレベル取得
        int magnumLevel = EnchantmentHelper.getLevel(
                ModEnchantments.MAGNUM,
                handStack
        );

        // 最大レベル5
        magnumLevel = Math.min(magnumLevel, 5);

        if (magnumLevel <= 0) {
            return baseDamage;
        }

        // 1レベルにつき5%増加
        float multiplier = 1.0F + (magnumLevel * 0.05F);

        return baseDamage * multiplier;
    }

    // =========================================================
    // NORMAL SHOOT
    // =========================================================

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

        // Magnumダメージ適用
        beam.setDamage(
                applyMagnumBonus(shooter, damage)
        );

        beam.setBeamVelocity(shooter, speed);

        world.spawnEntity(beam);

        return beam;
    }

    // =========================================================
    // LIFE TICKS SHOOT
    // =========================================================

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

    // =========================================================
    // RESET INVULNERABILITY SHOOT
    // =========================================================

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

    // =========================================================
    // YAW / PITCH SHOOT
    // =========================================================

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

    // =========================================================
    // YAW / PITCH + RESET INVULNERABILITY
    // =========================================================

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

        // Magnumダメージ適用
        beam.setDamage(
                applyMagnumBonus(shooter, damage)
        );

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

    // =========================================================
    // TARGET SHOOT
    // =========================================================

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

        // Magnumダメージ適用
        beam.setDamage(
                applyMagnumBonus(shooter, damage)
        );

        beam.setVelocity(
                direction.x * speed,
                direction.y * speed,
                direction.z * speed
        );

        world.spawnEntity(beam);

        return beam;
    }

    // =========================================================
    // TARGET SHOOT + RESET INVULNERABILITY
    // =========================================================

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
