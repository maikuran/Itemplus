package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.entity.BeamProjectileEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class BeamProjectileRenderer extends EntityRenderer<BeamProjectileEntity> {

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

        if (velocity.lengthSquared()  16) & 255) / 255.0F;
        float green = ((color >> 8) & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        float alpha = 1.0F; // 必要に応じて透明度を変更

        matrices.push();

        // エンティティの進行方向へモデルを回転させる
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-beamYaw));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(beamPitch));

        // 稲妻用の発光レイヤーを取得 (テクスチャ不要・半透明発光)
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getLightning());
        MatrixStack.Entry entry = matrices.peek();

        float h = BEAM_WIDTH / 2.0F;
        float l = BEAM_LENGTH;

        // 4面の四角柱を描画して立体化する (カリング対策の頂点順序)
        
        // 1. 上面 (Top)
        vertex(consumer, entry, -h,  h, 0.0F, red, green, blue, alpha, light, 0.0F, 1.0F, 0.0F);
        vertex(consumer, entry, -h,  h,   l, red, green, blue, alpha, light, 0.0F, 1.0F, 0.0F);
        vertex(consumer, entry,  h,  h,   l, red, green, blue, alpha, light, 0.0F, 1.0F, 0.0F);
        vertex(consumer, entry,  h,  h, 0.0F, red, green, blue, alpha, light, 0.0F, 1.0F, 0.0F);

        // 2. 下面 (Bottom)
        vertex(consumer, entry,  h, -h, 0.0F, red, green, blue, alpha, light, 0.0F, -1.0F, 0.0F);
        vertex(consumer, entry,  h, -h,   l, red, green, blue, alpha, light, 0.0F, -1.0F, 0.0F);
        vertex(consumer, entry, -h, -h,   l, red, green, blue, alpha, light, 0.0F, -1.0F, 0.0F);
        vertex(consumer, entry, -h, -h, 0.0F, red, green, blue, alpha, light, 0.0F, -1.0F, 0.0F);

        // 3. 左面 (Left)
        vertex(consumer, entry, -h, -h, 0.0F, red, green, blue, alpha, light, -1.0F, 0.0F, 0.0F);
        vertex(consumer, entry, -h, -h,   l, red, green, blue, alpha, light, -1.0F, 0.0F, 0.0F);
        vertex(consumer, entry, -h,  h,   l, red, green, blue, alpha, light, -1.0F, 0.0F, 0.0F);
        vertex(consumer, entry, -h,  h, 0.0F, red, green, blue, alpha, light, -1.0F, 0.0F, 0.0F);

        // 4. 右面 (Right)
        vertex(consumer, entry,  h,  h, 0.0F, red, green, blue, alpha, light, 1.0F, 0.0F, 0.0F);
        vertex(consumer, entry,  h,  h,   l, red, green, blue, alpha, light, 1.0F, 0.0F, 0.0F);
        vertex(consumer, entry,  h, -h,   l, red, green, blue, alpha, light, 1.0F, 0.0F, 0.0F);
        vertex(consumer, entry,  h, -h, 0.0F, red, green, blue, alpha, light, 1.0F, 0.0F, 0.0F);

        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private static void vertex(
            VertexConsumer consumer,
            MatrixStack.Entry entry,
            float x, float y, float z,
            float red, float green, float blue, float alpha,
            int light,
            float nX, float nY, float nZ
    ) {
        consumer.vertex(entry.getPositionMatrix(), x, y, z)
                .color(red, green, blue, alpha)
                .texture(0.0F, 0.0F)
                .overlay(0)
                .light(light)
                .normal(entry.getNormalMatrix(), nX, nY, nZ)
                .next();
    }

    @Override
    public Identifier getTexture(BeamProjectileEntity entity) {
        return null;
    }
}
