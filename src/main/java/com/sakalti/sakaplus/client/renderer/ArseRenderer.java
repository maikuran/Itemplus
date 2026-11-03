package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.entity.ArseEntity;
import com.sakalti.sakaplus.client.model.ArseModel;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ArseRenderer extends MobEntityRenderer<ArseEntity, ArseModel> {

    private static final Identifier TEXTURE =
            new Identifier("sakaplus", "textures/entity/arse.png");

    public static final EntityModelLayer MODEL_LAYER =
            new EntityModelLayer(
                    new Identifier("sakaplus", "arse"),
                    "main"
            );

    public ArseRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new ArseModel(context.getPart(MODEL_LAYER)),
                0.5F
        );
    }

    @Override
    public Identifier getTexture(ArseEntity entity) {
        return TEXTURE;
    }
}
