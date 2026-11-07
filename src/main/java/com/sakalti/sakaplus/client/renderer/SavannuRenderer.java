package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.client.model.SavannuModel;
import com.sakalti.sakaplus.entity.SavannuEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class SavannuRenderer extends MobEntityRenderer<
        SavannuEntity,
        SavannuModel
> {

    public static final EntityModelLayer MODEL_LAYER =
            new EntityModelLayer(
                    new Identifier("sakaplus", "savannu"),
                    "main"
            );

    private static final Identifier TEXTURE =
            new Identifier(
                    "sakaplus",
                    "textures/entity/savannu.png"
            );

    public SavannuRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new SavannuModel(
                        context.getPart(MODEL_LAYER)
                ),
                0.65F
        );
    }

    @Override
    public Identifier getTexture(SavannuEntity entity) {
        return TEXTURE;
    }
}
