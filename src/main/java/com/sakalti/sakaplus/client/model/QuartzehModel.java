package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.QuartzehEntity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class QuartzehModel extends SinglePartEntityModel<QuartzehEntity> {

    private final ModelPart root;

    private final ModelPart core;
    private final ModelPart crown;
    private final ModelPart plateL;
    private final ModelPart plateR;
    private final ModelPart spikeL;
    private final ModelPart spikeR;
    private final ModelPart lower;

    public QuartzehModel(ModelPart root) {

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
        // 中央の水晶コア
        // =====================================

        root.addChild(
                "core",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-7F, -13F, -5F, 14, 13, 10)
                        .uv(48, 0)
                        .cuboid(-6F, -16F, -4F, 12, 4, 8)
                        .uv(48, 12)
                        .cuboid(-5F, -1F, -4F, 10, 3, 8),
                ModelTransform.pivot(0F, 14F, 0F)
        );

        // =====================================
        // 上部の尖った水晶クラウン
        // =====================================

        root.addChild(
                "crown",
                ModelPartBuilder.create()
                        .uv(0, 26)
                        .cuboid(-5F, -8F, -5F, 10, 8, 10)
                        .uv(40, 26)
                        .cuboid(-4F, -14F, -4F, 8, 7, 8)
                        .uv(72, 26)
                        .cuboid(-2.5F, -20F, -2.5F, 5, 7, 5)
                        .uv(92, 26)
                        .cuboid(-1F, -24F, -1F, 2, 5, 2),
                ModelTransform.pivot(0F, 0F, 0F)
        );

        // =====================================
        // 左大型結晶プレート
        // =====================================

        root.addChild(
                "plate_l",
                ModelPartBuilder.create()
                        .uv(0, 48)
                        .cuboid(-12F, -12F, -2.5F, 9, 14, 5)
                        .uv(28, 48)
                        .cuboid(-14F, -9F, -2F, 5, 10, 4)
                        .uv(48, 48)
                        .cuboid(-11F, -17F, -1.5F, 4, 7, 3)
                        .uv(64, 48)
                        .cuboid(-13F, -20F, -1F, 3, 5, 2),
                ModelTransform.of(
                        0F, 8F, 0F,
                        0F, 0F, -0.38F
                )
        );

        // =====================================
        // 右大型結晶プレート
        // =====================================

        root.addChild(
                "plate_r",
                ModelPartBuilder.create()
                        .uv(80, 48)
                        .cuboid(3F, -12F, -2.5F, 9, 14, 5)
                        .uv(0, 72)
                        .cuboid(9F, -9F, -2F, 5, 10, 4)
                        .uv(28, 72)
                        .cuboid(7F, -17F, -1.5F, 4, 7, 3)
                        .uv(44, 72)
                        .cuboid(10F, -20F, -1F, 3, 5, 2),
                ModelTransform.of(
                        0F, 8F, 0F,
                        0F, 0F, 0.38F
                )
        );

        // =====================================
        // 左側の結晶突起
        // =====================================

        root.addChild(
                "spike_l",
                ModelPartBuilder.create()
                        .uv(60, 72)
                        .cuboid(-10F, -6F, -4F, 6, 8, 6)
                        .uv(84, 72)
                        .cuboid(-12F, -11F, -3F, 4, 7, 4)
                        .uv(100, 72)
                        .cuboid(-11F, -15F, -2F, 3, 5, 3),
                ModelTransform.pivot(0F, 10F, 0F)
        );

        // =====================================
        // 右側の結晶突起
        // =====================================

        root.addChild(
                "spike_r",
                ModelPartBuilder.create()
                        .uv(0, 92)
                        .cuboid(4F, -6F, -4F, 6, 8, 6)
                        .uv(24, 92)
                        .cuboid(8F, -11F, -3F, 4, 7, 4)
                        .uv(44, 92)
                        .cuboid(8F, -15F, -2F, 3, 5, 3),
                ModelTransform.pivot(0F, 10F, 0F)
        );

        // =====================================
        // 下部の細長い結晶
        // =====================================

        root.addChild(
                "lower",
                ModelPartBuilder.create()
                        .uv(60, 92)
                        .cuboid(-4F, 0F, -4F, 8, 5, 8)
                        .uv(92, 92)
                        .cuboid(-3F, 4F, -3F, 6, 7, 6)
                        .uv(0, 108)
                        .cuboid(-2F, 10F, -2F, 4, 6, 4)
                        .uv(20, 108)
                        .cuboid(-1F, 15F, -1F, 2, 5, 2),
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
            QuartzehEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {

        // 浮遊アニメーション
        float hover = MathHelper.sin(
                animationProgress * 0.095F
        );

        // 中央コア
        core.pitch = hover * 0.045F;
        core.roll = MathHelper.cos(
                animationProgress * 0.055F
        ) * 0.04F;

        // クラウン
        crown.pitch = hover * 0.055F;
        crown.roll = -hover * 0.035F;

        // 左右プレート
        plateL.roll = -0.38F
                + MathHelper.sin(animationProgress * 0.07F) * 0.06F;

        plateR.roll = 0.38F
                + MathHelper.sin(animationProgress * 0.07F + 1.5F) * 0.06F;

        // 側面突起
        spikeL.yaw = MathHelper.sin(
                animationProgress * 0.065F
        ) * 0.04F;

        spikeR.yaw = -spikeL.yaw;

        // 下部結晶
        lower.pitch = MathHelper.sin(
                animationProgress * 0.085F
        ) * 0.045F;
    }
}
