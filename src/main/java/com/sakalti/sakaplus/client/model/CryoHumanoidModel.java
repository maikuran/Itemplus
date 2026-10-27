
package com.sakalti.sakaplus.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.MathHelper;

public class CryoHumanoidModel<T extends HostileEntity>
        extends SinglePartEntityModel<T> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public CryoHumanoidModel(ModelPart root) {
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

        ModelPartData head = root.addChild(
                "head",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4F, -8F, -4F, 8F, 8F, 8F)

                        // Ice crown
                        .uv(32, 0)
                        .cuboid(-1F, -11F, -1F, 2F, 4F, 2F)

                        .uv(40, 0)
                        .cuboid(-5F, -7F, -2F, 2F, 3F, 4F)

                        .uv(40, 7)
                        .cuboid(3F, -7F, -2F, 2F, 3F, 4F),

                ModelTransform.NONE
        );

        // =====================================
        // BODY
        // =====================================

        root.addChild(
                "body",
                ModelPartBuilder.create()

                        // Main torso
                        .uv(0, 20)
                        .cuboid(-4F, 0F, -2F, 8F, 12F, 4F)

                        // Chest armor
                        .uv(24, 20)
                        .cuboid(-4.5F, 1F, -2.5F, 9F, 5F, 5F)

                        // Lower armor
                        .uv(24, 31)
                        .cuboid(-4.5F, 7F, -2.5F, 9F, 4F, 5F)

                        // Ice core
                        .uv(48, 20)
                        .cuboid(-2F, 3F, -3F, 4F, 4F, 1F),

                ModelTransform.NONE
        );

        // =====================================
        // RIGHT ARM
        // =====================================

        root.addChild(
                "right_arm",
                ModelPartBuilder.create()

                        .uv(40, 20)
                        .cuboid(-2F, -2F, -2F, 4F, 12F, 4F)

                        // Shoulder crystal
                        .uv(48, 28)
                        .cuboid(-3F, -3F, -2.5F, 3F, 4F, 3F),

                ModelTransform.pivot(-5F, 2F, 0F)
        );

        // =====================================
        // LEFT ARM
        // =====================================

        root.addChild(
                "left_arm",
                ModelPartBuilder.create()

                        .uv(40, 36)
                        .cuboid(-2F, -2F, -2F, 4F, 12F, 4F)

                        // Shoulder crystal
                        .uv(48, 35)
                        .cuboid(0F, -3F, -2.5F, 3F, 4F, 3F),

                ModelTransform.pivot(5F, 2F, 0F)
        );

        // =====================================
        // RIGHT LEG
        // =====================================

        root.addChild(
                "right_leg",
                ModelPartBuilder.create()

                        .uv(0, 36)
                        .cuboid(-2F, 0F, -2F, 4F, 12F, 4F)

                        // Ice greave
                        .uv(16, 36)
                        .cuboid(-2.5F, 4F, -2.5F, 5F, 5F, 5F),

                ModelTransform.pivot(-1.9F, 12F, 0F)
        );

        // =====================================
        // LEFT LEG
        // =====================================

        root.addChild(
                "left_leg",
                ModelPartBuilder.create()

                        .uv(32, 48)
                        .cuboid(-2F, 0F, -2F, 4F, 12F, 4F)

                        // Ice greave
                        .uv(48, 43)
                        .cuboid(-2.5F, 4F, -2.5F, 5F, 5F, 5F),

                ModelTransform.pivot(1.9F, 12F, 0F)
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
        this.head.yaw = headYaw * ((float)Math.PI / 180F);
        this.head.pitch = headPitch * ((float)Math.PI / 180F);

        // Walking animation
        this.rightArm.pitch =
                MathHelper.cos(limbAngle * 0.6662F + (float)Math.PI)
                        * 2.0F * limbDistance * 0.5F;

        this.leftArm.pitch =
                MathHelper.cos(limbAngle * 0.6662F)
                        * 2.0F * limbDistance * 0.5F;

        this.rightLeg.pitch =
                MathHelper.cos(limbAngle * 0.6662F)
                        * 1.4F * limbDistance;

        this.leftLeg.pitch =
                MathHelper.cos(limbAngle * 0.6662F + (float)Math.PI)
                        * 1.4F * limbDistance;

        // Slight idle animation
        float idle = MathHelper.sin(animationProgress * 0.08F) * 0.025F;

        this.body.pitch = idle;

        this.rightArm.roll = idle;
        this.leftArm.roll = -idle;
    }
}
