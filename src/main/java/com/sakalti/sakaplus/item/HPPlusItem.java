package com.sakalti.sakaplus.item;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

public class HPPlusItem extends Item {

    // 🔴 ランダムではなく、このMOD専用の固定UUIDを1つだけ定義します
    private static final UUID HP_BONUS_UUID = UUID.fromString("6f8a2c4e-1b3d-5e7f-9a0c-2b4d6e8f0a1b");

    public HPPlusItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
            var attrInstance = serverPlayer.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

            if (attrInstance != null) {
                double currentAmount = 0;
                
                // 1. すでにこのアイテムで増やした分のModifierがあるか確認
                var existingModifier = attrInstance.getModifier(HP_BONUS_UUID);
                if (existingModifier != null) {
                    // 現在の増加量を取得
                    currentAmount = existingModifier.getValue();
                    // 🔴 古いModifierを一度削除（これがないと重複エラーになります）
                    attrInstance.removeModifier(HP_BONUS_UUID);
                }

                // 2. 現在の増加量にさらに +2.0 して新しいModifierを作成
                EntityAttributeModifier modifier = new EntityAttributeModifier(
                        HP_BONUS_UUID,
                        "hp_plus_bonus",
                        currentAmount + 2.0, // ここでどんどん加算される
                        EntityAttributeModifier.Operation.ADDITION
                );

                // 3. 永続的なModifierとしてプレイヤーに再適用
                attrInstance.addPersistentModifier(modifier);
                
                // (任意) プレイヤーの現在の体力を増えた分だけ回復させる処理
                // これを入れないと、最大HPの枠だけ増えてハートは空のままになります
                serverPlayer.heal(2.0f);

                serverPlayer.sendMessage(Text.literal("最大HPが恒久的に +2 されました。"), true);

                // アイテムを1つ消費
                user.getStackInHand(hand).decrement(1);
            }
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
