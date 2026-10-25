
package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.entity.BeamAttack;
import com.sakalti.sakaplus.sound.SakaplusSounds;
import com.sakalti.sakaplus.client.item.GunRecoil;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;

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
    // RECOIL CONFIGURATION
    // =========================================================

    public static final float RECOIL_STRENGTH = 9.0F;

    public static final float RECOIL_RECOVERY_SPEED = 0.12F;

    public static final float RECOIL_MAX_ANGLE = 35.0F;

    // =========================================================
    // NBT
    // =========================================================

    private static final String AMMO_KEY = "kruzi_rifle_ammo";

    // Callback registration guard
    private static boolean callbacksRegistered = false;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public KruziRifle(Settings settings) {
        super(settings, MAX_DURABILITY);

        registerReloadCallbacks();
    }

    // =========================================================
    // LEFT CLICK RELOAD CALLBACKS
    // =========================================================

    private static void registerReloadCallbacks() {

        if (callbacksRegistered) {
            return;
        }

        callbacksRegistered = true;

        // -----------------------------------------------------
        // Left-click block
        // -----------------------------------------------------

        AttackBlockCallback.EVENT.register(
                (player, world, hand, pos, direction) -> {

                    ItemStack rifle = findRifle(player);

                    if (rifle.isEmpty()) {
                        return ActionResult.PASS;
                    }

                    if (!world.isClient) {
                        reload(player, rifle);
                    }

                    // Cancel normal block attack
                    return ActionResult.FAIL;
                }
        );

        // -----------------------------------------------------
        // Left-click entity
        // -----------------------------------------------------

        AttackEntityCallback.EVENT.register(
                (player, world, hand, entity, hitResult) -> {

                    ItemStack rifle = findRifle(player);

                    if (rifle.isEmpty()) {
                        return ActionResult.PASS;
                    }

                    if (!world.isClient) {
                        reload(player, rifle);
                    }

                    // Cancel normal entity attack
                    return ActionResult.FAIL;
                }
        );
    }

    // =========================================================
    // FIND RIFLE IN BOTH HANDS
    // =========================================================

    private static ItemStack findRifle(PlayerEntity player) {

        ItemStack mainHand = player.getMainHandStack();

        if (mainHand.getItem() instanceof KruziRifle) {
            return mainHand;
        }

        ItemStack offHand = player.getOffHandStack();

        if (offHand.getItem() instanceof KruziRifle) {
            return offHand;
        }

        return ItemStack.EMPTY;
    }

    // =========================================================
    // RIGHT CLICK: SHOOT
    // =========================================================

    @Override
    public TypedActionResult<ItemStack> use(
            World world,
            PlayerEntity user,
            Hand hand
    ) {

        ItemStack stack = user.getStackInHand(hand);

        int ammo = getAmmo(stack);

        // -----------------------------------------------------
        // AMMO CHECK
        // -----------------------------------------------------

        if (ammo <= 0) {

            if (!world.isClient) {
                world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        SoundEvents.BLOCK_NOTE_BLOCK_BASS,
                        SoundCategory.PLAYERS,
                        0.5F,
                        0.5F
                );
            }

            return TypedActionResult.fail(stack);
        }

        // -----------------------------------------------------
        // COOLDOWN CHECK
        // -----------------------------------------------------

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        user.getItemCooldownManager().set(
                this,
                COOLDOWN_TICKS
        );

        // -----------------------------------------------------
        // CLIENT: RECOIL
        // -----------------------------------------------------

        if (world.isClient) {

            GunRecoil.recoil(
                    RECOIL_STRENGTH,
                    RECOIL_RECOVERY_SPEED,
                    RECOIL_MAX_ANGLE
            );

            return TypedActionResult.success(stack, true);
        }

        // -----------------------------------------------------
        // SERVER: FIRE
        // -----------------------------------------------------

        BeamAttack.shoot(
                user,
                BEAM_SPEED,
                DAMAGE,
                BEAM_COLOR
        );

        // -----------------------------------------------------
        // DECREASE AMMO
        // -----------------------------------------------------

        decrementAmmo(stack);

        // -----------------------------------------------------
        // SHOOT SOUND
        // -----------------------------------------------------

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

        // -----------------------------------------------------
        // DURABILITY
        // -----------------------------------------------------

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

    public static boolean reload(
            PlayerEntity player,
            ItemStack stack
    ) {

        if (player.getWorld().isClient) {
            return false;
        }

        if (!(stack.getItem() instanceof KruziRifle)) {
            return false;
        }

        // Already full
        if (getAmmo(stack) >= MAX_AMMO) {
            return false;
        }

        // Reload cooldown
        if (player.getItemCooldownManager()
                .isCoolingDown(stack.getItem())) {
            return false;
        }

        // -----------------------------------------------------
        // MATERIAL CHECK
        // -----------------------------------------------------

        if (!player.getAbilities().creativeMode) {

            PlayerInventory inventory = player.getInventory();

            if (countItem(inventory, Items.DIAMOND) < 1) {
                return false;
            }

            if (countItem(inventory, Items.GUNPOWDER) < 3) {
                return false;
            }

            // Consume materials
            consumeItem(inventory, Items.DIAMOND, 1);

            consumeItem(inventory, Items.GUNPOWDER, 3);
        }

        // -----------------------------------------------------
        // FILL AMMO
        // -----------------------------------------------------

        stack.getOrCreateNbt().putInt(
                AMMO_KEY,
                MAX_AMMO
        );

        // -----------------------------------------------------
        // RELOAD COOLDOWN
        // -----------------------------------------------------

        player.getItemCooldownManager().set(
                stack.getItem(),
                RELOAD_COOLDOWN
        );

        // -----------------------------------------------------
        // RELOAD SOUND
        // -----------------------------------------------------

        player.getWorld().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SakaplusSounds.Magazine,
                SoundCategory.PLAYERS,
                1.0F,
                0.85F
        );

        return true;
    }

    // =========================================================
    // INVENTORY MATERIAL COUNT
    // =========================================================

    private static int countItem(
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

    private static void consumeItem(
            PlayerInventory inventory,
            net.minecraft.item.Item item,
            int amount
    ) {

        int remaining = amount;

        for (int i = 0; i < inventory.size(); i++) {

            if (remaining <= 0) {
                break;
            }

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
        }

        inventory.markDirty();
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

    // =========================================================
    // DECREASE AMMO
    // =========================================================

    private static void decrementAmmo(ItemStack stack) {

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
                Text.of(
                        "弾数: " + getAmmo(stack) + " / " + MAX_AMMO
                )
        );

        tooltip.add(
                Text.of("左クリック: リロード")
        );

        tooltip.add(
                Text.of("しゃがむ: スコープ")
        );

        tooltip.add(
                Text.of("右クリック: 射撃")
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

        tooltip.add(
                Text.of("反動: " + RECOIL_STRENGTH)
        );
    }
}
