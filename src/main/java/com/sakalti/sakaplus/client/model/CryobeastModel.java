
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

        // =====================================
        // HEAD
        // =====================================

        root.addChild(
                "head",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4F, -8F, -4F, 8F, 8F, 8F)

                        // Ice horns
                        .uv(32, 0)
                        .cuboid(-5F, -11F, -1F, 2F, 5F, 2F)

                        .uv(32, 7)
                        .cuboid(3F, -11F, -1F, 2F, 5F, 2F)

                        // Frozen jaw
                        .uv(40, 0)
                        .cuboid(-3F, -2F, -5F, 6F, 3F, 2F)

                        // Side crystals
                        .uv(40, 6)
                        .cuboid(-5F, -5F, -2F, 2F, 4F, 4F)

                        .uv(40, 14)
                        .cuboid(3F, -5F, -2F, 2F, 4F, 4F),

                ModelTransform.pivot(0F, 0F, 0F)
        );

        // =====================================
        // BODY
        // =====================================

        root.addChild(
                "body",
                ModelPartBuilder.create()

                        // Main torso
                        .uv(0, 20)
                        .cuboid(-5F, 0F, -3F, 10F, 13F, 6F)

                        // Upper ice armor
                        .uv(32, 20)
                        .cuboid(-6F, 1F, -3.5F, 12F, 6F, 7F)

                        // Lower armor
                        .uv(32, 34)
                        .cuboid(-5F, 8F, -3.5F, 10F, 5F, 7F)

                        // Frozen core
                        .uv(0, 40)
                        .cuboid(-2F, 4F, -4F, 4F, 5F, 1F)

                        // Back ice spikes
                        .uv(16, 40)
                        .cuboid(-1F, 1F, 3F, 2F, 5F, 3F)

                        .uv(16, 48)
                        .cuboid(-4F, 3F, 3F, 2F, 4F, 3F)

                        .uv(26, 48)
                        .cuboid(2F, 3F, 3F, 2F, 4F, 3F),

                ModelTransform.pivot(0F, 0F, 0F)
        );

        // =====================================
        // RIGHT ARM
        // =====================================

        root.addChild(
                "right_arm",
                ModelPartBuilder.create()

                        // Massive arm
                        .uv(0, 54)
                        .cuboid(-3F, -2F, -3F, 6F, 13F, 6F)

                        // Shoulder armor
                        .uv(24, 54)
                        .cuboid(-4F, -3F, -3.5F, 8F, 5F, 7F)

                        // Claw
                        .uv(48, 54)
                        .cuboid(-2.5F, 9F, -4F, 5F, 3F, 2F)

                        // Ice spike
                        .uv(56, 0)
                        .cuboid(-4F, 0F, 1F, 2F, 5F, 2F),

                ModelTransform.pivot(-7F, 2F, 0F)
        );

        // =====================================
        // LEFT ARM
        // =====================================

        root.addChild(
                "left_arm",
                ModelPartBuilder.create()

                        .uv(0, 54)
                        .cuboid(-3F, -2F, -3F, 6F, 13F, 6F)

                        // Shoulder armor
                        .uv(24, 54)
                        .cuboid(-4F, -3F, -3.5F, 8F, 5F, 7F)

                        // Claw
                        .uv(48, 54)
                        .cuboid(-2.5F, 9F, -4F, 5F, 3F, 2F)

                        // Ice spike
                        .uv(56, 8)
                        .cuboid(2F, 0F, 1F, 2F, 5F, 2F),

                ModelTransform.pivot(7F, 2F, 0F)
        );

        // =====================================
        // RIGHT LEG
        // =====================================

        root.addChild(
                "right_leg",
                ModelPartBuilder.create()

                        // Heavy leg
                        .uv(0, 20)
                        .cuboid(-3F, 0F, -3F, 6F, 12F, 6F)

                        // Frozen greave
                        .uv(24, 20)
                        .cuboid(-3.5F, 4F, -3.5F, 7F, 6F, 7F)

                        // Foot
                        .uv(48, 20)
                        .cuboid(-3.5F, 10F, -5F, 7F, 2F, 8F),

                ModelTransform.pivot(-3F, 12F, 0F)
        );

        // =====================================
        // LEFT LEG
        // =====================================

        root.addChild(
                "left_leg",
                ModelPartBuilder.create()

                        .uv(0, 20)
                        .cuboid(-3F, 0F, -3F, 6F, 12F, 6F)

                        // Frozen greave
                        .uv(24, 20)
                        .cuboid(-3.5F, 4F, -3.5F, 7F, 6F, 7F)

                        // Foot
                        .uv(48, 20)
                        .cuboid(-3.5F, 10F, -5F, 7F, 2F, 8F),

                ModelTransform.pivot(3F, 12F, 0F)
        );

        return TexturedModelData.of(data, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
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

        // =====================================
        // RESET
        // =====================================

        this.head.yaw = headYaw * ((float)Math.PI / 180F);
        this.head.pitch = headPitch * ((float)Math.PI / 180F);

        this.body.pitch = 0F;
        this.body.roll = 0F;

        this.rightArm.pitch = 0F;
        this.leftArm.pitch = 0F;
        this.rightArm.roll = 0F;
        this.leftArm.roll = 0F;

        this.rightLeg.pitch = 0F;
        this.leftLeg.pitch = 0F;

        // =====================================
        // WALK ANIMATION
        // =====================================

        float walkSpeed = 0.6662F;
        float walkAmount = Math.min(limbDistance, 1.0F);

        float walk = limbAngle * walkSpeed;

        this.rightLeg.pitch =
                MathHelper.cos(walk) * 1.15F * walkAmount;

        this.leftLeg.pitch =
                MathHelper.cos(walk + (float)Math.PI)
                        * 1.15F * walkAmount;

        this.rightArm.pitch =
                MathHelper.cos(walk + (float)Math.PI)
                        * 0.85F * walkAmount;

        this.leftArm.pitch =
                MathHelper.cos(walk)
                        * 0.85F * walkAmount;

        // Heavy body movement
        this.body.pitch =
                MathHelper.cos(walk * 2F)
                        * 0.055F * walkAmount;

        this.body.pivotY =
                MathHelper.sin(walk * 2F)
                        * 0.4F * walkAmount;

        // =====================================
        // IDLE ANIMATION
        // =====================================

        float idle = animationProgress;

        this.head.pitch +=
                MathHelper.sin(idle * 0.045F) * 0.035F;

        this.body.pivotY +=
                MathHelper.sin(idle * 0.08F) * 0.12F;

        this.rightArm.roll =
                -0.035F + MathHelper.sin(idle * 0.07F) * 0.025F;

        this.leftArm.roll =
                0.035F - MathHelper.sin(idle * 0.07F) * 0.025F;

        // =====================================
        // ATTACK ANIMATION
        // =====================================

        float attack = entity.getHandSwingProgress(0F);

        if (attack > 0F) {

            float swing = MathHelper.sin(
                    (float)Math.sqrt(attack) * (float)Math.PI
            );

            float power = MathHelper.sin(
                    attack * (float)Math.PI
            );

            // Body leans forward
            this.body.pitch -= swing * 0.42F;

            // Head follows the attack
            this.head.pitch -= swing * 0.18F;

            // Both arms raise and smash forward
            this.rightArm.pitch -= swing * 2.15F;
            this.leftArm.pitch -= swing * 2.15F;

            // Arms spread slightly
            this.rightArm.roll -= power * 0.22F;
            this.leftArm.roll += power * 0.22F;

            // Heavy legs brace the body
            this.rightLeg.pitch += swing * 0.25F;
            this.leftLeg.pitch += swing * 0.25F;

            // Recoil
            this.body.pivotY -= swing * 0.3F;
        }
    }
}
