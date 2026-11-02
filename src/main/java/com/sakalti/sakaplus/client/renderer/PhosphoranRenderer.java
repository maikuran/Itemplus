package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.entity.PhosphoranEntity;
import com.sakalti.sakaplus.client.model.PhosphoranModel;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class PhosphoranRenderer extends MobEntityRenderer<PhosphoranEntity, PhosphoranModel> {

    private static final Identifier TEXTURE =
        new Identifier("sakaplus", "textures/entity/phosphoran.png");

    public static final EntityModelLayer MODEL_LAYER =
        new EntityModelLayer(new Identifier("sakaplus", "phosphoran"), "main");

    public PhosphoranRenderer(EntityRendererFactory.Context context) {
        super(context, new PhosphoranModel(context.getPart(MODEL_LAYER)), 0.4F);
    }

    @Override
    public Identifier getTexture(PhosphoranEntity entity) {
        return TEXTURE;
    }
}
