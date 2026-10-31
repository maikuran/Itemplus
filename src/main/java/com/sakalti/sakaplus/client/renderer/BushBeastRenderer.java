
package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.entity.BushBeastEntity;
import com.sakalti.sakaplus.client.model.BushBeastModel;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class BushBeastRenderer extends MobEntityRenderer<BushBeastEntity, BushBeastModel> {

    private static final Identifier TEXTURE = new Identifier(
        "sakaplus",
        "textures/entity/bush_beast.png"
    );

    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(
        new Identifier("sakaplus", "bush_beast"),
        "main"
    );

    public BushBeastRenderer(EntityRendererFactory.Context context) {
        super(
            context,
            new BushBeastModel(context.getPart(MODEL_LAYER)),
            0.5F
        );
    }

    @Override
    public Identifier getTexture(BushBeastEntity entity) {
        return TEXTURE;
    }
}
