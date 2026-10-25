
package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.entity.ShyronEntity;
import com.sakalti.sakaplus.client.model.ShyronModel;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ShyronRenderer extends MobEntityRenderer<ShyronEntity, ShyronModel> {

    private static final Identifier TEXTURE = new Identifier(
            "sakaplus",
            "textures/entity/shyron.png"
    );

    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(
            new Identifier("sakaplus", "shyron"),
            "main"
    );

    public ShyronRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new ShyronModel(context.getPart(MODEL_LAYER)),
                0.5F
        );
    }

    @Override
    public Identifier getTexture(ShyronEntity entity) {
        return TEXTURE;
    }
}
