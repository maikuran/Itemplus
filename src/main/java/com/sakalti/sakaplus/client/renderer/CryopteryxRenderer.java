package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.client.model.CryopteryxModel;
import com.sakalti.sakaplus.entity.CryopteryxEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class CryopteryxRenderer extends MobEntityRenderer<
        CryopteryxEntity,
        CryopteryxModel
> {

    public static final EntityModelLayer MODEL_LAYER =
            new EntityModelLayer(
                    new Identifier("sakaplus", "cryopteryx"),
                    "main"
            );

    private static final Identifier TEXTURE =
            new Identifier(
                    "sakaplus",
                    "textures/entity/cryopteryx.png"
            );

    public CryopteryxRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new CryopteryxModel(
                        context.getPart(MODEL_LAYER)
                ),
                1.0F
        );
    }

    @Override
    public Identifier getTexture(CryopteryxEntity entity) {
        return TEXTURE;
    }
}
