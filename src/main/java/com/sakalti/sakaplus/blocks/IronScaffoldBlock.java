package com.sakalti.sakaplus.block;

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
            // .nonOpaque() を削除することで、完全な不透明（Opaque）ブロックになります
        );
    }

    // 1. 上面を含め、他のブロックがこのブロックに「隣接して設置できる」ようにする
    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        // 手に持っているアイテムが「この足場自身」のときだけ、バニラ足場のスタック設置（上に伸びる挙動）を許可する
        if (context.getStack().getItem() == this.asItem()) {
            return super.canReplace(state, context);
        }
        // それ以外のブロック（石や土など）を置こうとしたときは、通常の固体ブロックと同じように振る舞う
        return false;
    }

    // 2. ブロックの上面が「固体の面」として扱われるようにし、上に他のブロックを載せられるようにする
    @Override
    public boolean isSideSolidFullSquare(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        // バニラの足場は常にfalseを返して上にブロックを置けなくしているため、
        // 上面（UP）に関しては固体として扱い、ブロックを置けるようにします。
        if (direction == Direction.UP) {
            return true;
        }
        return super.isSideSolidFullSquare(state, world, pos, direction);
    }
}
