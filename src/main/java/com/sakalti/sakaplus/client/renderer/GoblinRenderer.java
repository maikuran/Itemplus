
package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.client.model.GoblinModel;
import com.sakalti.sakaplus.entity.GoblinEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class GoblinRenderer extends MobEntityRenderer<
        GoblinEntity,
        GoblinModel<GoblinEntity>
> {

    public static final EntityModelLayer MODEL_LAYER =
            new EntityModelLayer(
                    new Identifier("sakaplus", "goblin"),
                    "main"
            );

    private static final Identifier TEXTURE =
            new Identifier(
                    "sakaplus",
                    "textures/entity/goblin.png"
            );

    public GoblinRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new GoblinModel<>(
                        context.getPart(MODEL_LAYER)
                ),
                0.3F
        );
    }

    @Override
    public Identifier getTexture(GoblinEntity entity) {
        return TEXTURE;
    }
}
