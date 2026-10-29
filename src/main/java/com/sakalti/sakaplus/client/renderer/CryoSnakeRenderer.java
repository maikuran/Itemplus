
package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.client.model.CryoSnakeModel;
import com.sakalti.sakaplus.entity.CryoSnakeEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class CryoSnakeRenderer extends MobEntityRenderer<
        CryoSnakeEntity,
        CryoSnakeModel
> {

    public static final EntityModelLayer MODEL_LAYER =
            new EntityModelLayer(
                    new Identifier("sakaplus", "cryosnake"),
                    "main"
            );

    private static final Identifier TEXTURE =
            new Identifier(
                    "sakaplus",
                    "textures/entity/cryosnake.png"
            );

    public CryoSnakeRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new CryoSnakeModel(
                        context.getPart(MODEL_LAYER)
                ),
                0.65F
        );
    }

    @Override
    public Identifier getTexture(CryoSnakeEntity entity) {
        return TEXTURE;
    }
}
