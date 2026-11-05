package com.sakalti.sakaplus.item;

import java.util.List;

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
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.ProjectileUtil;

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
    public void onStoppedUsing(
            ItemStack stack,
            World world,
            LivingEntity user,
            int remainingUseTicks) {

        int charge = this.getMaxUseTime(stack) - remainingUseTicks;

        float pull = getPullProgress(charge);

        if (pull < 0.1F) {
            return;
        }

        List<ItemStack> arrows = ProjectileUtil.getHandPossiblyHolding(
                user,
                item -> item instanceof ArrowItem
        );

        boolean infinite = user.getAbilities().creativeMode
                || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;

        ItemStack arrowStack;

        if (infinite && arrows.isEmpty()) {
            arrowStack = new ItemStack(Items.ARROW);
        } else if (!arrows.isEmpty()) {
            arrowStack = arrows.get(0).copy();
        } else {
            return;
        }

        if (world instanceof ServerWorld serverWorld) {

            ArrowItem arrowItem = arrowStack.getItem() instanceof ArrowItem
                    ? (ArrowItem) arrowStack.getItem()
                    : (ArrowItem) Items.ARROW;

            PersistentProjectileEntity arrow =
                    arrowItem.createArrow(serverWorld, arrowStack, user);

            arrow.setVelocity(
                    user,
                    user.getPitch(),
                    user.getYaw(),
                    0.0F,
                    pull * 3.0F,
                    1.0F
            );

            // 0.1～1.0のチャージを4.5～13.5に変換
            float normalizedPull = MathHelper.clamp(
                    (pull - 0.1F) / 0.9F,
                    0.0F,
                    1.0F
            );

            float damage = MIN_DAMAGE
                    + (MAX_DAMAGE - MIN_DAMAGE) * normalizedPull;

            arrow.setDamage(damage);

            // 最大チャージ時はクリティカル
            if (pull >= 1.0F) {
                arrow.setCritical(true);
            }

            // Powerエンチャント
            int power = EnchantmentHelper.getLevel(
                    Enchantments.POWER, stack
            );

            if (power > 0) {
                arrow.setDamage(
                        arrow.getDamage() + power * 0.5D + 0.5D
                );
            }

            // Punchエンチャント
            int punch = EnchantmentHelper.getLevel(
                    Enchantments.PUNCH, stack
            );

            if (punch > 0) {
                arrow.setPunch(punch);
            }

            // Flameエンチャント
            if (EnchantmentHelper.getLevel(
                    Enchantments.FLAME, stack) > 0) {
                arrow.setOnFireFor(100);
            }

            // 矢の消費
            if (!user.getAbilities().creativeMode && !infinite) {
                arrowStack.decrement(1);

                if (arrowStack.isEmpty()) {
                    user.getInventory().removeOne(arrowStack);
                }
            }

            // Infinity・クリエイティブでは回収不可
            if (infinite || user.getAbilities().creativeMode
                    && (arrowStack.isOf(Items.SPECTRAL_ARROW)
                    || arrowStack.isOf(Items.TIPPED_ARROW))) {
                arrow.pickupType =
                        PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }

            serverWorld.spawnEntity(arrow);
        }

        // 弓の耐久消費
        stack.damage(1, user,
                entity -> entity.sendToolBreakStatus(user.getActiveHand()));

        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_ARROW_SHOOT,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F / (world.random.nextFloat() * 0.4F + 1.2F)
                        + pull * 0.5F
        );
    }

    private static float getPullProgress(int useTicks) {
        float f = (float) useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        return Math.min(f, 1.0F);
    }
}
