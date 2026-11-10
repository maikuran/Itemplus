package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.entity.QuartzehEntity;
import com.sakalti.sakaplus.client.model.QuartzehModel;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class QuartzehRenderer extends MobEntityRenderer<QuartzehEntity, QuartzehModel> {

    private static final Identifier TEXTURE =
            new Identifier("sakaplus", "textures/entity/quartzeh.png");

    public static final EntityModelLayer MODEL_LAYER =
            new EntityModelLayer(
                    new Identifier("sakaplus", "quartzeh"),
                    "main"
            );

    public QuartzehRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new QuartzehModel(context.getPart(MODEL_LAYER)),
                0.5F
        );
    }

    @Override
    public Identifier getTexture(QuartzehEntity entity) {
        return TEXTURE;
    }
}
