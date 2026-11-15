
package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.client.model.VolcanicKingModel;
import com.sakalti.sakaplus.entity.VolcanicKingEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class VolcanicKingRenderer extends MobEntityRenderer<
        VolcanicKingEntity,
        VolcanicKingModel
> {

    public static final EntityModelLayer MODEL_LAYER =
            new EntityModelLayer(
                    new Identifier("sakaplus", "volcanic_king"),
                    "main"
            );

    private static final Identifier TEXTURE =
            new Identifier(
                    "sakaplus",
                    "textures/entity/volcanic_king.png"
            );

    public VolcanicKingRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new VolcanicKingModel(
                        context.getPart(MODEL_LAYER)
                ),
                0.85F
        );
    }

    @Override
    public Identifier getTexture(VolcanicKingEntity entity) {
        return TEXTURE;
    }
}
