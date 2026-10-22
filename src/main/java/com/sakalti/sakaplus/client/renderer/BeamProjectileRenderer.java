
package com.sakalti.sakaplus.client;

import com.sakalti.sakaplus.entity.BeamProjectileEntity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

public class BeamProjectileRenderer
        extends EntityRenderer<BeamProjectileEntity> {

    private static final Identifier TEXTURE =
            new Identifier(
                    "minecraft",
                    "textures/entity/experience_orb.png"
            );

    public BeamProjectileRenderer(
            EntityRendererFactory.Context context
    ) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(
            BeamProjectileEntity entity,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light
    ) {
        matrices.push();

        matrices.multiply(this.dispatcher.getRotation());
        matrices.scale(0.22F, 0.22F, 0.22F);

        MatrixStack.Entry entry = matrices.peek();
        Matrix4f positionMatrix = entry.getPositionMatrix();
        Matrix3f normalMatrix = entry.getNormalMatrix();

        VertexConsumer vertices = vertexConsumers.getBuffer(
                RenderLayer.getEntityTranslucent(TEXTURE)
        );

        int color = entity.getColor();
        int red = (color >> 16) & 255;
        int green = (color >> 8) & 255;
        int blue = color & 255;

        vertex(vertices, positionMatrix, normalMatrix,
                -0.5F, -0.5F, 0.0F, 0.0F, 1.0F,
                red, green, blue, light);

        vertex(vertices, positionMatrix, normalMatrix,
                 0.5F, -0.5F, 0.0F, 1.0F, 1.0F,
                red, green, blue, light);

        vertex(vertices, positionMatrix, normalMatrix,
                 0.5F,  0.5F, 0.0F, 1.0F, 0.0F,
                red, green, blue, light);

        vertex(vertices, positionMatrix, normalMatrix,
                -0.5F,  0.5F, 0.0F, 0.0F, 0.0F,
                red, green, blue, light);

        matrices.pop();

        super.render(
                entity,
                yaw,
                tickDelta,
                matrices,
                vertexConsumers,
                light
        );
    }

    private static void vertex(
            VertexConsumer vertices,
            Matrix4f positionMatrix,
            Matrix3f normalMatrix,
            float x,
            float y,
            float z,
            float u,
            float v,
            int red,
            int green,
            int blue,
            int light
    ) {
        vertices.vertex(positionMatrix, x, y, z)
                .color(red, green, blue, 255)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0x00F000F0)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .next();
    }

    @Override
    public Identifier getTexture(BeamProjectileEntity entity) {
        return TEXTURE;
    }
}
