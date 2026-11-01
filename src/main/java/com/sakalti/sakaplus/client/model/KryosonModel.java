
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

    private final ModelPart[] membranesL = new ModelPart[8];
    private final ModelPart[] membranesR = new ModelPart[8];

    public KryosonModel(ModelPart root) {

        this.root = root;

        this.core = root.getChild("core");

        this.wingL = root.getChild("wing_l");
        this.wingR = root.getChild("wing_r");

        for (int i = 0; i < 8; i++) {
            membranesL[i] = wingL.getChild("membrane_l_" + i);
            membranesR[i] = wingR.getChild("membrane_r_" + i);
        }
    }

    public static TexturedModelData getTexturedModelData() {

        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // =====================================
        // CORE
        // 八角柱・高さ4px
        // =====================================

        ModelPartData core = root.addChild(
                "core",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0F, 12F, 0F)
        );

        float radius = 3.0F;
        float sideWidth = 2.45F;

        // 八角柱の8側面
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

        // 上面
        core.addChild(
                "core_top",
                ModelPartBuilder.create()
                        .uv(0, 6)
                        .cuboid(-2.5F, -0.5F, -2.5F, 5F, 1F, 5F),
                ModelTransform.pivot(0F, -2F, 0F)
        );

        // 下面
        core.addChild(
                "core_bottom",
                ModelPartBuilder.create()
                        .uv(12, 6)
                        .cuboid(-2.5F, -0.5F, -2.5F, 5F, 1F, 5F),
                ModelTransform.pivot(0F, 2F, 0F)
        );

        // =====================================
        // LEFT WING
        // コアから離して配置
        // =====================================

        ModelPartData wingL = root.addChild(
                "wing_l",

                ModelPartBuilder.create()
                        .uv(0, 20)
                        .cuboid(0F, -0.5F, -1F, 9F, 1F, 2F),

                ModelTransform.pivot(-6F, 11F, 0F)
        );

        // 左翼の羽膜8枚
        for (int i = 0; i < 8; i++) {

            float angle = (float) (Math.PI * 2.0 * i / 8.0);

            float x = 2.0F + i * 0.85F;

            wingL.addChild(
                    "membrane_l_" + i,

                    ModelPartBuilder.create()
                            .uv(0, 24 + (i % 8) * 8)
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
                            0F,
                            angle * 0.15F,
                            0F,
                            (float) Math.toRadians(-15 - i * 5)
                    )
            );
        }

        // =====================================
        // RIGHT WING
        // =====================================

        ModelPartData wingR = root.addChild(
                "wing_r",

                ModelPartBuilder.create()
                        .uv(0, 20)
                        .cuboid(-9F, -0.5F, -1F, 9F, 1F, 2F),

                ModelTransform.pivot(6F, 11F, 0F)
        );

        // 右翼の羽膜8枚
        for (int i = 0; i < 8; i++) {

            float angle = (float) (Math.PI * 2.0 * i / 8.0);

            float x = -2.0F - i * 0.85F;

            wingR.addChild(
                    "membrane_r_" + i,

                    ModelPartBuilder.create()
                            .uv(0, 24 + (i % 8) * 8)
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
                            0F,
                            -angle * 0.15F,
                            0F,
                            (float) Math.toRadians(15 + i * 5)
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

        // コア：縦軸回転
        core.pitch = time * 0.12F;
        core.yaw = time * 0.08F;
        core.roll = 0F;

        // 浮遊
        core.pivotY = 12F
                + MathHelper.sin(time * 0.08F) * 0.7F;

        // 羽ばたき
        float flapSpeed = 0.65F;
        float flapPower = 0.85F;

        float flap = MathHelper.sin(time * flapSpeed);

        wingL.roll = -0.25F - flap * flapPower;
        wingR.roll = 0.25F + flap * flapPower;

        wingL.pitch = MathHelper.sin(time * flapSpeed) * 0.15F;
        wingR.pitch = -MathHelper.sin(time * flapSpeed) * 0.15F;

        // 羽膜：各方向に独立した動き
        for (int i = 0; i < 8; i++) {

            float phase = i * 0.38F;

            float membraneFlap = MathHelper.sin(
                    time * flapSpeed + phase
            );

            float power = 0.20F + i * 0.045F;

            // 左
            membranesL[i].roll =
                    (float) Math.toRadians(-15 - i * 5)
                            + membraneFlap * power;

            membranesL[i].pitch =
                    MathHelper.cos(time * flapSpeed + phase) * 0.15F;

            membranesL[i].yaw =
                    MathHelper.sin(time * 0.4F + phase) * 0.12F;

            // 右
            membranesR[i].roll =
                    (float) Math.toRadians(15 + i * 5)
                            - membraneFlap * power;

            membranesR[i].pitch =
                    -MathHelper.cos(time * flapSpeed + phase) * 0.15F;

            membranesR[i].yaw =
                    -MathHelper.sin(time * 0.4F + phase) * 0.12F;
        }
    }
}
