
package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.entity.BeamAttack;
import com.sakalti.sakaplus.sound.SakaplusSounds;
import com.sakalti.sakaplus.client.item.GunRecoil;

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

    // =========================================================
    // CONFIGURATION
    // =========================================================

    public static final int COOLDOWN_TICKS = 10;

    public static final int RELOAD_COOLDOWN = 20;

    public static final int MAX_AMMO = 10;

    public static final int MAX_DURABILITY = 1536;

    public static final float BEAM_DAMAGE = 11.0F;

    public static final float BEAM_SPEED = 1.9F;

    public static final int BEAM_COLOR = 0x00FFFF;

    // =========================================================
    // RECOIL CONFIGURATION
    // =========================================================

    // 発射時の反動の強さ
    public static final float RECOIL_STRENGTH = 4.0F;

    // 反動からの復帰速度
    public static final float RECOIL_RECOVERY_SPEED = 0.18F;

    // 最大反動角度
    public static final float RECOIL_MAX_ANGLE = 18.0F;

    // =========================================================
    // NBT
    // =========================================================

    private static final String AMMO_KEY = "ammo";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Overshot(Settings settings) {
        super(settings.maxDamage(MAX_DURABILITY));
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

        // =====================================================
        // RELOAD
        // =====================================================

        if (user.isSneaking()) {

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
                    1.0F
            );

            return TypedActionResult.success(stack, false);
        }

        // =====================================================
        // AMMO CHECK
        // =====================================================

        int ammo = getAmmo(stack);

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

            // Custom recoil
            GunRecoil.recoil(
                    RECOIL_STRENGTH,
                    RECOIL_RECOVERY_SPEED,
                    RECOIL_MAX_ANGLE
            );

            return TypedActionResult.success(stack, true);
        }

        // =====================================================
        // SERVER: FIRE BEAM
        // =====================================================

        BeamAttack.shoot(
                user,
                BEAM_SPEED,
                BEAM_DAMAGE,
                BEAM_COLOR
        );

        // =====================================================
        // SERVER: DECREASE AMMO
        // =====================================================

        decrementAmmo(stack);

        // =====================================================
        // SERVER: SHOOT SOUND
        // =====================================================

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

        // =====================================================
        // SERVER: DURABILITY
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

        if (!player.isCreative()) {

            PlayerInventory inventory = player.getInventory();

            int ironSlot = -1;
            int blazeSlot = -1;

            for (int i = 0; i < inventory.size(); i++) {

                ItemStack inventoryStack = inventory.getStack(i);

                if (ironSlot == -1
                        && inventoryStack.isOf(Items.IRON_NUGGET)) {

                    ironSlot = i;
                }

                if (blazeSlot == -1
                        && inventoryStack.isOf(Items.GUNPOWDER)) {

                    blazeSlot = i;
                }

                if (ironSlot != -1 && blazeSlot != -1) {
                    break;
                }
            }

            if (ironSlot == -1 || blazeSlot == -1) {
                return false;
            }

            inventory.getStack(ironSlot).decrement(1);
            inventory.getStack(blazeSlot).decrement(1);
        }

        stack.getOrCreateNbt().putInt(
                AMMO_KEY,
                MAX_AMMO
        );

        return true;
    }

    // =========================================================
    // AMMO
    // =========================================================

    public static int getAmmo(ItemStack stack) {

        if (!stack.hasNbt()) {
            return 0;
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

        int ammo = getAmmo(stack);

        tooltip.add(
                Text.of("弾数: " + ammo + " / " + MAX_AMMO)
        );

        tooltip.add(
                Text.of("Shift + 右クリック: リロード")
        );

        tooltip.add(
                Text.of("強力なエネルギービームを発射する")
        );

        tooltip.add(
                Text.of("発射ダメージ: " + BEAM_DAMAGE)
        );

        tooltip.add(
                Text.of("耐久値: " + MAX_DURABILITY)
        );

        tooltip.add(
                Text.of("反動の強さ: " + RECOIL_STRENGTH)
        );
    }
}
