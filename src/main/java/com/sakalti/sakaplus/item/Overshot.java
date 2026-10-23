package com.sakalti.sakaplus.item;

import com.sakalti.sakaplus.entity.BeamAttack;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class Overshot extends Item {
    private static final int COOLDOWN_TICKS = 10;
    private static final int MAX_AMMO = 10;
    
    // 設定されたステータス
    private static final float BEAM_DAMAGE = 11.0F;
    private static final float BEAM_SPEED = 1.9F;
    private static final int BEAM_COLOR = 0x00FFFF; // 水色（お好みのRGBカラーに変更可能）

    public Overshot(Settings settings) {
        // 耐久値を 1536 に設定
        super(settings.maxDamage(1536));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        // スニーク右クリックでリロード
        if (user.isSneaking()) {
            if (reload(user, stack)) {
                if (!world.isClient) {
                    user.getItemCooldownManager().set(this, 20);
                    user.playSound(SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F, 1.0F);
                }
            }
            return TypedActionResult.success(stack, world.isClient());
        }

        // 残弾数チェック
        if (!stack.hasNbt() || stack.getOrCreateNbt().getInt("ammo") <= 0) {
            user.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASS, 0.5F, 0.5F);
            return TypedActionResult.fail(stack);
        }

        // クールダウンチェック
        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        user.getItemCooldownManager().set(this, COOLDOWN_TICKS);
        
        if (!world.isClient) {
            // BeamAttackを使用してビームエンティティを生成・発射
            BeamAttack.shoot(user, BEAM_SPEED, BEAM_DAMAGE, BEAM_COLOR);
            
            // 残弾数を減らす
            decrementAmmo(stack);
            
            // クリエイティブモード以外なら耐久値を1減らす
            if (!user.getAbilities().creativeMode) {
                stack.damage(1, user, (p) -> p.sendToolBreakStatus(hand));
            }
        }

        user.playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE, 1.0F, 1.5F);
        return TypedActionResult.success(stack, world.isClient());
    }

    private boolean reload(PlayerEntity player, ItemStack stack) {
        if (!player.isCreative()) {
            boolean hasIron = false;
            boolean hasBlazePowder = false;
            PlayerInventory inv = player.getInventory();

            for (int i = 0; i < inv.size(); i++) {
                ItemStack is = inv.getStack(i);
                if (!hasIron && is.isOf(Items.IRON_INGOT)) {
                    is.decrement(1);
                    hasIron = true;
                } else if (!hasBlazePowder && is.isOf(Items.BLAZE_POWDER)) {
                    is.decrement(1);
                    hasBlazePowder = true;
                }
                if (hasIron && hasBlazePowder) break;
            }

            if (!hasIron || !hasBlazePowder) return false;
        }

        stack.getOrCreateNbt().putInt("ammo", MAX_AMMO);
        return true;
    }

    private void decrementAmmo(ItemStack stack) {
        int ammo = stack.getOrCreateNbt().getInt("ammo");
        stack.getOrCreateNbt().putInt("ammo", Math.max(0, ammo - 1));
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        int ammo = stack.getOrCreateNbt().getInt("ammo");
        tooltip.add(Text.of("弾数: " + ammo + " / " + MAX_AMMO));
        tooltip.add(Text.of("Shift+右クリックでリロード"));
        tooltip.add(Text.of("強力なエネルギービームを発射する"));
    }
}
