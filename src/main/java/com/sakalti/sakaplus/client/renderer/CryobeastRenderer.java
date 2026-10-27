package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.client.model.CryobeastModel;
import com.sakalti.sakaplus.entity.CryobeastEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class CryobeastRenderer extends MobEntityRenderer<
        CryobeastEntity,
        CryobeastModel
> {

    public static final EntityModelLayer MODEL_LAYER =
            new EntityModelLayer(
                    new Identifier("sakaplus", "cryobeast"),
                    "main"
            );

    private static final Identifier TEXTURE =
            new Identifier(
                    "sakaplus",
                    "textures/entity/cryobeast.png"
            );

    public CryobeastRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new CryobeastModel(
                        context.getPart(MODEL_LAYER)
                ),
                0.65F
        );
    }

    @Override
    public Identifier getTexture(CryobeastEntity entity) {
        return TEXTURE;
    }
}
