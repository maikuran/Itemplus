
package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.client.model.CryoHumanoidModel;
import com.sakalti.sakaplus.entity.CryowarriorEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class CryowarriorRenderer extends MobEntityRenderer<
        CryowarriorEntity,
        CryoHumanoidModel<CryowarriorEntity>
> {

    public static final EntityModelLayer MODEL_LAYER =
            new EntityModelLayer(
                    new Identifier("sakaplus", "cryowarrior"),
                    "main"
            );

    private static final Identifier TEXTURE =
            new Identifier(
                    "sakaplus",
                    "textures/entity/cryowarrior.png"
            );

    public CryowarriorRenderer(EntityRendererFactory.Context context) {
        super(
                context,
                new CryoHumanoidModel<>(
                        context.getPart(MODEL_LAYER)
                ),
                0.55F
        );
    }

    @Override
    public Identifier getTexture(CryowarriorEntity entity) {
        return TEXTURE;
    }
}
