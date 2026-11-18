package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.PeridotehEntity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class PeridotehModel extends SinglePartEntityModel<PeridotehEntity> {

    private final ModelPart root;
    private final ModelPart core;
    private final ModelPart crystalRing;

    private static final int UV_CELL_WIDTH = 64;
    private static final int UV_CELL_HEIGHT = 32;
    private static final int TEXTURE_WIDTH = 512;
    private static final int TEXTURE_HEIGHT = 192;

    public PeridotehModel(ModelPart root) {
        this.root = root;
        this.core = root.getChild("core");
        this.crystalRing = root.getChild("crystal_ring");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        int uvIndex = 0;

        // 1. 中央のコア結晶（縦長の八角形風クリスタル：2つの直方体を45度ずらして重ねる）
        ModelPartData core = root.addChild(
                "core",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0F, 8F, 0F)
        );

        addCuboidPart(core, "core_main",
                0F, 0F, 0F,
                7F, 16F, 7F,
                0F, 0F, 0F,
                uvIndex++);

        addCuboidPart(core, "core_cross",
                0F, 0F, 0F,
                7F, 14F, 7F,
                0F, (float) Math.PI / 4F, 0F,
                uvIndex++);

        // コアの上下にある尖った先端パーツ
        addCuboidPart(core, "core_tip_top",
                0F, -10F, 0F,
                4F, 5F, 4F,
                0F, (float) Math.PI / 8F, 0F,
                uvIndex++);

        addCuboidPart(core, "core_tip_bottom",
                0F, 10F, 0F,
                4F, 5F, 4F,
                0F, (float) Math.PI / 8F, 0F,
                uvIndex++);

        // 2. 周囲を公転する結晶リング（6本の斜め結晶スパイク＋外側を漂う小さな破片）
        ModelPartData ring = root.addChild(
                "crystal_ring",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0F, 8F, 0F)
        );

        int spikeCount = 6;
        float radius = 10F;

        for (int i = 0; i < spikeCount; i++) {
            float angle = (float) (i * (Math.PI * 2.0 / spikeCount));
            float x = MathHelper.cos(angle) * radius;
            float z = MathHelper.sin(angle) * radius;
            // 交互に高さを少しずらして立体感を出す
            float y = (i % 2 == 0) ? -1.5F : 1.5F;

            // 外側に向かって斜めに傾いた結晶柱
            float tiltX = MathHelper.sin(angle) * 0.45F;
            float tiltZ = -MathHelper.cos(angle) * 0.45F;

            addCuboidPart(ring, "crystal_spike_" + i,
                    x, y, z,
                    3F, 10F, 3F,
                    tiltX, -angle, tiltZ,
                    uvIndex++);

            // 各結晶スパイクのさらに外側に浮遊する小さな鉱石の欠片
            float shardRadius = 14.5F;
            float sx = MathHelper.cos(angle + 0.25F) * shardRadius;
            float sz = MathHelper.sin(angle + 0.25F) * shardRadius;
            float sy = (i % 2 == 0) ? 3F : -3F;

            addCuboidPart(ring, "crystal_shard_" + i,
                    sx, sy, sz,
                    2F, 2F, 2F,
                    0.5F, angle, 0.5F,
                    uvIndex++);
        }

        return TexturedModelData.of(
                modelData,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );
    }

    // UVセルを重複なく割り当てつつ、サイズと回転を指定してパーツを追加
    private static void addCuboidPart(
            ModelPartData parent,
            String name,
            float x, float y, float z,
            float sizeX, float sizeY, float sizeZ,
            float pitch, float yaw, float roll,
            int uvIndex
    ) {
        int uvX = (uvIndex % 8) * UV_CELL_WIDTH;
        int uvY = (uvIndex / 8) * UV_CELL_HEIGHT;

        ModelPartBuilder builder = ModelPartBuilder.create()
                .uv(uvX, uvY)
                .cuboid(
                        -sizeX / 2F,
                        -sizeY / 2F,
                        -sizeZ / 2F,
                        sizeX,
                        sizeY,
                        sizeZ
                );

        parent.addChild(
                name,
                builder,
                ModelTransform.of(x, y, z, pitch, yaw, roll)
        );
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(
            PeridotehEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        // 全体をゆっくり上下に浮遊させる
        float bobbing = MathHelper.sin(animationProgress * 0.1F) * 1.2F;
        this.core.pivotY = 8F + bobbing;
        this.crystalRing.pivotY = 8F - bobbing * 0.6F;

        // コアと周囲の結晶リングを逆方向にゆっくり回転させる
        this.core.yaw = animationProgress * 0.03F;
        this.crystalRing.yaw = -animationProgress * 0.05F;
    }
}
