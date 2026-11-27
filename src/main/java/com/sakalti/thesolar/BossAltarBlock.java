package com.sakalti.thesolar;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public final class BossAltarBlock extends Block {
    private final EntityType<? extends Mob> bossType;
    private final Item catalyst;
    private final ResourceKey<Level> requiredDimension;

    public BossAltarBlock(BlockBehaviour.Properties properties, EntityType<? extends Mob> bossType,
                          Item catalyst, ResourceKey<Level> requiredDimension) {
        super(properties);
        this.bossType = bossType;
        this.catalyst = catalyst;
        this.requiredDimension = requiredDimension;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                          BlockPos pos, Player player, InteractionHand hand,
                                          BlockHitResult hitResult) {
        if (!stack.is(catalyst)) return InteractionResult.PASS;
        if (!level.dimension().equals(requiredDimension)) return InteractionResult.FAIL;

        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.FAIL;

        AABB searchBox = new AABB(pos).inflate(48.0);
        boolean bossAlreadyPresent = !level.getEntitiesOfClass(Entity.class, searchBox,
                entity -> entity.isAlive() && entity.getType() == bossType).isEmpty();
        if (bossAlreadyPresent) return InteractionResult.FAIL;

        Mob summoned = bossType.spawn(serverLevel, pos.above(), EntitySpawnReason.TRIGGERED);
        if (summoned == null) return InteractionResult.FAIL;

        if (!player.getAbilities().instabuild) stack.shrink(1);
        return InteractionResult.SUCCESS;
    }
}
