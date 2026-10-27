package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.CryobeastEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class CryobeastModel extends SinglePartEntityModel<CryobeastEntity> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public CryobeastModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        // HEAD
        ModelPartData head = root.addChild("head",
            ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-4, -8, -4, 8, 8, 8)

                // Horns
                .uv(34, 0)
                .cuboid(-5, -10, -1, 2, 4, 2)
                .uv(42, 0)
                .cuboid(3, -10, -1, 2, 4, 2)

                // Jaw
                .uv(50, 0)
                .cuboid(-4, -2, -5, 8, 3, 5)

                // Cheek crystals
                .uv(78, 0)
                .cuboid(-6, -5, -2, 2, 4, 3)
                .uv(88, 0)
                .cuboid(4, -5, -2, 2, 4, 3),

            ModelTransform.pivot(0, 0, 0)
        );

        head.addChild("brow_crystal",
            ModelPartBuilder.create()
                .uv(100, 0)
                .cuboid(-2, -9, -4.5F, 4, 2, 1),
            ModelTransform.NONE
        );

        // BODY
        ModelPartData body = root.addChild("body",
            ModelPartBuilder.create()
                .uv(0, 20)
                .cuboid(-5, 0, -3, 10, 13, 6)

                // Upper armor
                .uv(36, 20)
                .cuboid(-6, 1, -4, 12, 6, 7)

                // Lower armor
                .uv(76, 20)
                .cuboid(-5, 7, -4, 10, 5, 7)

                // Ice core
                .uv(112, 20)
                .cuboid(-2, 3, -4.8F, 4, 4, 1),

            ModelTransform.pivot(0, 0, 0)
        );

        // Back spikes
        body.addChild("back_spike_l",
            ModelPartBuilder.create()
                .uv(0, 44)
                .cuboid(-1, -5, 0, 2, 6, 2),
            ModelTransform.of(4, 3, 2, -0.35F, 0, -0.35F)
        );

        body.addChild("back_spike_r",
            ModelPartBuilder.create()
                .uv(8, 44)
                .cuboid(-1, -5, 0, 2, 6, 2),
            ModelTransform.of(-4, 3, 2, -0.35F, 0, 0.35F)
        );

        // RIGHT ARM
        root.addChild("right_arm",
            ModelPartBuilder.create()
                .uv(18, 44)
                .cuboid(-3, -2, -3, 6, 13, 6)

                // Shoulder armor
                .uv(44, 44)
                .cuboid(-4, -3, -4, 7, 5, 7)

                // Ice claw
                .uv(72, 44)
                .cuboid(-2, 10, -4, 2, 4, 2),

            ModelTransform.pivot(-8, 2, 0)
        );

        // LEFT ARM
        root.addChild("left_arm",
            ModelPartBuilder.create()
                .uv(0, 70)
                .cuboid(-3, -2, -3, 6, 13, 6)

                // Shoulder armor
                .uv(28, 70)
                .cuboid(-3, -3, -4, 7, 5, 7)

                // Ice claw
                .uv(56, 70)
                .cuboid(0, 10, -4, 2, 4, 2),

            ModelTransform.pivot(8, 2, 0)
        );

        // RIGHT LEG
        root.addChild("right_leg",
            ModelPartBuilder.create()
                .uv(84, 44)
                .cuboid(-3, 0, -3, 6, 12, 6)

                // Greave
                .uv(84, 70)
                .cuboid(-3.5F, 5, -3.5F, 7, 6, 7),

            ModelTransform.pivot(-3, 13, 0)
        );

        // LEFT LEG
        root.addChild("left_leg",
            ModelPartBuilder.create()
                .uv(0, 96)
                .cuboid(-3, 0, -3, 6, 12, 6)

                // Greave
                .uv(28, 96)
                .cuboid(-3.5F, 5, -3.5F, 7, 6, 7),

            ModelTransform.pivot(3, 13, 0)
        );

        return TexturedModelData.of(data, 128, 128);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(
        CryobeastEntity entity,
        float limbAngle,
        float limbDistance,
        float animationProgress,
        float headYaw,
        float headPitch
    ) {

        // Head rotation
        head.yaw = headYaw * ((float) Math.PI / 180F);
        head.pitch = headPitch * ((float) Math.PI / 180F);

        // Walking animation
        float walk = MathHelper.cos(
            limbAngle * 0.6662F
        ) * 1.15F * limbDistance;

        rightArm.pitch =
            MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI)
            * 1.25F * limbDistance;

        leftArm.pitch =
            MathHelper.cos(limbAngle * 0.6662F)
            * 1.25F * limbDistance;

        rightLeg.pitch = walk;
        leftLeg.pitch = -walk;

        // Idle animation
        float idle = MathHelper.sin(
            animationProgress * 0.08F
        ) * 0.035F;

        body.pitch = idle;
        head.pivotY = idle * 3.0F;

        rightArm.roll = -0.06F + idle;
        leftArm.roll = 0.06F - idle;

        // Attack animation
        float swing = entity.getHandSwingProgress(0.0F);

        if (swing > 0.0F) {
            float s = MathHelper.sin(
                swing * (float) Math.PI
            );

            body.yaw =
                MathHelper.sin(
                    MathHelper.sqrt(swing) * ((float) Math.PI * 2F)
                ) * 0.12F;

            rightArm.pitch -= s * 1.75F;
            leftArm.pitch -= s * 1.75F;

            rightArm.roll -= s * 0.18F;
            leftArm.roll += s * 0.18F;

            rightLeg.pitch += s * 0.22F;
            leftLeg.pitch += s * 0.22F;

        } else {
            body.yaw = 0.0F;
        }
    }
}
