package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.CryoSnakeEntity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class CryoSnakeModel extends SinglePartEntityModel<CryoSnakeEntity> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart[] segments = new ModelPart[8];

    private static final float GROUND_Y = 24.0F;
    private static final float HEAD_BASE_Y = 21.0F;
    private static final float HEAD_BASE_Z = -4.0F;

    public CryoSnakeModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");

        for (int i = 0; i < 8; i++) {
            segments[i] = root.getChild("segment_" + (i + 1));
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
                .cuboid(-4, -3, -8, 8, 6, 9)

                // Jaw
                .uv(0, 16)
                .cuboid(-3, 1, -8, 6, 2, 5)

                // Eyes
                .uv(32, 0)
                .cuboid(-4.2F, -2, -6, 1, 2, 2)
                .cuboid(3.2F, -2, -6, 1, 2, 2)

                // Horns
                .uv(40, 0)
                .cuboid(-3, -6, -3, 2, 4, 2)
                .cuboid(1, -6, -3, 2, 4, 2)

                // Nose crystal
                .uv(50, 0)
                .cuboid(-1, -2, -9, 2, 2, 2),

            ModelTransform.pivot(
                0,
                HEAD_BASE_Y,
                HEAD_BASE_Z
            )
        );

        // EIGHT BODY SEGMENTS
        for (int i = 0; i < 8; i++) {

            int size = Math.max(2, 4 - i / 3);

            ModelPartBuilder builder =
                ModelPartBuilder.create()
                    .uv(0, 24)
                    .cuboid(
                        -size,
                        -size,
                        -3,
                        size * 2,
                        size * 2,
                        6
                    );

            // Ice armor
            builder.uv(40, 24)
                .cuboid(
                    -size - 0.5F,
                    -size - 0.5F,
                    -1,
                    size * 2 + 1,
                    2,
                    3
                );

            // Back crystal
            builder.uv(64, 24)
                .cuboid(
                    -1,
                    -size - 3,
                    -1,
                    2,
                    3,
                    2
                );

            // Side crystals
            builder.uv(72, 24)
                .cuboid(
                    -size - 2,
                    -1,
                    0,
                    2,
                    2,
                    2
                )
                .cuboid(
                    size,
                    -1,
                    0,
                    2,
                    2,
                    2
                );

            // Position body directly on ground
            float groundPivotY = GROUND_Y - size;

            root.addChild(
                "segment_" + (i + 1),
                builder,
                ModelTransform.pivot(
                    0,
                    groundPivotY,
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
        CryoSnakeEntity entity,
        float limbAngle,
        float limbDistance,
        float animationProgress,
        float headYaw,
        float headPitch
    ) {

        // RESET HEAD
        head.pivotX = 0.0F;
        head.pivotY = HEAD_BASE_Y;
        head.pivotZ = HEAD_BASE_Z;

        head.roll = 0.0F;
        head.yaw = headYaw * ((float) Math.PI / 180F);
        head.pitch = headPitch * ((float) Math.PI / 180F);

        // IDLE ANIMATION
        float idle = MathHelper.sin(animationProgress * 0.08F);

        head.pivotY -= Math.max(0.0F, idle) * 0.15F;

        // SNAKE MOVEMENT
        float movement = Math.min(limbDistance, 1.0F);

        for (int i = 0; i < 8; i++) {

            int size = Math.max(2, 4 - i / 3);

            float baseY = GROUND_Y - size;

            float phase = i * 0.65F;

            float wave = MathHelper.sin(
                limbAngle * 0.55F - phase
            );

            float idleWave = MathHelper.sin(
                animationProgress * 0.045F - phase
            );

            // Reset segment position
            segments[i].pivotX = 0.0F;
            segments[i].pivotY = baseY;
            segments[i].pivotZ = 3 + i * 5;

            segments[i].pitch = 0.0F;
            segments[i].yaw = 0.0F;
            segments[i].roll = 0.0F;

            // Side-to-side movement
            segments[i].yaw =
                wave * 0.20F * movement
                + idleWave * 0.07F;

            // Vertical movement
            // Only move upward, never into the ground
            segments[i].pivotY -=
                Math.abs(wave) * 0.8F * movement;

            segments[i].pivotY -=
                Math.max(
                    0.0F,
                    MathHelper.sin(
                        animationProgress * 0.08F - phase
                    )
                ) * 0.2F;

            // Body bending
            segments[i].pitch =
                MathHelper.cos(
                    limbAngle * 0.55F - phase
                ) * 0.08F * movement;
        }

        // ATTACK: BODY RAM
        float swing = entity.getHandSwingProgress(0.0F);

        if (swing > 0.0F) {

            float attack = MathHelper.sin(
                swing * (float) Math.PI
            );

            float power = attack;

            // Head lunges forward
            head.pitch -= power * 0.75F;
            head.pivotZ -= power * 4.0F;
            head.pivotY -= power * 1.5F;

            // Front body sections follow
            for (int i = 0; i < 8; i++) {

                float influence = 1.0F - (i / 8.0F);

                segments[i].pitch +=
                    power * 0.35F * influence;

                segments[i].pivotY -=
                    power * 0.5F * influence;

                segments[i].yaw *=
                    1.0F - power * 0.5F;
            }
        }
    }
}
