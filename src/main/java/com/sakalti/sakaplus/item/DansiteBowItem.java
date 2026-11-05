
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

public class DansiteBowItem extends BowItem {

    private static final float MIN_DAMAGE = 4.5F;
    private static final float MAX_DAMAGE = 13.5F;

    public DansiteBowItem(Settings settings) {
        super(settings.maxCount(1).maxDamage(550));
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

        int infinityLevel = EnchantmentHelper.getLevel(
                Enchantments.INFINITY, stack
        );

        // Yarn 1.19.2: getArrowType()
        ItemStack arrowStack = player.getArrowType(stack);

        boolean hasArrow = !arrowStack.isEmpty();

        if (!hasArrow && !creative) {
            return;
        }

        // クリエイティブ用の通常矢
        if (!hasArrow) {
            arrowStack = new ItemStack(Items.ARROW);
        }

        if (!(world instanceof ServerWorld)) {
            return;
        }

        ServerWorld serverWorld = (ServerWorld) world;

        ArrowItem arrowItem = (ArrowItem) arrowStack.getItem();

        // 通常の矢Entityを生成
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

        // チャージ量を0～1に変換
        float normalizedPull = MathHelper.clamp(
                (pull - 0.1F) / 0.9F,
                0.0F,
                1.0F
        );

        // ダメージ4.5～13.5
        float damage = MIN_DAMAGE
                + (MAX_DAMAGE - MIN_DAMAGE) * normalizedPull;

        arrow.setDamage(damage);

        // クリティカル追加ダメージを無効化
        arrow.setCritical(false);

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
        int flame = EnchantmentHelper.getLevel(
                Enchantments.FLAME, stack
        );

        if (flame > 0) {
            arrow.setOnFireFor(100);
        }

        // Infinityは通常の矢のみ対象
        boolean infinityApplies =
                infinityLevel > 0 && arrowStack.isOf(Items.ARROW);

        // 矢の消費
        if (!creative && !infinityApplies) {
            arrowStack.decrement(1);
        }

        // 回収制限
        if (creative || infinityApplies) {
            arrow.pickupType =
                    PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }

        // 矢をワールドに追加
        serverWorld.spawnEntity(arrow);

        // 弓の耐久消費
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
