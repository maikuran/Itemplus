
package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.entity.BeamAttack;
import com.sakalti.sakaplus.sound.SakaplusSounds;
import com.sakalti.sakaplus.client.item.GunRecoil;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class Cryocannon extends Gun {

    // CONFIGURATION

    public static final int COOLDOWN_TICKS = 23;
    public static final int RELOAD_COOLDOWN = 20;

    public static final int MAX_AMMO = 6;
    public static final int MAX_DURABILITY = 1000;

    public static final float BEAM_DAMAGE = 11.5F;
    public static final float BEAM_SPEED = 1.15F;

    public static final int BEAM_COLOR = 0x75DFFF;

    // ARMOR BONUS

    // Armor 20 = +3.33 damage
    public static final float ARMOR_DAMAGE_MULTIPLIER = 0.1665F;

    // THREE BEAMS

    public static final int BEAM_COUNT = 3;

    // Total vertical spread = 0.75 blocks

    public static final double BEAM_VERTICAL_SPREAD = 0.375D;

    // RECOIL

    public static final float RECOIL_STRENGTH = 6.0F;
    public static final float RECOIL_RECOVERY_SPEED = 0.18F;
    public static final float RECOIL_MAX_ANGLE = 38.0F;

    public static final boolean RESET_INVULNERABILITY = true;

    private static final String AMMO_KEY = "ammo";

    public Cryocannon(Settings settings) {
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

        initializeAmmo(stack);

        // RELOAD

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
                    1.1F
            );

            return TypedActionResult.success(stack, false);
        }

        // AMMO CHECK

        if (getAmmo(stack) <= 0) {

            if (!world.isClient) {
                world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        SoundEvents.BLOCK_NOTE_BLOCK_BASS,
                        SoundCategory.PLAYERS,
                        0.5F,
                        0.6F
                );
            }

            return TypedActionResult.fail(stack);
        }

        // COOLDOWN CHECK

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        user.getItemCooldownManager().set(
                this,
                COOLDOWN_TICKS
        );

        // CLIENT

        if (world.isClient) {

            GunRecoil.recoil(
                    RECOIL_STRENGTH,
                    RECOIL_RECOVERY_SPEED,
                    RECOIL_MAX_ANGLE
            );

            return TypedActionResult.success(stack, true);
        }

        // =====================================================
        // SERVER: THREE VERTICAL BEAMS
        // =====================================================

        for (int i = 0; i < BEAM_COUNT; i++) {

            // -0.375 / 0 / +0.375

            double verticalOffset =
                    (i - 1) * BEAM_VERTICAL_SPREAD;

            BeamAttack.shootCustom(
                    user,
                    BEAM_SPEED,
                    BEAM_DAMAGE,
                    BEAM_COLOR,
                    RESET_INVULNERABILITY,
                    0.0F,
                    0.0F,
                    verticalOffset,
                    ARMOR_DAMAGE_MULTIPLIER
            );
        }

        // ONE AMMO FOR THREE BEAMS

        decrementAmmo(stack);

        // SHOOT SOUND

        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SakaplusSounds.Shoot,
                SoundCategory.PLAYERS,
                1.6F,
                1.2F
        );

        // DURABILITY

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
            int gunpowderSlot = -1;

            for (int i = 0; i < inventory.size(); i++) {

                ItemStack inventoryStack = inventory.getStack(i);

                if (ironSlot == -1
                        && inventoryStack.isOf(Items.IRON_NUGGET)) {
                    ironSlot = i;
                }

                if (gunpowderSlot == -1
                        && inventoryStack.isOf(Items.GUNPOWDER)) {
                    gunpowderSlot = i;
                }

                if (ironSlot != -1 && gunpowderSlot != -1) {
                    break;
                }
            }

            if (ironSlot == -1 || gunpowderSlot == -1) {
                return false;
            }

            inventory.getStack(ironSlot).decrement(1);
            inventory.getStack(gunpowderSlot).decrement(1);
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

        tooltip.add(Text.of(
                "弾数: " + getAmmo(stack) + " / " + MAX_AMMO
        ));

        tooltip.add(Text.of(
                "Shift + 右クリック: リロード"
        ));

        tooltip.add(Text.of(
                "3本の氷結エネルギービームを縦方向に同時発射"
        ));

        tooltip.add(Text.of(
                "基本ダメージ: " + BEAM_DAMAGE
        ));

        tooltip.add(Text.of(
                "防御力1につき追加ダメージ: +"
                        + ARMOR_DAMAGE_MULTIPLIER
        ));

        tooltip.add(Text.of(
                "防御力20の場合: 追加ダメージ +3.33"
        ));

        tooltip.add(Text.of(
                "発射間隔: 1.15秒 (23 tick)"
        ));

        tooltip.add(Text.of(
                "ビーム速度: " + BEAM_SPEED
        ));

        tooltip.add(Text.of(
                "最大垂直発射幅: 0.75m"
        ));

        tooltip.add(Text.of(
                "装填数: " + MAX_AMMO
        ));

        tooltip.add(Text.of(
                "耐久値: " + MAX_DURABILITY
        ));
    }
}
