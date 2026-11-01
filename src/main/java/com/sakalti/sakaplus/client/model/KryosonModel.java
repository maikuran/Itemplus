package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.KryosonEntity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class KryosonModel extends SinglePartEntityModel<KryosonEntity> {

    private final ModelPart root;
    private final ModelPart core;

    private final ModelPart wingL;
    private final ModelPart wingR;

    private final ModelPart[] membranesL = new ModelPart[4];
    private final ModelPart[] membranesR = new ModelPart[4];

    public KryosonModel(ModelPart root) {

        this.root = root;

        this.core = root.getChild("core");

        this.wingL = root.getChild("wing_l");
        this.wingR = root.getChild("wing_r");

        for (int i = 0; i < 4; i++) {
            membranesL[i] = wingL.getChild("membrane_l_" + i);
            membranesR[i] = wingR.getChild("membrane_r_" + i);
        }
    }

    public static TexturedModelData getTexturedModelData() {

        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // =====================================
        // CORE
        // 八角形コア
        // 側面8枚
        // 高さ4px
        // =====================================

        ModelPartData core = root.addChild(
                "core",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0F, 12F, 0F)
        );

        float radius = 3.0F;
        float sideWidth = 2.45F;

        for (int i = 0; i < 8; i++) {

            float angle = (float) (Math.PI * 2.0 * i / 8.0);

            float x = MathHelper.sin(angle) * radius;
            float z = MathHelper.cos(angle) * radius;

            core.addChild(
                    "core_side_" + i,

                    ModelPartBuilder.create()
                            .uv(0, 0)
                            .cuboid(
                                    -sideWidth / 2F,
                                    -2F,
                                    -0.5F,
                                    sideWidth,
                                    4F,
                                    1F
                            ),

                    ModelTransform.of(
                            x,
                            0F,
                            z,
                            0F,
                            angle,
                            0F
                    )
            );
        }

        // =====================================
        // LEFT WING
        // コア外端から6px離す
        // =====================================

        ModelPartData wingL = root.addChild(
                "wing_l",

                ModelPartBuilder.create()
                        .uv(0, 20)
                        .cuboid(
                                -7F,
                                -0.5F,
                                -1F,
                                7F,
                                1F,
                                2F
                        ),

                ModelTransform.pivot(-10.225F, 11F, 0F)
        );

        // 左翼の4枚の膜
        for (int i = 0; i < 4; i++) {

            float x = -1.5F - i * 1.3F;

            wingL.addChild(
                    "membrane_l_" + i,

                    ModelPartBuilder.create()
                            .uv(0, 24 + i * 8)
                            .cuboid(
                                    -5F,
                                    -0.5F,
                                    -0.5F,
                                    5F,
                                    1F,
                                    4F
                            ),

                    ModelTransform.of(
                            x,
                            0F,
                            -0.5F,
                            0F,
                            0F,
                            (float) Math.toRadians(-15 - i * 8)
                    )
            );
        }

        // =====================================
        // RIGHT WING
        // コア外端から6px離す
        // =====================================

        ModelPartData wingR = root.addChild(
                "wing_r",

                ModelPartBuilder.create()
                        .uv(0, 20)
                        .cuboid(
                                0F,
                                -0.5F,
                                -1F,
                                7F,
                                1F,
                                2F
                        ),

                ModelTransform.pivot(10.225F, 11F, 0F)
        );

        // 右翼の4枚の膜
        for (int i = 0; i < 4; i++) {

            float x = 1.5F + i * 1.3F;

            wingR.addChild(
                    "membrane_r_" + i,

                    ModelPartBuilder.create()
                            .uv(0, 24 + i * 8)
                            .cuboid(
                                    0F,
                                    -0.5F,
                                    -0.5F,
                                    5F,
                                    1F,
                                    4F
                            ),

                    ModelTransform.of(
                            x,
                            0F,
                            -0.5F,
                            0F,
                            0F,
                            (float) Math.toRadians(15 + i * 8)
                    )
            );
        }

        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    // =====================================
    // ANIMATION
    // =====================================

    @Override
    public void setAngles(
            KryosonEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {

        float time = animationProgress;

        // -----------------------------
        // CORE ROTATION
        // -----------------------------

        core.pitch = time * 0.12F;

        core.yaw = MathHelper.sin(time * 0.04F) * 0.12F;

        core.roll = 0F;

        core.pivotY = 12F
                + MathHelper.sin(time * 0.08F) * 0.7F;

        // -----------------------------
        // WING FLAPPING
        // -----------------------------

        float flapSpeed = 0.65F;
        float flapPower = 0.85F;

        float flap = MathHelper.sin(time * flapSpeed);

        wingL.roll = -0.25F - flap * flapPower;
        wingR.roll = 0.25F + flap * flapPower;

        wingL.pitch = MathHelper.sin(time * flapSpeed) * 0.15F;

        wingR.pitch = -MathHelper.sin(time * flapSpeed) * 0.15F;

        // -----------------------------
        // MEMBRANE ANIMATION
        // -----------------------------

        for (int i = 0; i < 4; i++) {

            float phase = i * 0.45F;

            float membraneFlap = MathHelper.sin(
                    time * flapSpeed + phase
            );

            float power = 0.25F + i * 0.08F;

            // LEFT MEMBRANE

            membranesL[i].roll =
                    (float) Math.toRadians(-15 - i * 8)
                            + membraneFlap * power;

            membranesL[i].pitch =
                    MathHelper.cos(time * flapSpeed + phase) * 0.12F;

            // RIGHT MEMBRANE

            membranesR[i].roll =
                    (float) Math.toRadians(15 + i * 8)
                            - membraneFlap * power;

            membranesR[i].pitch =
                    -MathHelper.cos(time * flapSpeed + phase) * 0.12F;
        }
    }
}
