
package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.entity.CryofreezerEntity;
import com.sakalti.sakaplus.client.model.CryofreezerModel;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class CryofreezerRenderer extends MobEntityRenderer<CryofreezerEntity, CryofreezerModel> {

    private static final Identifier TEXTURE = new Identifier(
            "sakaplus",
            "textures/entity/cryofreezer.png"
    );

    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(
            new Identifier("sakaplus", "cryofreezer"),
            "main"
    );

    public CryofreezerRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new CryofreezerModel(context.getPart(MODEL_LAYER)),
                0.5F
        );
    }

    @Override
    public Identifier getTexture(CryofreezerEntity entity) {
        return TEXTURE;
    }
}
