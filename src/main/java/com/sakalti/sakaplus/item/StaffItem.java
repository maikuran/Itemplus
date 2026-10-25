package com.sakalti.sakaplus.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class StaffItem extends Item {

    private final List<CatalystCost> catalystCosts;
    private final StaffAction action;
    private final int cooldown;

    public static class CatalystCost {
        public final Item item;
        public final int count;

        public CatalystCost(Item item, int count) {
            if (item == null || count <= 0) {
                throw new IllegalArgumentException("Invalid catalyst cost");
            }

            this.item = item;
            this.count = count;
        }
    }

    @FunctionalInterface
    public interface StaffAction {
        void activate(World world, PlayerEntity player, Hand hand);
    }

    public StaffItem(
            Settings settings,
            int cooldown,
            StaffAction action,
            CatalystCost... catalystCosts) {

        super(settings);

        if (cooldown < 0 || action == null) {
            throw new IllegalArgumentException(
                "Invalid staff configuration");
        }

        this.cooldown = cooldown;
        this.action = action;
        this.catalystCosts = List.of(catalystCosts);
    }

    /**
     * エンチャント適性
     */
    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public TypedActionResult<ItemStack> use(
            World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {

            // 触媒の所持数を確認
            if (!user.getAbilities().creativeMode) {
                for (CatalystCost cost : catalystCosts) {
                    if (user.getInventory().count(cost.item)
                            < cost.count) {
                        return TypedActionResult.fail(stack);
                    }
                }

                // 触媒を消費
                for (CatalystCost cost : catalystCosts) {
                    sacrificeCatalyst(
                        user, cost.item, cost.count);
                }
            }

            // 魔法を発動
            action.activate(world, user, hand);

            // 耐久を1消費（クリエイティブでは消費しない）
            if (!user.getAbilities().creativeMode
                    && stack.isDamageable()) {
                stack.damage(
                    1,
                    user,
                    player -> player.sendToolBreakStatus(hand)
                );
            }

            // クールダウン
            user.getItemCooldownManager().set(this, cooldown);
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    /**
     * インベントリから触媒を消費
     */
    private void sacrificeCatalyst(
            PlayerEntity player,
            Item item,
            int count) {

        int remaining = count;

        for (int i = 0;
             i < player.getInventory().size() && remaining > 0;
             i++) {

            ItemStack invStack =
                player.getInventory().getStack(i);

            if (invStack.isOf(item)) {
                int amount = Math.min(
                    invStack.getCount(), remaining);

                invStack.decrement(amount);
                remaining -= amount;
            }
        }
    }
}
