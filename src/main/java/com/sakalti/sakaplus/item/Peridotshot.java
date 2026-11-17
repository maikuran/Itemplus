package com.sakalti.sakaplus.item;

import java.util.List;

import com.sakalti.sakaplus.entity.BeamAttack;
import com.sakalti.sakaplus.sound.SakaplusSounds;
import com.sakalti.sakaplus.client.item.GunRecoil;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffects;
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

public class Peridotshot extends Gun {

    // CONFIGURATION

    private static final int COOLDOWN_TICKS = 2;
    private static final int RELOAD_COOLDOWN = 20;

    private static final int MAX_AMMO = 85;
    private static final int MAX_DURABILITY = 8750;

    private static final float BEAM_DAMAGE = 4.0F;
    private static final float BEAM_SPEED = 2.6F;
    private static final int BEAM_COLOR = 0x7FFF00;

    // SINGLE SHOT

    private static final float SPREAD_ANGLE = 0.0F;
    private static final float PITCH_OFFSET = 0.0F;

    // RECOIL (やや強め)

    private static final float RECOIL_STRENGTH = 4.2F;
    private static final float RECOIL_RECOVERY_SPEED = 0.22F;
    private static final float RECOIL_MAX_ANGLE = 18.0F;

    // POISON III

    private static final int EFFECT_DURATION = 6 * 20;
    private static final int EFFECT_AMPLIFIER = 2;

    private static final boolean RESET_INVULNERABILITY = true;

    private static final String AMMO_KEY = "ammo";

    public Peridotshot(Settings settings) {
        super(settings, MAX_DURABILITY);
    }

    // USE

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

            reload(user, stack);

            return TypedActionResult.success(stack, false);
        }

        // INITIAL AMMO

        if (!world.isClient) {
            initializeAmmo(stack);
        }

        // COOLDOWN

        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        // AMMO CHECK

        int ammo = getAmmo(stack);

        if (ammo <= 0) {

            if (!world.isClient) {
                user.playSound(
                    SakaplusSounds.Magazine,
                    SoundCategory.PLAYERS,
                    1.0F,
                    0.8F
                );
            }

            return TypedActionResult.fail(stack);
        }

        // CLIENT RECOIL

        if (world.isClient) {

            GunRecoil.recoil(
                RECOIL_STRENGTH,
                RECOIL_RECOVERY_SPEED,
                RECOIL_MAX_ANGLE
            );

            return TypedActionResult.success(stack, true);
        }

        // SERVER: SINGLE SHOT

        BeamAttack.shootCustom(
            user,
            BEAM_SPEED,
            BEAM_DAMAGE,
            BEAM_COLOR,
            RESET_INVULNERABILITY,
            SPREAD_ANGLE,
            PITCH_OFFSET,
            0.0D,
            0.0F,
            StatusEffects.POISON,
            EFFECT_DURATION,
            EFFECT_AMPLIFIER
        );

        // DECREASE AMMO

        setAmmo(stack, ammo - 1);

        // SHOOT SOUND

        user.playSound(
            SakaplusSounds.Shoot,
            SoundCategory.PLAYERS,
            1.0F,
            1.45F
        );

        // DURABILITY

        if (!user.getAbilities().creativeMode) {
            stack.damage(1, user, player -> {
                player.sendToolBreakStatus(hand);
            });
        }

        // COOLDOWN

        user.getItemCooldownManager().set(
            this,
            COOLDOWN_TICKS
        );

        return TypedActionResult.success(stack, false);
    }

    // RELOAD

    private void reload(
            PlayerEntity player,
            ItemStack stack
    ) {

        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return;
        }

        initializeAmmo(stack);

        int ammo = getAmmo(stack);

        if (ammo >= MAX_AMMO) {
            return;
        }

        if (!player.isCreative()) {

            PlayerInventory inventory = player.getInventory();

            if (!hasItem(inventory, Items.IRON_NUGGET)) {
                return;
            }

            if (!hasItem(inventory, Items.GUNPOWDER)) {
                return;
            }

            consumeItem(inventory, Items.IRON_NUGGET);
            consumeItem(inventory, Items.GUNPOWDER);
        }

        setAmmo(stack, MAX_AMMO);

        player.playSound(
            SakaplusSounds.Magazine,
            SoundCategory.PLAYERS,
            1.0F,
            1.2F
        );

        player.getItemCooldownManager().set(
            this,
            RELOAD_COOLDOWN
        );
    }

    // ITEM CHECK

    private boolean hasItem(
            PlayerInventory inventory,
            Item item
    ) {

        for (int i = 0; i < inventory.size(); i++) {

            ItemStack stack = inventory.getStack(i);

            if (stack.isOf(item) && !stack.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    // CONSUME ITEM

    private void consumeItem(
            PlayerInventory inventory,
            Item item
    ) {

        for (int i = 0; i < inventory.size(); i++) {

            ItemStack stack = inventory.getStack(i);

            if (stack.isOf(item) && !stack.isEmpty()) {
                stack.decrement(1);
                return;
            }
        }

        // ITEM NOT FOUND
    }

    // AMMO GET

    private int getAmmo(ItemStack stack) {

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

    // AMMO SET

    private void setAmmo(
            ItemStack stack,
            int ammo
    ) {

        stack.getOrCreateNbt().putInt(
            AMMO_KEY,
            Math.max(0, Math.min(MAX_AMMO, ammo))
        );
    }

    // INITIAL AMMO

    private void initializeAmmo(ItemStack stack) {

        if (!stack.hasNbt()
                || !stack.getNbt().contains(AMMO_KEY)) {

            setAmmo(stack, MAX_AMMO);
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

        int ammo = getAmmo(stack);

        tooltip.add(
            Text.literal("§a弾薬: §f" + ammo + " / " + MAX_AMMO)
        );

        tooltip.add(
            Text.literal("§eダメージ: §f" + BEAM_DAMAGE)
        );

        tooltip.add(
            Text.literal("§a発射数: §f1発")
        );

        tooltip.add(
            Text.literal("§a弾速: §f" + BEAM_SPEED)
        );

        tooltip.add(
            Text.literal("§a弾色: §f黄緑")
        );

        tooltip.add(
            Text.literal("§a命中効果: §f毒 III")
        );

        tooltip.add(
            Text.literal("§a毒時間: §f6秒")
        );

        tooltip.add(
            Text.literal("§e発射間隔: §f" + COOLDOWN_TICKS + " tick")
        );

        tooltip.add(
            Text.literal("§7リロード: スニーク + 右クリック")
        );

        tooltip.add(
            Text.literal("§7リロード素材: 鉄塊1個 + 火薬1個")
        );

        tooltip.add(
            Text.literal("§c無敵時間リセット: §f有効")
        );
    }
}
