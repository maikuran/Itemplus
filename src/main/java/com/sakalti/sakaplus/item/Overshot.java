package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.entity.BeamAttack;
import com.sakalti.sakaplus.sound.SakaplusSounds;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class Overshot extends Item {

    private static final int COOLDOWN_TICKS = 10;
    private static final int RELOAD_COOLDOWN = 20;
    private static final int MAX_AMMO = 10;

    private static final float BEAM_DAMAGE = 11.0F;
    private static final float BEAM_SPEED = 1.9F;
    private static final int BEAM_COLOR = 0x00FFFF;

    public Overshot(Settings settings) {
        super(settings.maxDamage(1536));
    }

    @Override
    public TypedActionResult<ItemStack> use(
            World world,
            PlayerEntity user,
            Hand hand
    ) {

        ItemStack stack = user.getStackInHand(hand);

        // リロード
        if (user.isSneaking()) {

            if (world.isClient) {
                return TypedActionResult.success(stack, true);
            }

            if (reload(user, stack)) {

                user.getItemCooldownManager().set(
                    this,
                    RELOAD_COOLDOWN
                );

                world.playSound(
                    null,
                    user.getX(),
                    user.getY(),
                    user.getZ(),
                    SakaplusSounds.Magazine,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
                );

                return TypedActionResult.success(stack, false);
            }

            return TypedActionResult.fail(stack);
        }

        // 弾数確認
        int ammo = stack.hasNbt()
                ? stack.getNbt().getInt("ammo")
                : 0;

        if (ammo <= 0) {
            return TypedActionResult.fail(stack);
        }

        // クールダウン確認
        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        user.getItemCooldownManager().set(
            this,
            COOLDOWN_TICKS
        );

        if (!world.isClient) {

            // ビーム発射
            BeamAttack.shoot(
                user,
                BEAM_SPEED,
                BEAM_DAMAGE,
                BEAM_COLOR
            );

            // 弾数減少
            decrementAmmo(stack);

            // 発射音
            world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SakaplusSounds.Shoot,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F
            );

            // 耐久値減少
            if (!user.getAbilities().creativeMode) {
                stack.damage(
                    1,
                    user,
                    player -> player.sendToolBreakStatus(hand)
                );
            }
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    private boolean reload(
            PlayerEntity player,
            ItemStack stack
    ) {

        if (!player.isCreative()) {

            PlayerInventory inventory = player.getInventory();

            boolean hasIron = false;
            boolean hasBlazePowder = false;

            // 先に両方の素材を確認
            for (int i = 0; i < inventory.size(); i++) {

                ItemStack item = inventory.getStack(i);

                if (!hasIron && item.isOf(Items.IRON_INGOT)) {
                    hasIron = true;
                }

                if (!hasBlazePowder && item.isOf(Items.BLAZE_POWDER)) {
                    hasBlazePowder = true;
                }

                if (hasIron && hasBlazePowder) {
                    break;
                }
            }

            // 素材不足なら消費しない
            if (!hasIron || !hasBlazePowder) {
                return false;
            }

            // 素材を消費
            for (int i = 0; i < inventory.size(); i++) {

                ItemStack item = inventory.getStack(i);

                if (item.isOf(Items.IRON_INGOT)) {
                    item.decrement(1);
                    break;
                }
            }

            for (int i = 0; i < inventory.size(); i++) {

                ItemStack item = inventory.getStack(i);

                if (item.isOf(Items.BLAZE_POWDER)) {
                    item.decrement(1);
                    break;
                }
            }
        }

        stack.getOrCreateNbt().putInt("ammo", MAX_AMMO);

        return true;
    }

    private void decrementAmmo(ItemStack stack) {

        int ammo = stack.getOrCreateNbt().getInt("ammo");

        stack.getOrCreateNbt().putInt(
            "ammo",
            Math.max(0, ammo - 1)
        );
    }

    @Override
    public void appendTooltip(
            ItemStack stack,
            World world,
            List<Text> tooltip,
            TooltipContext context
    ) {

        int ammo = stack.hasNbt()
                ? stack.getNbt().getInt("ammo")
                : 0;

        tooltip.add(Text.of("弾数: " + ammo + " / " + MAX_AMMO));
        tooltip.add(Text.of("Shift+右クリックでリロード"));
        tooltip.add(Text.of("強力なエネルギービームを発射する"));
    }
}
