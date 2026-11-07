
package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.entity.BeamAttack;
import com.sakalti.sakaplus.sound.SakaplusSounds;
import com.sakalti.sakaplus.client.item.GunRecoil;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class Fighter extends Gun {

    // CONFIGURATION

    public static final int MAX_DURABILITY = 190;

    public static final float DAMAGE = 22.5F;

    public static final float BEAM_SPEED = 3.5F;

    public static final int BEAM_COLOR = 0xFF3333;

    public static final int COOLDOWN_TICKS = 82;

    public static final int RELOAD_COOLDOWN = 20;

    public static final int MAX_AMMO = 2;

    // RECOIL

    public static final float RECOIL_STRENGTH = 13.5F;

    public static final float RECOIL_RECOVERY_SPEED = 0.12F;

    public static final float RECOIL_MAX_ANGLE = 35.0F;

    // NBT

    private static final String AMMO_KEY = "fighter_ammo";

    // NETWORK

    public static final Identifier RELOAD_PACKET =
            new Identifier("sakaplus", "fighter_reload");

    private static boolean serverRegistered = false;

    public Fighter(Settings settings) {
        super(settings, MAX_DURABILITY);
    }

    // SERVER PACKET REGISTRATION

    public static void registerServer() {

        if (serverRegistered) {
            return;
        }

        serverRegistered = true;

        ServerPlayNetworking.registerGlobalReceiver(
                RELOAD_PACKET,
                (server, player, handler, buf, responseSender) -> {

                    server.execute(() -> {

                        ItemStack main = player.getMainHandStack();
                        ItemStack off = player.getOffHandStack();

                        ItemStack rifle = ItemStack.EMPTY;

                        if (main.getItem() instanceof Fighter) {
                            rifle = main;
                        } else if (off.getItem() instanceof Fighter) {
                            rifle = off;
                        }

                        if (rifle.isEmpty()) {
                            return;
                        }

                        reload(player, rifle);
                    });
                }
        );
    }

    // SHOOT

    @Override
    public TypedActionResult<ItemStack> use(
            World world,
            PlayerEntity user,
            Hand hand
    ) {

        ItemStack stack = user.getStackInHand(hand);

        int ammo = getAmmo(stack);

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

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        user.getItemCooldownManager().set(
                this,
                COOLDOWN_TICKS
        );

        if (world.isClient) {

            GunRecoil.recoil(
                    RECOIL_STRENGTH,
                    RECOIL_RECOVERY_SPEED,
                    RECOIL_MAX_ANGLE
            );

            return TypedActionResult.success(stack, true);
        }

        BeamAttack.shoot(
                user,
                BEAM_SPEED,
                DAMAGE,
                BEAM_COLOR
        );

        decrementAmmo(stack);

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

    public static boolean reload(
            PlayerEntity player,
            ItemStack stack
    ) {

        if (player.getWorld().isClient) {
            return false;
        }

        if (!(stack.getItem() instanceof Fighter)) {
            return false;
        }

        if (getAmmo(stack) >= MAX_AMMO) {
            return false;
        }

        if (player.getItemCooldownManager()
                .isCoolingDown(stack.getItem())) {
            return false;
        }

        if (!player.getAbilities().creativeMode) {

            PlayerInventory inventory = player.getInventory();

            if (countItem(inventory, Items.IRON_INGOT) < 1) {
                return false;
            }

            if (countItem(inventory, Items.GUNPOWDER) < 3) {
                return false;
            }

            consumeItem(inventory, Items.IRON_INGOT, 1);
            consumeItem(inventory, Items.GUNPOWDER, 3);
        }

        stack.getOrCreateNbt().putInt(
                AMMO_KEY,
                MAX_AMMO
        );

        player.getItemCooldownManager().set(
                stack.getItem(),
                RELOAD_COOLDOWN
        );

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

    // COUNT MATERIALS

    private static int countItem(
            PlayerInventory inventory,
            net.minecraft.item.Item item
    ) {

        int count = 0;

        for (int i = 0; i < inventory.size(); i++) {

            ItemStack stack = inventory.getStack(i);

            if (stack.isOf(item)) {
                count += stack.getCount();
            }
        }

        return count;
    }

    // CONSUME MATERIALS

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

            ItemStack stack = inventory.getStack(i);

            if (!stack.isOf(item)) {
                continue;
            }

            int consume = Math.min(
                    remaining,
                    stack.getCount()
            );

            stack.decrement(consume);
            remaining -= consume;
        }

        inventory.markDirty();
    }

    // GET AMMO

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

    // DECREASE AMMO

    private static void decrementAmmo(ItemStack stack) {

        stack.getOrCreateNbt().putInt(
                AMMO_KEY,
                Math.max(0, getAmmo(stack) - 1)
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

        tooltip.add(Text.of("Jキー: リロード"));
        tooltip.add(Text.of("しゃがむ: スコープ"));
        tooltip.add(Text.of("右クリック: 射撃"));
        tooltip.add(Text.of("ダメージ: " + DAMAGE));
        tooltip.add(Text.of("クールダウン: 4.1秒"));
        tooltip.add(Text.of("リロード: 鉄インゴット1個 + 火薬3個"));
        tooltip.add(Text.of("耐久値: " + MAX_DURABILITY));
        tooltip.add(Text.of("反動: " + RECOIL_STRENGTH));
    }
}
