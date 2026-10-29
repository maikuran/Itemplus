package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.XxenaEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class XxenaModel extends SinglePartEntityModel<XxenaEntity> {

    private final ModelPart root;
    private final ModelPart core;
    private final ModelPart[][] ribs = new ModelPart[6][3];

    private static final int TEXTURE_WIDTH = 512;
    private static final int TEXTURE_HEIGHT = 192;

    private static final int UV_CELL_WIDTH = 64;
    private static final int UV_CELL_HEIGHT = 32;

    // 【修正】地面への設置・高さをコントロールする基準Y座標
    // 通常のMobは 24.0F で足元が地面にピッタリつきます。
    // もしこれでも浮く場合は数値を大きく、埋まる場合は小さく（20.0Fなど）調整してください。
    private static final float BASE_Y = 24.0F;

    public XxenaModel(ModelPart root) {
        this.root = root;
        this.core = root.getChild("core");

        for (int i = 0; i < 6; i++) {
            ModelPart rib = root.getChild("rib_" + i);
            for (int j = 0; j < 3; j++) {
                ribs[i][j] = rib.getChild("segment_" + j);
            }
        }
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        int uvIndex = 0;

        // Purple Core
        root.addChild(
                "core",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4F, -4F, -4F, 8F, 8F, 8F),
                ModelTransform.NONE
        );

        uvIndex++;

        // Six Ribs
        for (int i = 0; i < 6; i++) {
            float angle = (float) (Math.PI * 2.0D * i / 6.0D);

            // 【修正】各リブの基準Y座標を 0F（上空）ではなく、BASE_Y に設定
            ModelPartData rib = root.addChild(
                    "rib_" + i,
                    ModelPartBuilder.create(),
                    ModelTransform.of(0F, BASE_Y, 0F, 0F, angle, 0F)
            );

            // Segment 1
            int uvX1 = (uvIndex % 8) * UV_CELL_WIDTH;
            int uvY1 = (uvIndex / 8) * UV_CELL_HEIGHT;
            uvIndex++;
            rib.addChild(
                    "segment_0",
                    ModelPartBuilder.create().uv(uvX1, uvY1).cuboid(-1F, -2.5F, -1F, 2F, 5F, 2F),
                    ModelTransform.of(4F, -2F, 0F, 0F, 0F, -0.35F)
            );

            // Segment 2
            int uvX2 = (uvIndex % 8) * UV_CELL_WIDTH;
            int uvY2 = (uvIndex / 8) * UV_CELL_HEIGHT;
            uvIndex++;
            rib.addChild(
                    "segment_1",
                    ModelPartBuilder.create().uv(uvX2, uvY2).cuboid(-1F, -2.5F, -1F, 2F, 5F, 2F),
                    ModelTransform.of(6F, -5F, 0F, 0F, 0F, 0.55F)
            );

            // Segment 3
            int uvX3 = (uvIndex % 8) * UV_CELL_WIDTH;
            int uvY3 = (uvIndex / 8) * UV_CELL_HEIGHT;
            uvIndex++;
            rib.addChild(
                    "segment_2",
                    ModelPartBuilder.create().uv(uvX3, uvY3).cuboid(-1F, -2.5F, -1F, 2F, 5F, 2F),
                    ModelTransform.of(8F, -8F, 0F, 0F, 0F, -0.45F)
            );
        }

        return TexturedModelData.of(modelData, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(
            XxenaEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        float time = animationProgress * 0.12F;

        // 【修正】コアの浮遊位置に BASE_Y を加算
        // これにより、地面の高さ（BASE_Y）をベースに上下にふわふわ動くようになります
        core.pivotY = BASE_Y + (float) Math.sin(time) * 0.5F;

        core.pitch = (float) Math.sin(time * 0.7F) * 0.08F;
        core.yaw = (float) Math.cos(time * 0.5F) * 0.08F;

        // Rib undulation
        for (int i = 0; i < 6; i++) {
            float phase = time + i * 1.05F;
            ModelPart rib = this.root.getChild("rib_" + i);

            // 【修正】アニメーション時に毎回リブの Y 座標が 0 にリセットされないよう BASE_Y を固定
            rib.pivotY = BASE_Y;

            rib.roll = (float) Math.sin(phase) * 0.08F;
            rib.pitch = (float) Math.cos(phase * 0.8F) * 0.06F;

            ribs[i][0].roll = -0.35F + (float) Math.sin(phase) * 0.16F;
            ribs[i][1].roll = 0.55F + (float) Math.sin(phase + 0.8F) * 0.20F;
            ribs[i][2].roll = -0.45F + (float) Math.sin(phase + 1.6F) * 0.18F;
        }
    }
}
