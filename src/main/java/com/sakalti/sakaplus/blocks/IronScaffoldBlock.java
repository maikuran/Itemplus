package com.sakalti.sakaplus.blocks; // パッケージ名をエラーログと一致させています

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class IronScaffoldBlock extends ScaffoldingBlock {
    public IronScaffoldBlock() {
        super(AbstractBlock.Settings.of(Material.METAL)
            .strength(2.5f, 10.0f)
            .requiresTool()
        );
    }

    // 1. 上面を含め、他のブロックがこのブロックに「隣接して設置できる」ようにする
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        if (context.getStack().getItem() == this.asItem()) {
            return super.canReplace(state, context);
        }
        return false;
    }

    // 2. ブロックの面が「固体の面」として扱われるようにする（修正部分）
    public boolean isSideSolid(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        // 上面（UP）に関しては固体として扱い、他のブロックを置けるようにする
        if (direction == Direction.UP) {
            return true;
        }
        // それ以外の面はバニラの足場の挙動（基本false）に任せる
        return super.isSideSolid(state, world, pos, direction);
    }
}
