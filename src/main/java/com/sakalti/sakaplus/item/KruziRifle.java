
package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.entity.BeamAttack;
import com.sakalti.sakaplus.sound.SakaplusSounds;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class KruziRifle extends Gun {

    // =========================================================
    // CONFIGURATION
    // =========================================================

    public static final int MAX_DURABILITY = 350;

    public static final float DAMAGE = 75.0F;

    public static final float BEAM_SPEED = 3.5F;

    public static final int BEAM_COLOR = 0xFFD700;

    // 5.25 seconds = 105 ticks
    public static final int COOLDOWN_TICKS = 105;

    public static final int RELOAD_COOLDOWN = 20;

    public static final int MAX_AMMO = 2;

    // =========================================================
    // NBT
    // =========================================================

    private static final String AMMO_KEY = "kruzi_rifle_ammo";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public KruziRifle(Settings settings) {
        super(settings, MAX_DURABILITY);
    }

    // =========================================================
    // USE
    // =========================================================

    @Override
    public TypedActionResult<ItemStack> use(
            World world,
            PlayerEntity user,
            Hand hand
    ) {

        ItemStack stack = user.getStackInHand(hand);

        int ammo = getAmmo(stack);

        // =====================================================
        // SNEAKING: RELOAD
        // =====================================================

        if (user.isSneaking()) {

            // Scope is displayed by the client overlay.
            // Reload only when ammunition is not full.

            if (ammo >= MAX_AMMO) {
                return TypedActionResult.pass(stack);
            }

            if (user.getItemCooldownManager().isCoolingDown(this)) {
                return TypedActionResult.fail(stack);
            }

            if (world.isClient) {
                return TypedActionResult.success(stack, true);
            }

            if (!reload(user, stack)) {
                return TypedActionResult.fail(stack);
            }

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
                    0.85F
            );

            return TypedActionResult.success(stack, false);
        }

        // =====================================================
        // AMMO CHECK
        // =====================================================

        if (ammo <= 0) {

            if (!world.isClient) {
                world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        net.minecraft.sound.SoundEvents.BLOCK_NOTE_BLOCK_BASS,
                        SoundCategory.PLAYERS,
                        0.5F,
                        0.5F
                );
            }

            return TypedActionResult.fail(stack);
        }

        // =====================================================
        // COOLDOWN CHECK
        // =====================================================

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        user.getItemCooldownManager().set(
                this,
                COOLDOWN_TICKS
        );

        // =====================================================
        // CLIENT
        // =====================================================

        if (world.isClient) {
            return TypedActionResult.success(stack, true);
        }

        // =====================================================
        // SERVER: FIRE
        // =====================================================

        BeamAttack.shoot(
                user,
                BEAM_SPEED,
                DAMAGE,
                BEAM_COLOR
        );

        // =====================================================
        // DECREASE AMMO
        // =====================================================

        decrementAmmo(stack);

        // =====================================================
        // SHOOT SOUND
        // =====================================================

        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SakaplusSounds.Shoot,
                SoundCategory.PLAYERS,
                1.0F,
                0.65F
        );

        // =====================================================
        // DURABILITY
        // =====================================================

        if (!user.getAbilities().creativeMode) {

            stack.damage(
                    1,
                    user,
                    player -> player.sendToolBreakStatus(hand)
            );
        }

        return TypedActionResult.success(stack, false);
    }

    // =========================================================
    // RELOAD
    // =========================================================

    private boolean reload(
            PlayerEntity player,
            ItemStack stack
    ) {

        if (getAmmo(stack) >= MAX_AMMO) {
            return false;
        }

        if (!player.isCreative()) {

            PlayerInventory inventory = player.getInventory();

            // Required materials:
            // 1 Diamond + 3 Gunpowder

            if (countItem(inventory, Items.DIAMOND) < 1) {
                return false;
            }

            if (countItem(inventory, Items.GUNPOWDER) < 3) {
                return false;
            }

            consumeItem(inventory, Items.DIAMOND, 1);
            consumeItem(inventory, Items.GUNPOWDER, 3);
        }

        stack.getOrCreateNbt().putInt(
                AMMO_KEY,
                MAX_AMMO
        );

        return true;
    }

    // =========================================================
    // INVENTORY MATERIAL COUNT
    // =========================================================

    private int countItem(
            PlayerInventory inventory,
            net.minecraft.item.Item item
    ) {

        int count = 0;

        for (int i = 0; i < inventory.size(); i++) {

            ItemStack inventoryStack = inventory.getStack(i);

            if (inventoryStack.isOf(item)) {
                count += inventoryStack.getCount();
            }
        }

        return count;
    }

    // =========================================================
    // CONSUME MATERIALS
    // =========================================================

    private void consumeItem(
            PlayerInventory inventory,
            net.minecraft.item.Item item,
            int amount
    ) {

        int remaining = amount;

        for (int i = 0; i < inventory.size(); i++) {

            ItemStack inventoryStack = inventory.getStack(i);

            if (!inventoryStack.isOf(item)) {
                continue;
            }

            int consume = Math.min(
                    remaining,
                    inventoryStack.getCount()
            );

            inventoryStack.decrement(consume);

            remaining -= consume;

            if (remaining <= 0) {
                break;
            }
        }
    }

    // =========================================================
    // AMMO
    // =========================================================

    public static int getAmmo(ItemStack stack) {

        if (!stack.hasNbt()
                || !stack.getNbt().contains(AMMO_KEY)) {

            return MAX_AMMO;
        }

        return Math.max(
                0,
                Math.min(
                        MAX_AMMO,
                        stack.getNbt().getInt(AMMO_KEY)
                )
        );
    }

    private void decrementAmmo(ItemStack stack) {

        int ammo = getAmmo(stack);

        stack.getOrCreateNbt().putInt(
                AMMO_KEY,
                Math.max(0, ammo - 1)
        );
    }

    // =========================================================
    // INITIAL AMMO
    // =========================================================

    public static void initializeAmmo(ItemStack stack) {

        if (!stack.hasNbt()
                || !stack.getNbt().contains(AMMO_KEY)) {

            stack.getOrCreateNbt().putInt(
                    AMMO_KEY,
                    MAX_AMMO
            );
        }
    }

    // =========================================================
    // TOOLTIP
    // =========================================================

    @Override
    public void appendTooltip(
            ItemStack stack,
            World world,
            List<Text> tooltip,
            TooltipContext context
    ) {

        tooltip.add(
                Text.of("弾数: " + getAmmo(stack) + " / " + MAX_AMMO)
        );

        tooltip.add(
                Text.of("しゃがむ: スコープ / リロード")
        );

        tooltip.add(
                Text.of("ダメージ: " + DAMAGE)
        );

        tooltip.add(
                Text.of("クールダウン: 5.25秒")
        );

        tooltip.add(
                Text.of("リロード: ダイヤモンド1個 + 火薬3個")
        );

        tooltip.add(
                Text.of("耐久値: " + MAX_DURABILITY)
        );
    }
}
