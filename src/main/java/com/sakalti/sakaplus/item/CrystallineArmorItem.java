package com.sakalti.sakaplus.item;

import java.util.function.Predicate;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class DansiteBowItem extends BowItem {

    private static final float MIN_DAMAGE = 4.5F;
    private static final float MAX_DAMAGE = 13.5F;

    public DansiteBowItem(Settings settings) {
        super(settings.maxDamage(550));
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return stack -> stack.getItem() instanceof ArrowItem;
    }

    @Override
    public void onStoppedUsing(
            ItemStack stack,
            World world,
            LivingEntity user,
            int remainingUseTicks) {

        if (!(user instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity player = (PlayerEntity) user;

        int charge = this.getMaxUseTime(stack) - remainingUseTicks;

        float pull = BowItem.getPullProgress(charge);

        if (pull < 0.1F) {
            return;
        }

        boolean creative = player.getAbilities().creativeMode;

        boolean infinity = EnchantmentHelper.getLevel(
                Enchantments.INFINITY, stack
        ) > 0;

        ItemStack arrowStack = player.getProjectileType(stack);

        if (arrowStack.isEmpty()) {
            if (!creative) {
                return;
            }

            arrowStack = new ItemStack(Items.ARROW);
        }

        if (!(world instanceof ServerWorld)) {
            return;
        }

        ServerWorld serverWorld = (ServerWorld) world;

        ArrowItem arrowItem = (ArrowItem) arrowStack.getItem();

        PersistentProjectileEntity arrow =
                arrowItem.createArrow(serverWorld, arrowStack, player);

        // 発射速度
        arrow.setVelocity(
                player,
                player.getPitch(),
                player.getYaw(),
                0.0F,
                pull * 3.0F,
                1.0F
        );

        // チャージに応じたダメージ
        float normalizedPull = Math.max(
                0.0F,
                Math.min(1.0F, (pull - 0.1F) / 0.9F)
        );

        float damage = MIN_DAMAGE
                + (MAX_DAMAGE - MIN_DAMAGE) * normalizedPull;

        arrow.setDamage(damage);

        // クリティカル追加ダメージを無効化
        arrow.setCritical(false);

        // Power
        int power = EnchantmentHelper.getLevel(
                Enchantments.POWER, stack
        );

        if (power > 0) {
            arrow.setDamage(
                    arrow.getDamage() + power * 0.5D + 0.5D
            );
        }

        // Punch
        int punch = EnchantmentHelper.getLevel(
                Enchantments.PUNCH, stack
        );

        if (punch > 0) {
            arrow.setPunch(punch);
        }

        // Flame
        if (EnchantmentHelper.getLevel(
                Enchantments.FLAME, stack) > 0) {
            arrow.setOnFireFor(100);
        }

        // 矢の消費
        boolean infinityApplies = infinity
                && arrowStack.isOf(Items.ARROW);

        if (!creative && !infinityApplies) {
            arrowStack.decrement(1);
        }

        // 回収制限
        if (creative || infinityApplies) {
            arrow.pickupType =
                    PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }

        // 発射
        serverWorld.spawnEntity(arrow);

        // 耐久消費
        stack.damage(1, player, entity ->
                entity.sendToolBreakStatus(player.getActiveHand())
        );
    }
}
