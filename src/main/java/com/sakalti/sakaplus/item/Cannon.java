
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

public class Cannon extends Gun {

    // CONFIGURATION

    public static final int COOLDOWN_TICKS = 16;
    public static final int RELOAD_COOLDOWN = 20;
    public static final int MAX_AMMO = 10;
    public static final int MAX_DURABILITY = 550;

    public static final float BEAM_DAMAGE = 8.25F;
    public static final float BEAM_SPEED = 2.0F;
    public static final int BEAM_COLOR = 0x8B4513;

    // RECOIL

    public static final float RECOIL_STRENGTH = 5.0F;
    public static final float RECOIL_RECOVERY_SPEED = 0.18F;
    public static final float RECOIL_MAX_ANGLE = 35.0F;

    // INVULNERABILITY RESET

    public static final boolean RESET_INVULNERABILITY = true;

    private static final String AMMO_KEY = "ammo";

    public Cannon(Settings settings) {
        super(settings, MAX_DURABILITY);
    }

    @Override
    public TypedActionResult<ItemStack> use(
            World world,
            PlayerEntity user,
            Hand hand
    ) {
        ItemStack stack = user.getStackInHand(hand);

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
                    0.8F
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
                        0.5F
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

        // SERVER: FIRE BEAM

        BeamAttack.shoot(
                user,
                BEAM_SPEED,
                BEAM_DAMAGE,
                BEAM_COLOR,
                RESET_INVULNERABILITY
        );

        // DECREASE AMMO

        decrementAmmo(stack);

        // SHOOT SOUND

        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SakaplusSounds.Shoot,
                SoundCategory.PLAYERS,
                1.5F,
                0.65F
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

    // RELOAD

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

    // AMMO

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

    // INITIAL AMMO

    public static void initializeAmmo(ItemStack stack) {

        if (!stack.hasNbt()
                || !stack.getNbt().contains(AMMO_KEY)) {

            stack.getOrCreateNbt().putInt(
                    AMMO_KEY,
                    MAX_AMMO
            );
        }
    }

    // TOOLTIP

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
                "茶色のエネルギービームを発射する"
        ));

        tooltip.add(Text.of(
                "発射ダメージ: " + BEAM_DAMAGE
        ));

        tooltip.add(Text.of(
                "発射間隔: " + COOLDOWN_TICKS + " tick"
        ));

        tooltip.add(Text.of(
                "ビーム速度: " + BEAM_SPEED
        ));

        tooltip.add(Text.of(
                "耐久値: " + MAX_DURABILITY
        ));

        tooltip.add(Text.of(
                "反動: 強い"
        ));

        tooltip.add(Text.of(
                "無敵時間リセット: "
                        + (RESET_INVULNERABILITY ? "ON" : "OFF")
        ));
    }
}
