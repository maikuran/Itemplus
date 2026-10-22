package com.sakalti.sakaplus.client.render;

import com.sakalti.sakaplus.SakaPlus;
import com.sakalti.sakaplus.client.model.KruziAntModel;
import com.sakalti.sakaplus.entity.KruziAntEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public final class KruziAntRenderer extends EntityRenderer<KruziAntEntity> {
    private static final Identifier TEXTURE = new Identifier(SakaPlus.MOD_ID, "textures/entity/kruzi_ant.png");
    private final KruziAntModel model;

    public KruziAntRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new KruziAntModel(context.getPart(KruziAntModel.LAYER));
        this.shadowRadius = 0.35F;
    }

    @Override
    public Identifier getTexture(KruziAntEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(KruziAntEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(0.0D, 0.08D, 0.0D);
        float hover = MathHelper.sin((entity.age + tickDelta) * 0.12F) * 0.035F;
        matrices.translate(0.0D, hover, 0.0D);
        VertexConsumer vertices = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(getTexture(entity)));
        model.setAngles(entity, 0.0F, 0.0F, entity.age + tickDelta, 0.0F, 0.0F);
        model.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
