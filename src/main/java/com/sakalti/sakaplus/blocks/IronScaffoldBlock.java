package com.sakalti.sakaplus.blocks; // 1. パッケージ名をエラーに合わせ修正

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class IronScaffoldBlock extends ScaffoldingBlock {
    public IronScaffoldBlock() {
        super(AbstractBlock.Settings.of(Material.METAL)
            .strength(2.5f, 10.0f)
            .requiresTool()
        );
    }

    // 1. 他のブロックがこのブロックに「隣接して設置できる」ようにする
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        if (context.getStack().getItem() == this.asItem()) {
            return super.canReplace(state, context);
        }
        return false;
    }

    // 2. 衝突判定（CollisionShape）をフルキューブにすることで、上面を含め「固体ブロック」として扱わせる
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }
}
