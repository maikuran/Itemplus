
package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.entity.KryosonEntity;
import com.sakalti.sakaplus.client.model.KryosonModel;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class KryosonRenderer extends MobEntityRenderer<KryosonEntity, KryosonModel> {

    private static final Identifier TEXTURE =
            new Identifier("sakaplus", "textures/entity/kryoson.png");

    public static final EntityModelLayer MODEL_LAYER =
            new EntityModelLayer(
                    new Identifier("sakaplus", "kryoson"),
                    "main"
            );

    public KryosonRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new KryosonModel(context.getPart(MODEL_LAYER)),
                0.5F
        );
    }

    @Override
    public Identifier getTexture(KryosonEntity entity) {
        return TEXTURE;
    }
}
