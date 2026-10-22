
package com.sakalti.sakaplus.registry;

import com.sakalti.sakaplus.SakaPlusMod2;
import com.sakalti.sakaplus.entity.BeamProjectileEntity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ModBeamEntities {

    public static final EntityType<BeamProjectileEntity> BEAM_PROJECTILE =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier(SakaPlus.MOD_ID, "beam_projectile"),
                    FabricEntityTypeBuilder
                            .<BeamProjectileEntity>create(
                                    SpawnGroup.MISC,
                                    BeamProjectileEntity::new
                            )
                            .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                            .trackRangeBlocks(64)
                            .trackedUpdateRate(1)
                            .build()
            );

    private ModBeamEntities() {
    }

    public static void register() {
        // クラスの初期化時に登録される
    }
}
