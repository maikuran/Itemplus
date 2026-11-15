
package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.VolcanicKingEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class VolcanicKingModel extends SinglePartEntityModel<VolcanicKingEntity> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public VolcanicKingModel(ModelPart root) {
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

                // Crown horns
                .uv(32, 0)
                .cuboid(-6, -12, -2, 3, 6, 3)
                .uv(44, 0)
                .cuboid(3, -12, -2, 3, 6, 3)

                // Central crown
                .uv(56, 0)
                .cuboid(-2, -11, -3, 4, 4, 4)

                // Jaw armor
                .uv(72, 0)
                .cuboid(-4, -2, -5, 8, 3, 5)

                // Side armor
                .uv(98, 0)
                .cuboid(-6, -5, -2, 2, 5, 4)
                .uv(114, 0)
                .cuboid(4, -5, -2, 2, 5, 4),

            ModelTransform.pivot(0, 0, 0)
        );

        head.addChild("forehead_flame",
            ModelPartBuilder.create()
                .uv(0, 20)
                .cuboid(-2, -12, -4, 4, 3, 2)
                .uv(12, 20)
                .cuboid(-1, -15, -3, 2, 4, 2),
            ModelTransform.NONE
        );

        // BODY
        ModelPartData body = root.addChild("body",
            ModelPartBuilder.create()
                .uv(0, 26)
                .cuboid(-5, 0, -3, 10, 13, 6)

                // Upper armor
                .uv(34, 26)
                .cuboid(-7, 1, -4, 14, 6, 8)

                // Lower armor
                .uv(78, 26)
                .cuboid(-6, 7, -4, 12, 6, 8)

                // Molten core
                .uv(0, 48)
                .cuboid(-3, 3, -4.8F, 6, 6, 1)

                // Core frame
                .uv(16, 48)
                .cuboid(-4, 2, -4.5F, 1, 8, 2)
                .cuboid(3, 2, -4.5F, 1, 8, 2),

            ModelTransform.pivot(0, 0, 0)
        );

        // Back volcanic spikes
        body.addChild("back_spike_left",
            ModelPartBuilder.create()
                .uv(40, 48)
                .cuboid(-1, -7, 0, 3, 8, 3),
            ModelTransform.of(4, 3, 2, -0.45F, 0, -0.35F)
        );

        body.addChild("back_spike_right",
            ModelPartBuilder.create()
                .uv(52, 48)
                .cuboid(-2, -7, 0, 3, 8, 3),
            ModelTransform.of(-4, 3, 2, -0.45F, 0, 0.35F)
        );

        body.addChild("back_spike_center",
            ModelPartBuilder.create()
                .uv(64, 48)
                .cuboid(-2, -9, 0, 4, 10, 4),
            ModelTransform.of(0, 2, 3, -0.55F, 0, 0)
        );

        // RIGHT ARM
        root.addChild("right_arm",
            ModelPartBuilder.create()
                .uv(80, 48)
                .cuboid(-3, -2, -3, 6, 13, 6)

                // Shoulder armor
                .uv(104, 48)
                .cuboid(-5, -4, -4, 8, 6, 8)

                // Forearm armor
                .uv(0, 68)
                .cuboid(-4, 5, -4, 7, 6, 7)

                // Lava claws
                .uv(28, 68)
                .cuboid(-3, 10, -4, 2, 5, 2)
                .uv(36, 68)
                .cuboid(0, 10, -4, 2, 5, 2),

            ModelTransform.pivot(-8, 2, 0)
        );

        // LEFT ARM
        root.addChild("left_arm",
            ModelPartBuilder.create()
                .uv(48, 68)
                .cuboid(-3, -2, -3, 6, 13, 6)

                // Shoulder armor
                .uv(72, 68)
                .cuboid(-3, -4, -4, 8, 6, 8)

                // Forearm armor
                .uv(104, 68)
                .cuboid(-3, 5, -4, 7, 6, 7)

                // Lava claws
                .uv(0, 88)
                .cuboid(-2, 10, -4, 2, 5, 2)
                .uv(8, 88)
                .cuboid(1, 10, -4, 2, 5, 2),

            ModelTransform.pivot(8, 2, 0)
        );

        // RIGHT LEG
        root.addChild("right_leg",
            ModelPartBuilder.create()
                .uv(20, 88)
                .cuboid(-3, 0, -3, 6, 12, 6)

                // Heavy greave
                .uv(44, 88)
                .cuboid(-4, 4, -4, 8, 8, 8)

                // Knee armor
                .uv(76, 88)
                .cuboid(-3.5F, 2, -4, 7, 4, 7),

            ModelTransform.pivot(-3, 13, 0)
        );

        // LEFT LEG
        root.addChild("left_leg",
            ModelPartBuilder.create()
                .uv(108, 88)
                .cuboid(-3, 0, -3, 6, 12, 6)

                // Heavy greave
                .uv(0, 108)
                .cuboid(-4, 4, -4, 8, 8, 8)

                // Knee armor
                .uv(32, 108)
                .cuboid(-3.5F, 2, -4, 7, 4, 7),

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
        VolcanicKingEntity entity,
        float limbAngle,
        float limbDistance,
        float animationProgress,
        float headYaw,
        float headPitch
    ) {
        head.yaw = headYaw * ((float) Math.PI / 180F);
        head.pitch = headPitch * ((float) Math.PI / 180F);

        // Walking
        float walk = MathHelper.cos(limbAngle * 0.6662F)
            * 1.15F * limbDistance;

        rightArm.pitch = MathHelper.cos(
            limbAngle * 0.6662F + (float) Math.PI
        ) * 1.1F * limbDistance;

        leftArm.pitch = MathHelper.cos(
            limbAngle * 0.6662F
        ) * 1.1F * limbDistance;

        rightLeg.pitch = walk;
        leftLeg.pitch = -walk;

        // Idle animation
        float idle = MathHelper.sin(animationProgress * 0.08F) * 0.04F;

        body.pitch = idle;
        head.pivotY = idle * 2.0F;

        rightArm.roll = -0.08F + idle;
        leftArm.roll = 0.08F - idle;

        // Attack animation
        float swing = entity.getHandSwingProgress(0.0F);

        if (swing > 0.0F) {
            float s = MathHelper.sin(swing * (float) Math.PI);

            body.yaw = MathHelper.sin(
                MathHelper.sqrt(swing) * ((float) Math.PI * 2F)
            ) * 0.15F;

            rightArm.pitch -= s * 1.85F;
            leftArm.pitch -= s * 1.85F;

            rightArm.roll -= s * 0.2F;
            leftArm.roll += s * 0.2F;

            rightLeg.pitch += s * 0.2F;
            leftLeg.pitch += s * 0.2F;
        } else {
            body.yaw = 0.0F;
        }
    }
}
