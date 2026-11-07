
package com.sakalti.sakaplus.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.MathHelper;

public class GoblinModel<T extends HostileEntity>
        extends SinglePartEntityModel<T> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public GoblinModel(ModelPart root) {
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
        // HEAD - Big cute goblin head
        // =====================================

        root.addChild(
                "head",
                ModelPartBuilder.create()

                        // Main head
                        .uv(0, 0)
                        .cuboid(-4F, -7F, -4F, 8F, 8F, 8F)

                        // Right big pointed ear
                        .uv(32, 0)
                        .cuboid(-8F, -5F, -1F, 4F, 4F, 3F)

                        // Left big pointed ear
                        .uv(32, 7)
                        .cuboid(4F, -5F, -1F, 4F, 4F, 3F)

                        // Inner ears
                        .uv(46, 0)
                        .cuboid(-7F, -4F, -1.2F, 2F, 2F, 1F)

                        .uv(46, 4)
                        .cuboid(5F, -4F, -1.2F, 2F, 2F, 1F)

                        // Cute nose
                        .uv(0, 17)
                        .cuboid(-1F, -3F, -4.7F, 2F, 2F, 1F)

                        // Eyebrows
                        .uv(16, 17)
                        .cuboid(-3F, -5F, -4.3F, 2F, 1F, 1F)

                        .uv(22, 17)
                        .cuboid(1F, -5F, -4.3F, 2F, 1F, 1F),

                ModelTransform.pivot(0F, 8F, 0F)
        );

        // =====================================
        // BODY - Small chubby body
        // =====================================

        root.addChild(
                "body",
                ModelPartBuilder.create()

                        // Main body
                        .uv(0, 20)
                        .cuboid(-3F, 0F, -2F, 6F, 6F, 4F)

                        // Belly
                        .uv(20, 20)
                        .cuboid(-2.5F, 2F, -2.3F, 5F, 3F, 1F)

                        // Shoulder decoration
                        .uv(32, 20)
                        .cuboid(-3.5F, 0F, -2F, 7F, 2F, 4F),

                ModelTransform.pivot(0F, 14F, 0F)
        );

        // =====================================
        // RIGHT ARM
        // =====================================

        root.addChild(
                "right_arm",
                ModelPartBuilder.create()

                        .uv(0, 30)
                        .cuboid(-1.5F, -1F, -1.5F, 3F, 6F, 3F)

                        // Hand
                        .uv(12, 30)
                        .cuboid(-1.5F, 4F, -2F, 3F, 2F, 3F),

                ModelTransform.pivot(-3.5F, 15F, 0F)
        );

        // =====================================
        // LEFT ARM
        // =====================================

        root.addChild(
                "left_arm",
                ModelPartBuilder.create()

                        .uv(24, 30)
                        .cuboid(-1.5F, -1F, -1.5F, 3F, 6F, 3F)

                        // Hand
                        .uv(36, 30)
                        .cuboid(-1.5F, 4F, -2F, 3F, 2F, 3F),

                ModelTransform.pivot(3.5F, 15F, 0F)
        );

        // =====================================
        // RIGHT LEG
        // =====================================

        root.addChild(
                "right_leg",
                ModelPartBuilder.create()

                        .uv(0, 40)
                        .cuboid(-1.5F, 0F, -1.5F, 3F, 4F, 3F)

                        // Foot
                        .uv(12, 40)
                        .cuboid(-1.5F, 3F, -2F, 3F, 1F, 4F),

                ModelTransform.pivot(-1.5F, 20F, 0F)
        );

        // =====================================
        // LEFT LEG
        // =====================================

        root.addChild(
                "left_leg",
                ModelPartBuilder.create()

                        .uv(24, 40)
                        .cuboid(-1.5F, 0F, -1.5F, 3F, 4F, 3F)

                        // Foot
                        .uv(36, 40)
                        .cuboid(-1.5F, 3F, -2F, 3F, 1F, 4F),

                ModelTransform.pivot(1.5F, 20F, 0F)
        );

        return TexturedModelData.of(data, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(
            T entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {

        // Head rotation
        this.head.yaw =
                headYaw * ((float) Math.PI / 180F);

        this.head.pitch =
                headPitch * ((float) Math.PI / 180F);

        // Walking animation
        this.rightArm.pitch =
                MathHelper.cos(
                        limbAngle * 0.6662F + (float) Math.PI
                ) * 1.5F * limbDistance * 0.5F;

        this.leftArm.pitch =
                MathHelper.cos(
                        limbAngle * 0.6662F
                ) * 1.5F * limbDistance * 0.5F;

        this.rightLeg.pitch =
                MathHelper.cos(
                        limbAngle * 0.6662F
                ) * 1.2F * limbDistance;

        this.leftLeg.pitch =
                MathHelper.cos(
                        limbAngle * 0.6662F + (float) Math.PI
                ) * 1.2F * limbDistance;

        // Cute idle animation
        float idle =
                MathHelper.sin(animationProgress * 0.08F) * 0.035F;

        this.head.pivotY = 8F + idle * 5F;

        this.body.pitch = idle;

        this.rightArm.roll = idle;
        this.leftArm.roll = -idle;

        // Slight ear/head movement
        this.head.roll =
                MathHelper.sin(animationProgress * 0.05F) * 0.015F;
    }
}
