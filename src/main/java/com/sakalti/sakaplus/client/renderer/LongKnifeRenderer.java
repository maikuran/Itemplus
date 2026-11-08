package com.sakalti.sakaplus.client.renderer;

import com.sakalti.sakaplus.client.model.LongKnifeModel;
import com.sakalti.sakaplus.entity.LongKnifeProjectile;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class LongKnifeRenderer extends EntityRenderer<LongKnifeProjectile> {
    
    public static final Identifier TEXTURE = new Identifier("sakaplus", "textures/entity/projectiles/long_knife.png");
    public static final EntityModelLayer LAYER = new EntityModelLayer(new Identifier("sakaplus", "long_knife"), "main");

    private final LongKnifeModel<LongKnifeProjectile> model;

    // 🛠️ 修正: クラス名（LongKnifeRenderer）とコンストラクタ名を一致させました
    public LongKnifeRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new LongKnifeModel<>(context.getPart(LAYER));
    }

    @Override
    public void render(LongKnifeProjectile entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        // 1. 1.19.2のProjectile標準：EntityのPitchとYawに合わせてマトリクスを回転
        float entityYaw = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw());
        float entityPitch = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());
        
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - entityYaw));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-entityPitch));

        // 2. モデル側のアニメーション処理（スピン・垂直落下時のドリル切り替え）を安全に呼び出し
        float animationProgress = (float)entity.age + tickDelta;
        this.model.setAngles(entity, 0.0F, 0.0F, animationProgress, 0.0F, 0.0F);

        // 3. テクスチャとバッファのセットアップ
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(entity)));
        
        // 4. レンダー実行
        this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(LongKnifeProjectile entity) {
        return TEXTURE;
    }
}
