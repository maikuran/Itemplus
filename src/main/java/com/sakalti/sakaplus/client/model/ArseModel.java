package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.ArseEntity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class ArseModel extends SinglePartEntityModel<ArseEntity> {

    private final ModelPart root;

    private final ModelPart core;
    private final ModelPart crown;
    private final ModelPart plateL;
    private final ModelPart plateR;
    private final ModelPart spikeL;
    private final ModelPart spikeR;
    private final ModelPart lower;

    public ArseModel(ModelPart root) {

        this.root = root;

        this.core = root.getChild("core");
        this.crown = root.getChild("crown");
        this.plateL = root.getChild("plate_l");
        this.plateR = root.getChild("plate_r");
        this.spikeL = root.getChild("spike_l");
        this.spikeR = root.getChild("spike_r");
        this.lower = root.getChild("lower");
    }

    public static TexturedModelData getTexturedModelData() {

        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // =====================================
        // 中央コア
        // =====================================

        root.addChild(
                "core",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(
                                -6F, -14F, -5F,
                                12, 14, 10
                        )
                        .uv(44, 0)
                        .cuboid(
                                -5F, -15F, -4F,
                                10, 3, 8
                        ),
                ModelTransform.pivot(0F, 14F, 0F)
        );

        // =====================================
        // 上部結晶クラウン
        // =====================================

        root.addChild(
                "crown",
                ModelPartBuilder.create()
                        .uv(0, 26)
                        .cuboid(
                                -4F, -8F, -4F,
                                8, 8, 8
                        )
                        .uv(32, 26)
                        .cuboid(
                                -3F, -13F, -3F,
                                6, 7, 6
                        )
                        .uv(56, 26)
                        .cuboid(
                                -1.5F, -18F, -1.5F,
                                3, 6, 3
                        ),
                ModelTransform.pivot(0F, 0F, 0F)
        );

        // =====================================
        // 左結晶プレート
        // =====================================

        root.addChild(
                "plate_l",
                ModelPartBuilder.create()
                        .uv(0, 48)
                        .cuboid(
                                -10F, -10F, -2F,
                                8, 12, 4
                        )
                        .uv(24, 48)
                        .cuboid(
                                -12F, -7F, -1.5F,
                                5, 8, 3
                        )
                        .uv(40, 48)
                        .cuboid(
                                -9F, -13F, -1F,
                                4, 6, 2
                        ),
                ModelTransform.of(
                        0F, 8F, 0F,
                        0F, 0F, -0.30F
                )
        );

        // =====================================
        // 右結晶プレート
        // =====================================

        root.addChild(
                "plate_r",
                ModelPartBuilder.create()
                        .uv(56, 48)
                        .cuboid(
                                2F, -10F, -2F,
                                8, 12, 4
                        )
                        .uv(80, 48)
                        .cuboid(
                                7F, -7F, -1.5F,
                                5, 8, 3
                        )
                        .uv(96, 48)
                        .cuboid(
                                5F, -13F, -1F,
                                4, 6, 2
                        ),
                ModelTransform.of(
                        0F, 8F, 0F,
                        0F, 0F, 0.30F
                )
        );

        // =====================================
        // 左側の結晶突起
        // =====================================

        root.addChild(
                "spike_l",
                ModelPartBuilder.create()
                        .uv(0, 68)
                        .cuboid(
                                -9F, -5F, -4F,
                                5, 7, 5
                        )
                        .uv(20, 68)
                        .cuboid(
                                -11F, -9F, -3F,
                                3, 6, 3
                        ),
                ModelTransform.pivot(0F, 10F, 0F)
        );

        // =====================================
        // 右側の結晶突起
        // =====================================

        root.addChild(
                "spike_r",
                ModelPartBuilder.create()
                        .uv(40, 68)
                        .cuboid(
                                4F, -5F, -4F,
                                5, 7, 5
                        )
                        .uv(60, 68)
                        .cuboid(
                                8F, -9F, -3F,
                                3, 6, 3
                        ),
                ModelTransform.pivot(0F, 10F, 0F)
        );

        // =====================================
        // 下部結晶
        // =====================================

        root.addChild(
                "lower",
                ModelPartBuilder.create()
                        .uv(80, 68)
                        .cuboid(
                                -4F, 0F, -4F,
                                8, 5, 8
                        )
                        .uv(104, 68)
                        .cuboid(
                                -3F, 4F, -3F,
                                6, 5, 6
                        )
                        .uv(0, 88)
                        .cuboid(
                                -2F, 8F, -2F,
                                4, 5, 4
                        ),
                ModelTransform.pivot(0F, 14F, 0F)
        );

        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(
            ArseEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {

        // 浮遊アニメーション
        float hover = MathHelper.sin(
                animationProgress * 0.08F
        );

        // 中央コアの微振動
        core.pitch = hover * 0.035F;
        core.roll = MathHelper.cos(
                animationProgress * 0.045F
        ) * 0.035F;

        // クラウンの揺れ
        crown.pitch = hover * 0.045F;
        crown.roll = -hover * 0.025F;

        // 左右結晶の揺れ
        plateL.roll = -0.30F
                + MathHelper.sin(animationProgress * 0.06F) * 0.045F;

        plateR.roll = 0.30F
                + MathHelper.sin(animationProgress * 0.06F + 1.5F) * 0.045F;

        // 側面突起
        spikeL.yaw = MathHelper.sin(
                animationProgress * 0.05F
        ) * 0.025F;

        spikeR.yaw = -spikeL.yaw;

        // 下部結晶
        lower.pitch = MathHelper.sin(
                animationProgress * 0.07F
        ) * 0.035F;
    }
}
