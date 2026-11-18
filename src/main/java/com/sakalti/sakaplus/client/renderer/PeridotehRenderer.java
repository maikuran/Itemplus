
package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.entity.PeridotehEntity;
import com.sakalti.sakaplus.client.model.PeridotehModel;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class PeridotehRenderer extends MobEntityRenderer<PeridotehEntity, PeridotehModel> {

    private static final Identifier TEXTURE = new Identifier(
            "sakaplus",
            "textures/entity/peridoteh.png"
    );

    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(
            new Identifier("sakaplus", "peridoteh"),
            "main"
    );

    public PeridotehRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new PeridotehModel(context.getPart(MODEL_LAYER)),
                0.5F
        );
    }

    @Override
    public Identifier getTexture(PeridotehEntity entity) {
        return TEXTURE;
    }
}
