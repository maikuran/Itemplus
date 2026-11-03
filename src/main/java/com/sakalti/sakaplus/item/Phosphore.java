
package com.sakalti.sakaplus.item;

import java.util.List;

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

public class Phosphore extends Item {

    private static final int COOLDOWN_TICKS = 42;
    private static final int RELOAD_COOLDOWN = 20;

    private static final int MAX_AMMO = 60;
    private static final int MAX_DURABILITY = 840;

    private static final float BEAM_DAMAGE = 9.5F;
    private static final float BEAM_SPEED = 2.5F;
    private static final int BEAM_COLOR = 0xFF8C20;

    // 左右の射撃角度
    private static final float[] SPREAD_ANGLES = {
        -15.0F,
        0.0F,
        15.0F
    };

    // 上下方向の射撃角度
    private static final float PITCH_OFFSET = 0.0F;

    // リコイル
    private static final float RECOIL_STRENGTH = 3.0F;
    private static final float RECOIL_RECOVERY_SPEED = 0.22F;
    private static final float RECOIL_MAX_ANGLE = 23.0F;

    private static final int FIRE_SECONDS = 5;

    // 無敵時間を無視
    private static final boolean RESET_INVULNERABILITY = true;

    private static final String AMMO_KEY = "ammo";

    public Phosphore(Settings settings) {
        super(settings.maxDamage(MAX_DURABILITY));
    }

    @Override
    public TypedActionResult<ItemStack> use(
            World world,
            PlayerEntity user,
            Hand hand
    ) {
        ItemStack stack = user.getStackInHand(hand);

        initializeAmmo(stack);

        // スニーク中はリロード
        if (user.isSneaking()) {
            if (!world.isClient) {
                reload(user, stack);
            }

            return TypedActionResult.success(stack, world.isClient);
        }

        // 発射クールダウン
        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        int ammo = getAmmo(stack);

        // 弾切れ
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

        if (world.isClient) {

            // リコイル
            GunRecoil.recoil(
                RECOIL_STRENGTH,
                RECOIL_RECOVERY_SPEED,
                RECOIL_MAX_ANGLE
            );

        } else {

            // 3方向射撃
            for (float angle : SPREAD_ANGLES) {

                BeamAttack.shoot(
                    user,
                    BEAM_SPEED,
                    BEAM_DAMAGE,
                    BEAM_COLOR,
                    RESET_INVULNERABILITY,
                    angle,
                    PITCH_OFFSET
                );
            }

            // 弾薬を1発消費
            setAmmo(stack, ammo - 1);

            // 射撃音
            user.playSound(
                SakaplusSounds.Shoot,
                SoundCategory.PLAYERS,
                1.0F,
                0.9F
            );

            // 耐久値を1消費
            stack.damage(1, user, player -> {
                player.sendToolBreakStatus(hand);
            });

            // クールダウン
            user.getItemCooldownManager().set(
                this,
                COOLDOWN_TICKS
            );
        }

        return TypedActionResult.success(stack, world.isClient);
    }

    // リロード
    private void reload(PlayerEntity player, ItemStack stack) {

        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return;
        }

        initializeAmmo(stack);

        int ammo = getAmmo(stack);

        if (ammo >= MAX_AMMO) {
            return;
        }

        PlayerInventory inventory = player.getInventory();

        // 鉄塊1個と火薬1個を確認
        if (!hasItem(inventory, Items.IRON_NUGGET)) {
            return;
        }

        if (!hasItem(inventory, Items.GUNPOWDER)) {
            return;
        }

        // 素材消費
        consumeItem(inventory, Items.IRON_NUGGET);
        consumeItem(inventory, Items.GUNPOWDER);

        // 最大弾数まで補充
        setAmmo(stack, MAX_AMMO);

        // リロード音
        player.playSound(
            SakaplusSounds.Magazine,
            SoundCategory.PLAYERS,
            1.0F,
            1.0F
        );

        // リロードクールダウン
        player.getItemCooldownManager().set(
            this,
            RELOAD_COOLDOWN
        );
    }

    // アイテム所持確認
    private boolean hasItem(PlayerInventory inventory, Item item) {

        for (int i = 0; i < inventory.size(); i++) {

            ItemStack stack = inventory.getStack(i);

            if (stack.isOf(item) && !stack.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    // アイテムを1個消費
    private void consumeItem(PlayerInventory inventory, Item item) {

        for (int i = 0; i < inventory.size(); i++) {

            ItemStack stack = inventory.getStack(i);

            if (stack.isOf(item) && !stack.isEmpty()) {
                stack.decrement(1);
                return;
            }
        }
    }

    // 弾薬数取得
    private int getAmmo(ItemStack stack) {
        return stack.getOrCreateNbt().getInt(AMMO_KEY);
    }

    // 弾薬数設定
    private void setAmmo(ItemStack stack, int ammo) {

        stack.getOrCreateNbt().putInt(
            AMMO_KEY,
            Math.max(0, Math.min(MAX_AMMO, ammo))
        );
    }

    // 初期弾薬設定
    private void initializeAmmo(ItemStack stack) {

        if (!stack.getOrCreateNbt().contains(AMMO_KEY)) {
            setAmmo(stack, MAX_AMMO);
        }
    }

    // ツールチップ
    @Override
    public void appendTooltip(
            ItemStack stack,
            World world,
            List<Text> tooltip,
            TooltipContext context
    ) {
        initializeAmmo(stack);

        tooltip.add(
            Text.literal("§6弾薬: §f" + getAmmo(stack) + " / " + MAX_AMMO)
        );

        tooltip.add(
            Text.literal("§eダメージ: §f" + BEAM_DAMAGE)
        );

        tooltip.add(
            Text.literal("§e発射数: §f3発")
        );

        tooltip.add(
            Text.literal("§e射撃角度: §f-15° / 0° / +15°")
        );

        tooltip.add(
            Text.literal("§e射撃間隔: §f" + COOLDOWN_TICKS + " tick")
        );

        tooltip.add(
            Text.literal("§7リロード: スニーク + 右クリック")
        );

        tooltip.add(
            Text.literal("§7リロード素材: 鉄塊1個 + 火薬1個")
        );

        tooltip.add(
            Text.literal("§c燃焼時間: §f" + FIRE_SECONDS + "秒")
        );

        tooltip.add(
            Text.literal("§c無敵時間無視: §f有効")
        );
    }
}
