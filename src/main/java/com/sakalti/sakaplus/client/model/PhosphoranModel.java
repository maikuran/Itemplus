
package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.PhosphoranEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class PhosphoranModel extends SinglePartEntityModel<PhosphoranEntity> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart[] shields = new ModelPart[4];
    private final ModelPart[] arms = new ModelPart[2];
    private final ModelPart[] legs = new ModelPart[2];

    public PhosphoranModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = root.getChild("head");

        this.shields[0] = head.getChild("shield_1");
        this.shields[1] = head.getChild("shield_2");
        this.shields[2] = head.getChild("shield_3");
        this.shields[3] = head.getChild("shield_4");

        this.arms[0] = root.getChild("arm_l");
        this.arms[1] = root.getChild("arm_r");

        this.legs[0] = root.getChild("leg_l");
        this.legs[1] = root.getChild("leg_r");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // BODY
        root.addChild("body",
            ModelPartBuilder.create()
                .uv(0, 32)
                .cuboid(-4F, -6F, -2F, 8F, 9F, 4F),
            ModelTransform.pivot(0F, 12F, 0F)
        );

        // HEAD
        ModelPartData head = root.addChild("head",
            ModelPartBuilder.create()
                .uv(0, 16)
                .cuboid(-3F, -3F, -3F, 6F, 6F, 6F),
            ModelTransform.pivot(0F, 4F, 0F)
        );

        // FOUR ROTATING SHIELDS
        for (int i = 0; i < 4; i++) {
            head.addChild("shield_" + (i + 1),
                ModelPartBuilder.create()
                    .uv(0, 0)
                    .cuboid(-2.5F, -4F, -6F, 5F, 8F, 1F),
                ModelTransform.NONE
            );
        }

        // ARMS
        root.addChild("arm_l",
            ModelPartBuilder.create()
                .uv(24, 16)
                .cuboid(-1F, 0F, -1.5F, 2F, 8F, 3F),
            ModelTransform.pivot(-5F, 7F, 0F)
        );

        root.addChild("arm_r",
            ModelPartBuilder.create()
                .uv(24, 16)
                .cuboid(-1F, 0F, -1.5F, 2F, 8F, 3F),
            ModelTransform.pivot(5F, 7F, 0F)
        );

        // LEGS
        root.addChild("leg_l",
            ModelPartBuilder.create()
                .uv(36, 16)
                .cuboid(-1.5F, 0F, -1.5F, 3F, 7F, 3F),
            ModelTransform.pivot(-2F, 15F, 0F)
        );

        root.addChild("leg_r",
            ModelPartBuilder.create()
                .uv(36, 16)
                .cuboid(-1.5F, 0F, -1.5F, 3F, 7F, 3F),
            ModelTransform.pivot(2F, 15F, 0F)
        );

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(
            PhosphoranEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        // FOUR SHIELDS ROTATION
        float rotationSpeed = 0.09F;

        for (int i = 0; i < 4; i++) {
            shields[i].yaw =
                animationProgress * rotationSpeed
                + (float)(Math.PI / 2.0 * i);
        }

        // HEAD MOVEMENT
        head.yaw = headYaw * 0.017453292F;
        head.pitch = headPitch * 0.017453292F;

        // FLOATING MOVEMENT
        float hover = MathHelper.sin(animationProgress * 0.08F) * 0.5F;
        body.pivotY = 12F + hover;
        head.pivotY = 4F + hover;

        // ARMS
        arms[0].pitch = MathHelper.cos(limbAngle * 0.5F) * 0.3F * limbDistance;
        arms[1].pitch = MathHelper.cos(limbAngle * 0.5F + (float)Math.PI) * 0.3F * limbDistance;

        // LEGS
        legs[0].pitch = MathHelper.cos(limbAngle * 0.8F) * 0.7F * limbDistance;
        legs[1].pitch = MathHelper.cos(limbAngle * 0.8F + (float)Math.PI) * 0.7F * limbDistance;
    }
}
