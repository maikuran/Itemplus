
package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.client.model.CryoHumanoidModel;
import com.sakalti.sakaplus.entity.CryomageEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class CryomageRenderer extends MobEntityRenderer<
        CryomageEntity,
        CryoHumanoidModel<CryomageEntity>
> {

    public static final EntityModelLayer MODEL_LAYER =
            new EntityModelLayer(
                    new Identifier("sakaplus", "cryomage"),
                    "main"
            );

    private static final Identifier TEXTURE =
            new Identifier(
                    "sakaplus",
                    "textures/entity/cryomage.png"
            );

    public CryomageRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new CryoHumanoidModel<>(
                        context.getPart(MODEL_LAYER)
                ),
                0.45F
        );
    }

    @Override
    public Identifier getTexture(CryomageEntity entity) {
        return TEXTURE;
    }
}
