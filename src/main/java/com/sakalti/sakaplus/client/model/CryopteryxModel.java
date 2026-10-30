
package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.CryopteryxEntity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class CryopteryxModel extends SinglePartEntityModel<CryopteryxEntity> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart chest;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart jaw;

    private final ModelPart wingLeft;
    private final ModelPart wingRight;
    private final ModelPart wingTipLeft;
    private final ModelPart wingTipRight;

    private final ModelPart legLeft;
    private final ModelPart legRight;

    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;

    private static final float PI = (float) Math.PI;

    public CryopteryxModel(ModelPart root) {
        this.root = root;

        body = root.getChild("body");
        chest = body.getChild("chest");
        neck = body.getChild("neck");
        head = neck.getChild("head");
        jaw = head.getChild("jaw");

        wingLeft = body.getChild("wing_left");
        wingRight = body.getChild("wing_right");

        wingTipLeft = wingLeft.getChild("wing_tip");
        wingTipRight = wingRight.getChild("wing_tip");

        legLeft = body.getChild("leg_left");
        legRight = body.getChild("leg_right");

        tail1 = body.getChild("tail_1");
        tail2 = tail1.getChild("tail_2");
        tail3 = tail2.getChild("tail_3");
        tail4 = tail3.getChild("tail_4");
    }

    public static TexturedModelData getTexturedModelData() {

        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        // =====================================
        // BODY
        // =====================================

        ModelPartData body = root.addChild(
                "body",
                ModelPartBuilder.create()
                        // Main torso
                        .uv(0, 0)
                        .cuboid(-6, -5, -8, 12, 11, 18)

                        // Belly armor
                        .uv(0, 32)
                        .cuboid(-4.5F, 3, -6, 9, 4, 14)

                        // Chest armor
                        .uv(48, 0)
                        .cuboid(-7, -7, -8, 14, 9, 9)

                        // Back armor
                        .uv(48, 22)
                        .cuboid(-5, -8, -5, 10, 3, 13)

                        // Shoulder crystals
                        .uv(96, 0)
                        .cuboid(-7, -10, -5, 3, 5, 4)
                        .cuboid(4, -10, -5, 3, 5, 4)

                        // Spine crystals
                        .uv(112, 0)
                        .cuboid(-1.5F, -12, -5, 3, 5, 3)
                        .cuboid(-1.5F, -11, 0, 3, 5, 3)
                        .cuboid(-1.5F, -10, 5, 3, 4, 3),

                ModelTransform.pivot(0, 13, 0)
        );

        // =====================================
        // CHEST
        // =====================================

        body.addChild(
                "chest",
                ModelPartBuilder.create()
                        .uv(0, 56)
                        .cuboid(-6, -5, -7, 12, 10, 8)

                        .uv(40, 56)
                        .cuboid(-5, -6, -6, 10, 3, 5)

                        .uv(72, 56)
                        .cuboid(-3, -8, -4, 6, 3, 4)

                        // Chest crystals
                        .uv(96, 32)
                        .cuboid(-5, -8, -2, 2, 4, 2)
                        .cuboid(3, -8, -2, 2, 4, 2)
                        .cuboid(-1, -10, -4, 2, 4, 2),

                ModelTransform.pivot(0, 0, -7)
        );

        // =====================================
        // NECK
        // =====================================

        ModelPartData neck = body.addChild(
                "neck",
                ModelPartBuilder.create()
                        .uv(0, 80)
                        .cuboid(-3.5F, -4, -7, 7, 8, 10)

                        // Neck scales
                        .uv(32, 80)
                        .cuboid(-4, -5, -5, 8, 3, 7)

                        // Lower neck armor
                        .uv(64, 80)
                        .cuboid(-3, 3, -6, 6, 3, 7)

                        // Crystal spikes
                        .uv(96, 64)
                        .cuboid(-1, -7, -5, 2, 3, 2)
                        .cuboid(-1, -7, -1, 2, 3, 2)
                        .cuboid(-1, -6, 3, 2, 3, 2),

                ModelTransform.pivot(0, -2, -9)
        );

        // =====================================
        // HEAD
        // =====================================

        ModelPartData head = neck.addChild(
                "head",
                ModelPartBuilder.create()
                        // Skull
                        .uv(0, 104)
                        .cuboid(-4.5F, -4, -8, 9, 8, 10)

                        // Snout
                        .uv(40, 104)
                        .cuboid(-3.5F, -2, -15, 7, 5, 8)

                        // Upper jaw
                        .uv(72, 104)
                        .cuboid(-3, 1, -14, 6, 2, 6)

                        // Brow armor
                        .uv(96, 104)
                        .cuboid(-5, -5, -7, 3, 3, 5)
                        .cuboid(2, -5, -7, 3, 3, 5)

                        // Horns
                        .uv(120, 96)
                        .cuboid(-5, -9, -3, 2, 6, 3)
                        .cuboid(3, -9, -3, 2, 6, 3)

                        // Central horn
                        .uv(136, 96)
                        .cuboid(-1, -8, -5, 2, 5, 3)

                        // Ice crown
                        .uv(152, 96)
                        .cuboid(-3, -11, 0, 2, 5, 2)
                        .cuboid(1, -11, 0, 2, 5, 2)

                        // Side spikes
                        .uv(168, 96)
                        .cuboid(-6, -3, -1, 2, 3, 4)
                        .cuboid(4, -3, -1, 2, 3, 4)

                        // Nostrils
                        .uv(184, 96)
                        .cuboid(-2.5F, -1, -15.5F, 1, 1, 1)
                        .cuboid(1.5F, -1, -15.5F, 1, 1, 1),

                ModelTransform.pivot(0, -1, -7)
        );

        // =====================================
        // EYES
        // =====================================

        head.addChild(
                "eyes",
                ModelPartBuilder.create()
                        .uv(200, 0)
                        .cuboid(-4.7F, -3, -8, 2, 2, 2)
                        .cuboid(2.7F, -3, -8, 2, 2, 2),

                ModelTransform.NONE
        );

        // =====================================
        // JAW
        // =====================================

        head.addChild(
                "jaw",
                ModelPartBuilder.create()
                        .uv(40, 120)
                        .cuboid(-3.5F, 0, -14, 7, 2, 7)

                        // Lower teeth
                        .uv(72, 120)
                        .cuboid(-2.5F, -1, -12, 1, 2, 2)
                        .cuboid(1.5F, -1, -12, 1, 2, 2)

                        // Chin crystal
                        .uv(96, 120)
                        .cuboid(-1, 1, -15, 2, 2, 3),

                ModelTransform.pivot(0, 3, 0)
        );

        // =====================================
        // WINGS
        // =====================================

        addWing(body, "wing_left", 1);
        addWing(body, "wing_right", -1);

        // =====================================
        // LEGS
        // =====================================

        addLeg(body, "leg_left", 1);
        addLeg(body, "leg_right", -1);

        // =====================================
        // TAIL
        // =====================================

        ModelPartData tail1 = body.addChild(
                "tail_1",
                ModelPartBuilder.create()
                        .uv(120, 0)
                        .cuboid(-3.5F, -3, 0, 7, 7, 9)

                        .uv(152, 0)
                        .cuboid(-4, -5, 2, 8, 3, 5)

                        .uv(176, 0)
                        .cuboid(-1, -7, 2, 2, 4, 3),

                ModelTransform.pivot(0, 1, 9)
        );

        ModelPartData tail2 = tail1.addChild(
                "tail_2",
                ModelPartBuilder.create()
                        .uv(120, 24)
                        .cuboid(-2.8F, -2.5F, 0, 5.6F, 5, 8)

                        .uv(152, 24)
                        .cuboid(-3, -4, 2, 6, 2, 4)

                        .uv(176, 24)
                        .cuboid(-1, -6, 2, 2, 3, 3),

                ModelTransform.pivot(0, 0, 8)
        );

        ModelPartData tail3 = tail2.addChild(
                "tail_3",
                ModelPartBuilder.create()
                        .uv(120, 40)
                        .cuboid(-2, -2, 0, 4, 4, 8)

                        .uv(152, 40)
                        .cuboid(-2.5F, -4, 1, 5, 2, 4),

                ModelTransform.pivot(0, 0, 7)
        );

        tail3.addChild(
                "tail_4",
                ModelPartBuilder.create()
                        .uv(120, 56)
                        .cuboid(-1.5F, -1.5F, 0, 3, 3, 7)

                        // Large ice blade
                        .uv(152, 56)
                        .cuboid(-1, -4, 2, 2, 4, 6)

                        .uv(176, 56)
                        .cuboid(-3, -2, 5, 6, 2, 3),

                ModelTransform.pivot(0, 0, 7)
        );

        return TexturedModelData.of(data, 256, 256);
    }

    // =====================================
    // WING BUILDER
    // =====================================

    private static void addWing(
            ModelPartData body,
            String name,
            int side
    ) {

        float x = side * 5.0F;

        ModelPartData wing = body.addChild(
                name,
                ModelPartBuilder.create()
                        // Wing shoulder
                        .uv(0, 144)
                        .cuboid(-side * 2, -2, -3, 4, 5, 7)

                        // Main bone
                        .uv(24, 144)
                        .cuboid(
                                side > 0 ? 1 : -14,
                                -1,
                                0,
                                13,
                                3,
                                3
                        )

                        // Wing membrane
                        .uv(56, 144)
                        .cuboid(
                                side > 0 ? 3 : -13,
                                0,
                                1,
                                10,
                                1,
                                9
                        )

                        // Secondary membrane
                        .uv(96, 144)
                        .cuboid(
                                side > 0 ? 5 : -11,
                                1,
                                4,
                                8,
                                1,
                                7
                        )

                        // Wing fingers
                        .uv(128, 144)
                        .cuboid(
                                side > 0 ? 10 : -12,
                                -1,
                                0,
                                2,
                                2,
                                12
                        )

                        .cuboid(
                                side > 0 ? 5 : -7,
                                0,
                                2,
                                2,
                                2,
                                12
                        )

                        // Wing claws
                        .uv(160, 144)
                        .cuboid(
                                side > 0 ? 11 : -13,
                                -2,
                                10,
                                2,
                                3,
                                3
                        ),

                ModelTransform.pivot(x, -5, 0)
        );

        // Wing tip
        wing.addChild(
                "wing_tip",
                ModelPartBuilder.create()
                        // Outer wing bone
                        .uv(0, 168)
                        .cuboid(
                                side > 0 ? 0 : -10,
                                -1,
                                0,
                                10,
                                2,
                                2
                        )

                        // Outer membrane
                        .uv(32, 168)
                        .cuboid(
                                side > 0 ? 1 : -9,
                                0,
                                1,
                                9,
                                1,
                                8
                        )

                        // Long wing claw
                        .uv(64, 168)
                        .cuboid(
                                side > 0 ? 8 : -10,
                                -2,
                                6,
                                2,
                                3,
                                5
                        )

                        // Crystal at wing tip
                        .uv(88, 168)
                        .cuboid(
                                side > 0 ? 7 : -10,
                                -4,
                                2,
                                3,
                                4,
                                3
                        ),

                ModelTransform.pivot(side * 12, 0, 0)
        );
    }

    // =====================================
    // LEG BUILDER
    // =====================================

    private static void addLeg(
            ModelPartData body,
            String name,
            int side
    ) {

        ModelPartData leg = body.addChild(
                name,
                ModelPartBuilder.create()
                        // Thigh
                        .uv(112, 80)
                        .cuboid(-3, -1, -3, 6, 8, 7)

                        // Thigh armor
                        .uv(144, 80)
                        .cuboid(-3.5F, -2, -3, 7, 4, 6)

                        // Knee crystal
                        .uv(176, 80)
                        .cuboid(-2, 5, -3, 4, 3, 3),

                ModelTransform.pivot(side * 4, 3, 5)
        );

        leg.addChild(
                "foot",
                ModelPartBuilder.create()
                        // Shin
                        .uv(112, 96)
                        .cuboid(-2, 0, -2, 4, 6, 4)

                        // Foot
                        .uv(136, 96)
                        .cuboid(-3, 4, -5, 6, 3, 7)

                        // Claws
                        .uv(168, 96)
                        .cuboid(-3, 5, -8, 2, 2, 4)
                        .cuboid(-1, 5, -8, 2, 2, 4)
                        .cuboid(1, 5, -8, 2, 2, 4),

                ModelTransform.pivot(0, 6, 0)
        );
    }

    // =====================================
    // ROOT
    // =====================================

    @Override
    public ModelPart getPart() {
        return root;
    }

    // =====================================
    // ANIMATIONS
    // =====================================

    @Override
    public void setAngles(
            CryopteryxEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {

        float time = animationProgress;

        float movement = Math.min(limbDistance, 1.0F);

        boolean flying = !entity.isOnGround();

        // =================================
        // RESET
        // =================================

        body.pitch = 0;
        body.yaw = 0;
        body.roll = 0;

        chest.pitch = 0;

        neck.pitch = 0;
        neck.yaw = 0;

        head.pitch = 0;
        head.yaw = 0;
        head.roll = 0;

        jaw.pitch = 0;

        wingLeft.pitch = 0;
        wingRight.pitch = 0;

        wingLeft.roll = 0;
        wingRight.roll = 0;

        wingTipLeft.roll = 0;
        wingTipRight.roll = 0;

        legLeft.pitch = 0;
        legRight.pitch = 0;

        // =================================
        // HEAD TRACKING
        // =================================

        head.yaw = headYaw * PI / 180.0F;
        head.pitch = headPitch * PI / 180.0F;

        neck.yaw = headYaw * PI / 180.0F * 0.25F;
        neck.pitch = headPitch * PI / 180.0F * 0.25F;

        // =================================
        // IDLE ANIMATION
        // =================================

        float breathe = MathHelper.sin(time * 0.08F);

        chest.pivotY = -breathe * 0.35F;

        body.pivotY = 13.0F
                + MathHelper.sin(time * 0.06F) * 0.25F;

        head.pivotY = -1.0F
                + MathHelper.sin(time * 0.1F) * 0.2F;

        // =================================
        // WING FLAPPING
        // =================================

        float flapSpeed = flying ? 0.48F : 0.12F;
        float flapAmount = flying ? 0.85F : 0.12F;

        float flap = MathHelper.sin(time * flapSpeed);

        wingLeft.roll = -flap * flapAmount;
        wingRight.roll = flap * flapAmount;

        wingTipLeft.roll = -flap * 0.35F;
        wingTipRight.roll = flap * 0.35F;

        wingLeft.pitch = MathHelper.cos(time * flapSpeed) * 0.12F;
        wingRight.pitch = MathHelper.cos(time * flapSpeed) * 0.12F;

        // =================================
        // FLYING ANIMATION
        // =================================

        if (flying) {

            body.pitch = -0.12F;

            chest.pitch = -0.08F;

            legLeft.pitch = -0.8F;
            legRight.pitch = -0.8F;

            head.pitch -= 0.08F;

        } else {

            legLeft.pitch = MathHelper.sin(limbAngle * 0.5F)
                    * movement * 0.7F;

            legRight.pitch = MathHelper.sin(limbAngle * 0.5F + PI)
                    * movement * 0.7F;
        }

        // =================================
        // TAIL MOVEMENT
        // =================================

        tail1.yaw = MathHelper.sin(time * 0.08F) * 0.12F;

        tail2.yaw = MathHelper.sin(time * 0.08F - 0.5F) * 0.18F;

        tail3.yaw = MathHelper.sin(time * 0.08F - 1.0F) * 0.24F;

        tail4.yaw = MathHelper.sin(time * 0.08F - 1.5F) * 0.30F;

        // Tail vertical movement
        tail1.pitch = MathHelper.sin(time * 0.06F) * 0.08F;
        tail2.pitch = MathHelper.sin(time * 0.06F - 0.4F) * 0.10F;
        tail3.pitch = MathHelper.sin(time * 0.06F - 0.8F) * 0.12F;
        tail4.pitch = MathHelper.sin(time * 0.06F - 1.2F) * 0.15F;

        // =================================
        // ATTACK ANIMATION
        // =================================

        float swing = entity.getHandSwingProgress(0.0F);

        if (swing > 0.0F) {

            float attack = MathHelper.sin(swing * PI);

            // Body lunge
            body.pitch -= attack * 0.22F;

            // Head strike
            neck.pitch -= attack * 0.25F;
            head.pitch -= attack * 0.45F;

            head.pivotZ -= attack * 1.5F;

            // Wings spread
            wingLeft.roll -= attack * 0.25F;
            wingRight.roll += attack * 0.25F;

            // Tail counterbalance
            tail1.yaw += attack * 0.2F;
            tail2.yaw += attack * 0.15F;

            // Open jaw
            jaw.pitch += attack * 0.4F;
        }
    }
}
