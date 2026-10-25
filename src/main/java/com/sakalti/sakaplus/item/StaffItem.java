package com.sakalti.sakaplus.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class StaffItem extends Gun {

    private final Item catalystItem;

    /**
     * @param settings アイテムの設定
     * @param durability 魔力の耐久値
     * @param catalystItem 詠唱に必要な触媒アイテム
     */
    public StaffItem(Settings settings, int durability, Item catalystItem) {
        super(settings, durability);
        this.catalystItem = catalystItem;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!user.getAbilities().creativeMode) {
            // 精神を集中し、インベントリ内の触媒（万物）を確認
            if (user.getInventory().count(this.catalystItem) < 1) {
                return TypedActionResult.fail(stack);
            }

            // 触媒を捧げ、代償を支払う
            sacrificeCatalyst(user, this.catalystItem, 1);
            // 魔力の依代（杖）に負荷を刻む
            stack.damage(1, user, (p) -> p.sendToolBreakStatus(hand));
        }

        // 次の詠唱（インターバル）まで精神を研ぎ澄ます (0.83秒)
        user.getItemCooldownManager().set(this, 17);

        return TypedActionResult.success(stack, world.isClient());
    }

    /**
     * 術式を成立させるため、インベントリから触媒を消失させる
     */
    private void sacrificeCatalyst(PlayerEntity player, Item item, int count) {
        int remaining = count;
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack invStack = player.getInventory().getStack(i);
            if (invStack.isOf(item)) {
                int stackCount = invStack.getCount();
                if (stackCount <= remaining) {
                    remaining -= stackCount;
                    player.getInventory().setStack(i, ItemStack.EMPTY);
                } else {
                    invStack.decrement(remaining);
                    break;
                }
                if (remaining <= 0) break;
            }
        }
    }
}
