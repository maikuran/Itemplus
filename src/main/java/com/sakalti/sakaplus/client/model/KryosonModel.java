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

    private static final float CORE_RADIUS = 20.91F;
    private static final float WING_GAP = 6.0F;
    private static final float WING_LENGTH = 14.0F;

    private static final float FLAP_SPEED = 0.65F;
    private static final float FLAP_POWER = 0.85F;

    public KryosonModel(ModelPart root) {

        this.root = root;

        this.core = root.getChild("core");

        this.wingL = root.getChild("wing_l");
        this.wingR = root.getChild("wing_r");

        for (int i = 0; i < 8; i++) {

            membranesL[i] =
                    wingL.getChild("membrane_l_" + i);

            membranesR[i] =
                    wingR.getChild("membrane_r_" + i);
        }
    }

    public static TexturedModelData getTexturedModelData() {

        ModelData modelData = new ModelData();

        ModelPartData root = modelData.getRoot();

        // =====================================
        // CORE
        // OCTAGONAL CORE
        // 8 SIDES
        // EACH SIDE 16x16x1
        // =====================================

        ModelPartData core = root.addChild(
                "core",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0F, 12F, 0F)
        );

        float radius = CORE_RADIUS;

        float sideWidth = 16F;
        float sideHeight = 16F;
        float sideDepth = 1F;

        for (int i = 0; i < 8; i++) {

            float angle =
                    (float) (Math.PI * 2.0 * i / 8.0);

            float x = MathHelper.sin(angle) * radius;
            float z = MathHelper.cos(angle) * radius;

            // 34x17 UV領域を重ならないように配置
            int uvX = (i % 3) * 40;
            int uvY = (i / 3) * 18;

            core.addChild(
                    "core_side_" + i,

                    ModelPartBuilder.create()
                            .uv(uvX, uvY)
                            .cuboid(
                                    -sideWidth / 2F,
                                    -sideHeight / 2F,
                                    -sideDepth / 2F,
                                    sideWidth,
                                    sideHeight,
                                    sideDepth
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
        // WING POSITION
        // CORE EDGE + 6 PIXELS
        // =====================================

        float wingPosition = CORE_RADIUS + WING_GAP;

        // =====================================
        // LEFT WING
        // GOLDEN METAL SPAR
        // =====================================

        ModelPartData wingL = root.addChild(
                "wing_l",

                ModelPartBuilder.create()
                        .uv(80, 112)
                        .cuboid(
                                -WING_LENGTH,
                                -0.5F,
                                -1F,
                                WING_LENGTH,
                                1F,
                                2F
                        ),

                ModelTransform.pivot(
                        -wingPosition,
                        11F,
                        0F
                )
        );

        // =====================================
        // LEFT MEMBRANES
        // 8 WHITE MEMBRANE PIECES
        // =====================================

        for (int i = 0; i < 8; i++) {

            float x = -0.5F - i * 1.7F;

            float angle =
                    (float) Math.toRadians(-10F - i * 4F);

            wingL.addChild(
                    "membrane_l_" + i,

                    ModelPartBuilder.create()
                            .uv(0, 54 + i * 9)
                            .cuboid(
                                    -8F,
                                    -0.5F,
                                    -0.5F,
                                    8F,
                                    1F,
                                    4F
                            ),

                    ModelTransform.of(
                            x,
                            0F,
                            -0.5F,
                            0F,
                            0F,
                            angle
                    )
            );
        }

        // =====================================
        // RIGHT WING
        // GOLDEN METAL SPAR
        // =====================================

        ModelPartData wingR = root.addChild(
                "wing_r",

                ModelPartBuilder.create()
                        .uv(80, 116)
                        .cuboid(
                                0F,
                                -0.5F,
                                -1F,
                                WING_LENGTH,
                                1F,
                                2F
                        ),

                ModelTransform.pivot(
                        wingPosition,
                        11F,
                        0F
                )
        );

        // =====================================
        // RIGHT MEMBRANES
        // 8 WHITE MEMBRANE PIECES
        // =====================================

        for (int i = 0; i < 8; i++) {

            float x = 0.5F + i * 1.7F;

            float angle =
                    (float) Math.toRadians(10F + i * 4F);

            wingR.addChild(
                    "membrane_r_" + i,

                    ModelPartBuilder.create()
                            .uv(40, 54 + i * 9)
                            .cuboid(
                                    0F,
                                    -0.5F,
                                    -0.5F,
                                    8F,
                                    1F,
                                    4F
                            ),

                    ModelTransform.of(
                            x,
                            0F,
                            -0.5F,
                            0F,
                            0F,
                            angle
                    )
            );
        }

        return TexturedModelData.of(
                modelData,
                128,
                128
        );
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

        // =====================================
        // CORE ROTATION
        // =====================================

        core.pitch = time * 0.12F;

        core.yaw =
                MathHelper.sin(time * 0.04F) * 0.12F;

        core.roll = 0F;

        core.pivotY =
                12F + MathHelper.sin(time * 0.08F) * 0.7F;

        // =====================================
        // WING FLAPPING
        // =====================================

        float flap = MathHelper.sin(
                time * FLAP_SPEED
        );

        wingL.roll =
                -0.25F - flap * FLAP_POWER;

        wingR.roll =
                0.25F + flap * FLAP_POWER;

        wingL.pitch =
                MathHelper.sin(time * FLAP_SPEED) * 0.15F;

        wingR.pitch =
                -MathHelper.sin(time * FLAP_SPEED) * 0.15F;

        // =====================================
        // MEMBRANE ANIMATION
        // 8 LEFT + 8 RIGHT
        // =====================================

        for (int i = 0; i < 8; i++) {

            float phase = i * 0.30F;

            float wave = MathHelper.sin(
                    time * FLAP_SPEED + phase
            );

            float power = 0.20F + i * 0.035F;

            float secondaryWave = MathHelper.cos(
                    time * FLAP_SPEED + phase
            );

            // LEFT MEMBRANE

            membranesL[i].roll =
                    (float) Math.toRadians(-10F - i * 4F)
                            + wave * power;

            membranesL[i].pitch =
                    secondaryWave * 0.12F;

            // RIGHT MEMBRANE

            membranesR[i].roll =
                    (float) Math.toRadians(10F + i * 4F)
                            - wave * power;

            membranesR[i].pitch =
                    -secondaryWave * 0.12F;
        }
    }
}
