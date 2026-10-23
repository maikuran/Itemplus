package com.sakalti.sakaplus.client.render;

import com.sakalti.sakaplus.entity.BeamProjectileEntity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class BeamProjectileRenderer
        extends EntityRenderer<BeamProjectileEntity> {

    private static final float BEAM_WIDTH = 0.045F;
    private static final float BEAM_LENGTH = 1.5F;

    public BeamProjectileRenderer(EntityRendererFactory.Context context) {
        super(context);
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
        Vec3d velocity = entity.getVelocity();

        if (velocity.lengthSquared() < 1.0E-8D) {
            return;
        }

        Vec3d direction = velocity.normalize();

        float beamYaw = (float) Math.atan2(direction.z, direction.x);
        float beamPitch = (float) Math.asin(direction.y);

        int color = entity.getColor();
        float red = ((color >> 16) & 255) / 255.0F;
        float green = ((color >> 8) & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        matrices.push();

        matrices.multiply(
                RotationAxis.POSITIVE_Y.rotation(-beamYaw)
        );
        matrices.multiply(
                RotationAxis.POSITIVE_Z.rotation(beamPitch)
        );

        VertexConsumer consumer = vertexConsumers.getBuffer(
                RenderLayer.getLightning()
        );

        MatrixStack.Entry entry = matrices.peek();

        float halfWidth = BEAM_WIDTH / 2.0F;
        float length = BEAM_LENGTH;

        vertex(consumer, entry, -halfWidth, 0.0F, 0.0F,
                red, green, blue, 1.0F, light);
        vertex(consumer, entry, halfWidth, 0.0F, 0.0F,
                red, green, blue, 1.0F, light);
        vertex(consumer, entry, halfWidth, 0.0F, length,
                red, green, blue, 1.0F, light);
        vertex(consumer, entry, -halfWidth, 0.0F, length,
                red, green, blue, 1.0F, light);

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
            VertexConsumer consumer,
            MatrixStack.Entry entry,
            float x,
            float y,
            float z,
            float red,
            float green,
            float blue,
            float alpha,
            int light
    ) {
        consumer.vertex(entry.getPositionMatrix(), x, y, z)
                .color(red, green, blue, alpha)
                .texture(0.0F, 0.0F)
                .overlay(0)
                .light(light)
                .normal(entry.getNormalMatrix(), 0.0F, 1.0F, 0.0F)
                .next();
    }

    @Override
    public Identifier getTexture(BeamProjectileEntity entity) {
        return null;
    }
}
