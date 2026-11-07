package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.SavannuEntity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class SavannuModel extends SinglePartEntityModel<SavannuEntity> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart[] segments = new ModelPart[8];
    private final ModelPart[][] legs = new ModelPart[8][2];

    private static final float GROUND_Y = 24.0F;
    private static final float HEAD_Y = 19.0F;
    private static final float HEAD_Z = -5.0F;

    public SavannuModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");

        for (int i = 0; i < 8; i++) {
            segments[i] = root.getChild("segment_" + (i + 1));
            legs[i][0] = root.getChild("leg_left_" + (i + 1));
            legs[i][1] = root.getChild("leg_right_" + (i + 1));
        }
    }

    public static TexturedModelData getTexturedModelData() {

        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        // HEAD
        root.addChild(
            "head",
            ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-5, -5, -8, 10, 9, 9)

                // Lower jaw
                .uv(0, 18)
                .cuboid(-4, 2, -8, 8, 3, 6)

                // Eyes
                .uv(40, 0)
                .cuboid(-5.5F, -4, -5, 2, 2, 2)
                .cuboid(3.5F, -4, -5, 2, 2, 2)

                // Antennae
                .uv(48, 0)
                .cuboid(-3, -8, -4, 2, 4, 2)
                .cuboid(1, -8, -4, 2, 4, 2),

            ModelTransform.pivot(0, HEAD_Y, HEAD_Z)
        );

        // BODY SEGMENTS
        for (int i = 0; i < 8; i++) {

            int size = Math.max(3, 5 - i / 3);

            ModelPartBuilder body = ModelPartBuilder.create()

                // Main fleshy body
                .uv(0, 28)
                .cuboid(
                    -size,
                    -size,
                    -4,
                    size * 2,
                    size * 2,
                    8
                )

                // Upper armor plate
                .uv(40, 28)
                .cuboid(
                    -size + 0.5F,
                    -size - 1,
                    -3,
                    size * 2 - 1,
                    2,
                    6
                )

                // Side armor
                .uv(40, 40)
                .cuboid(
                    -size - 0.5F,
                    -2,
                    -2,
                    2,
                    4,
                    5
                )
                .cuboid(
                    size - 1.5F,
                    -2,
                    -2,
                    2,
                    4,
                    5
                );

            float pivotY = GROUND_Y - size;

            root.addChild(
                "segment_" + (i + 1),
                body,
                ModelTransform.pivot(
                    0,
                    pivotY,
                    3 + i * 5
                )
            );

            // LEFT LEG
            root.addChild(
                "leg_left_" + (i + 1),
                ModelPartBuilder.create()
                    .uv(72, 28)
                    .cuboid(-1, 0, -1, 2, 4, 3),
                ModelTransform.pivot(
                    -size + 1,
                    pivotY + 1,
                    3 + i * 5
                )
            );

            // RIGHT LEG
            root.addChild(
                "leg_right_" + (i + 1),
                ModelPartBuilder.create()
                    .uv(72, 28)
                    .cuboid(-1, 0, -1, 2, 4, 3),
                ModelTransform.pivot(
                    size - 1,
                    pivotY + 1,
                    3 + i * 5
                )
            );
        }

        return TexturedModelData.of(data, 128, 128);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(
        SavannuEntity entity,
        float limbAngle,
        float limbDistance,
        float animationProgress,
        float headYaw,
        float headPitch
    ) {

        // HEAD
        head.pivotX = 0;
        head.pivotY = HEAD_Y;
        head.pivotZ = HEAD_Z;

        head.yaw = headYaw * ((float) Math.PI / 180F);
        head.pitch = headPitch * ((float) Math.PI / 180F);
        head.roll = 0;

        float movement = Math.min(limbDistance, 1.0F);

        // IDLE
        float idle = MathHelper.sin(animationProgress * 0.08F);

        head.pivotY -= Math.max(0, idle) * 0.25F;

        // CATERPILLAR MOVEMENT
        for (int i = 0; i < 8; i++) {

            int size = Math.max(3, 5 - i / 3);

            float phase = i * 0.65F;

            float wave = MathHelper.sin(
                limbAngle * 0.65F - phase
            );

            float idleWave = MathHelper.sin(
                animationProgress * 0.045F - phase
            );

            float baseY = GROUND_Y - size;

            segments[i].pivotX = 0;
            segments[i].pivotY = baseY;
            segments[i].pivotZ = 3 + i * 5;

            segments[i].yaw =
                wave * 0.12F * movement
                + idleWave * 0.04F;

            // Raise body segments
            segments[i].pivotY -=
                Math.max(0, wave) * 1.5F * movement;

            // Body bending
            segments[i].pitch =
                MathHelper.cos(limbAngle * 0.65F - phase)
                * 0.18F * movement;

            // Legs
            for (int side = 0; side < 2; side++) {

                ModelPart leg = legs[i][side];

                leg.pitch =
                    MathHelper.sin(
                        limbAngle * 0.9F - phase
                        + side * (float) Math.PI
                    ) * 0.35F * movement;

                leg.roll =
                    (side == 0 ? -1 : 1) * 0.12F;
            }
        }

        // ATTACK: HEAD RAM
        float swing = entity.getHandSwingProgress(0.0F);

        if (swing > 0) {

            float attack = MathHelper.sin(
                swing * (float) Math.PI
            );

            head.pitch -= attack * 0.65F;
            head.pivotZ -= attack * 4.0F;
            head.pivotY -= attack * 1.0F;

            for (int i = 0; i < 8; i++) {

                float influence = 1.0F - i / 8.0F;

                segments[i].pitch +=
                    attack * 0.25F * influence;

                segments[i].pivotY -=
                    attack * 0.5F * influence;
            }
        }
    }
}
