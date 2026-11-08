package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.LongKnifeProjectile;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class LongKnifeModel<T extends LongKnifeProjectile> extends SinglePartEntityModel<T> {

    private final ModelPart root;
    private final ModelPart knife;

    public LongKnifeModel(ModelPart root) {
        this.root = root;
        this.knife = root.getChild("knife");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        // MAIN KNIFE ROOT (Collision 0.25f x 1.0f に完全同期)
        ModelPartData knife = root.addChild(
                "knife",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0F, 0F, 0F)
        );

        // SMOOTH BLADE (極薄エッジ: 厚み0.25f / 長さ1.0f対応)
        knife.addChild(
                "blade",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-2.0F, -12F, -1.0F, 4.0F, 12F, 2.0F)
                        .uv(12, 0)
                        .cuboid(-0.5F, -16F, -0.5F, 1.0F, 4F, 1.0F),
                ModelTransform.pivot(0F, 0F, 0F)
        );

        // HILT & GUARD (柄・衝突判定の下部ベース)
        knife.addChild(
                "hilt",
                ModelPartBuilder.create()
                        .uv(0, 16)
                        .cuboid(-3.0F, 0F, -1.5F, 6.0F, 1.0F, 3.0F)
                        .uv(18, 16)
                        .cuboid(-1.0F, 1.0F, -1.0F, 2.0F, 3.0F, 2.0F),
                ModelTransform.pivot(0F, 0F, 0F)
        );

        return TexturedModelData.of(data, 32, 32);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(
            T entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        // 1. 各軸の回転をリセット
        this.knife.pitch = 0F;
        this.knife.yaw = 0F;
        this.knife.roll = 0F;
        this.knife.pivotY = 0F;

        // 2. LongKnifeProjectileのモーション・状態をリアルタイム参照
        Vec3d velocity = entity.getVelocity();
        
        // XとZの速度がほぼ0、かつYがマイナス（setFallVelocityによる垂直落下状態）
        boolean isFallingStraight = Math.abs(velocity.x) < 0.001D && Math.abs(velocity.z) < 0.001D && velocity.y < 0;

        if (isFallingStraight) {
            // 【ボス演出：ギロチン落下】
            // 真下に突き刺さるように、ピッチを180度（下向き）に固定し、高速なドリル回転（Yaw回転）をかける
            this.knife.pitch = (float)Math.PI; 
            this.knife.yaw = animationProgress * 0.8F; 
        } else {
            // 【通常投擲・浮遊状態】
            // 進行方向（Pitch / Yaw）へ傾け、ロール軸（Z軸）で滑らかに前方スピン
            this.knife.pitch = headPitch * ((float)Math.PI / 180F);
            this.knife.yaw = headYaw * ((float)Math.PI / 180F);
            
            // 通常スピン
            float spinSpeed = animationProgress * 0.6F;
            this.knife.roll += spinSpeed;

            // ボス演出用の滑らかな浮遊（0.25f x 1.0f の Collision範囲から逸脱しないよう制限）
            float hover = MathHelper.sin(animationProgress * 0.12F) * 0.1F;
            this.knife.pivotY = hover * 2.0F;
        }
    }
}
