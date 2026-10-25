
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

    /**
     * 触媒の消費設定
     */
    public static class CatalystCost {
        public final Item item;
        public final int count;

        public CatalystCost(Item item, int count) {
            if (item == null || count <= 0) {
                throw new IllegalArgumentException(
                    "Invalid catalyst cost");
            }

            this.item = item;
            this.count = count;
        }
    }

    /**
     * 杖の発動処理
     */
    @FunctionalInterface
    public interface StaffAction {
        void activate(World world, PlayerEntity player, Hand hand);
    }

    /**
     * 汎用魔術杖のコンストラクタ
     *
     * @param settings アイテム設定
     * @param cooldown 再使用待機時間（tick）
     * @param action 発動処理
     * @param catalystCosts 消費する触媒
     */
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

    @Override
    public TypedActionResult<ItemStack> use(
            World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {

            // すべての触媒を確認（消費前に検査）
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

            // 杖の発動処理
            action.activate(world, user, hand);

            // クールダウン
            user.getItemCooldownManager().set(this, cooldown);
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    /**
     * インベントリから触媒を消費
     */
    private void sacrificeCatalyst(
            PlayerEntity player, Item item, int count) {

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
