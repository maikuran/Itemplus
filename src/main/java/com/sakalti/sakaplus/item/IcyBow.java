
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class IcyBow extends BowItem {

    private static final float MIN_DAMAGE = 6.5F;
    private static final float MAX_DAMAGE = 19.5F;
    private static final float ARROW_SPEED = 3.0F;

    public IcyBow(Settings settings) {
        super(settings.maxCount(1).maxDamage(702));
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

        if (!(user instanceof PlayerEntity)) return;

        PlayerEntity player = (PlayerEntity) user;

        int elapsed = getMaxUseTime(stack) - remainingUseTicks;
        float pull = BowItem.getPullProgress(elapsed);

        if (pull < 0.1F) return;

        boolean creative = player.getAbilities().creativeMode;

        int infinityLevel = EnchantmentHelper.getLevel(
                Enchantments.INFINITY, stack
        );

        ItemStack arrowStack = player.getArrowType(stack);
        boolean hasArrow = !arrowStack.isEmpty();

        if (!hasArrow && !creative) return;

        if (!hasArrow) {
            arrowStack = new ItemStack(Items.ARROW);
        }

        if (!(arrowStack.getItem() instanceof ArrowItem)) return;
        if (!(world instanceof ServerWorld)) return;

        ServerWorld serverWorld = (ServerWorld) world;

        ArrowItem arrowItem = (ArrowItem) arrowStack.getItem();

        PersistentProjectileEntity arrow =
                arrowItem.createArrow(serverWorld, arrowStack, player);

        float velocity = pull * ARROW_SPEED;

        arrow.setVelocity(
                player,
                player.getPitch(),
                player.getYaw(),
                0.0F,
                velocity,
                1.0F
        );

        float normalizedPull = MathHelper.clamp(
                (pull - 0.1F) / 0.9F,
                0.0F,
                1.0F
        );

        float targetDamage = MIN_DAMAGE
                + (MAX_DAMAGE - MIN_DAMAGE) * normalizedPull;

        double baseDamage = targetDamage
                / Math.max(velocity, 0.1F);

        int power = EnchantmentHelper.getLevel(
                Enchantments.POWER, stack
        );

        if (power > 0) {
            baseDamage += power * 0.5D + 0.5D;
        }

        arrow.setDamage(baseDamage);
        arrow.setCritical(false);

        int punch = EnchantmentHelper.getLevel(
                Enchantments.PUNCH, stack
        );

        if (punch > 0) {
            arrow.setPunch(punch);
        }

        int flame = EnchantmentHelper.getLevel(
                Enchantments.FLAME, stack
        );

        if (flame > 0) {
            arrow.setOnFireFor(100);
        }

        // IcyBow専用の命中判定タグ
        arrow.addScoreboardTag("sakaplus_icy_bow");

        // Infinityは通常の矢のみ
        boolean infinityApplies =
                infinityLevel > 0 && arrowStack.isOf(Items.ARROW);

        if (!creative && !infinityApplies) {
            arrowStack.decrement(1);
        }

        if (creative || infinityApplies) {
            arrow.pickupType =
                    PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }

        serverWorld.spawnEntity(arrow);

        // 耐久消費
        stack.damage(1, player, entity ->
                entity.sendToolBreakStatus(player.getActiveHand())
        );

        // 発射音
        world.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ENTITY_ARROW_SHOOT,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F / (
                        world.random.nextFloat() * 0.4F + 1.2F
                ) + pull * 0.5F
        );
    }
}
