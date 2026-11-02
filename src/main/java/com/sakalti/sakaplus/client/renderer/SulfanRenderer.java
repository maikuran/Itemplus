package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.entity.SulfanEntity;
import com.sakalti.sakaplus.client.model.SulfanModel;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class SulfanRenderer extends MobEntityRenderer<SulfanEntity, SulfanModel> {

    private static final Identifier TEXTURE =
        new Identifier("sakaplus", "textures/entity/sulfan.png");

    public static final EntityModelLayer MODEL_LAYER =
        new EntityModelLayer(new Identifier("sakaplus", "sulfan"), "main");

    public SulfanRenderer(EntityRendererFactory.Context context) {
        super(context, new SulfanModel(context.getPart(MODEL_LAYER)), 0.6F);
    }

    @Override
    public Identifier getTexture(SulfanEntity entity) {
        return TEXTURE;
    }
}
