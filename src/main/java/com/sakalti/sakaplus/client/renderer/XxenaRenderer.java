
package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.entity.XxenaEntity;
import com.sakalti.sakaplus.client.model.XxenaModel;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class XxenaRenderer extends MobEntityRenderer<XxenaEntity, XxenaModel> {

    private static final Identifier TEXTURE = new Identifier(
            "sakaplus",
            "textures/entity/xxena.png"
    );

    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(
            new Identifier("sakaplus", "xxena"),
            "main"
    );

    public XxenaRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new XxenaModel(context.getPart(MODEL_LAYER)),
                0.5F
        );
    }

    @Override
    public Identifier getTexture(XxenaEntity entity) {
        return TEXTURE;
    }
}
