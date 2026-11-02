
package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.SulfanEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class SulfanModel extends SinglePartEntityModel<SulfanEntity> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart tail;
    private final ModelPart[] legs = new ModelPart[4];
    private final ModelPart[] crystals = new ModelPart[3];

    public SulfanModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.jaw = head.getChild("jaw");
        this.tail = root.getChild("tail");

        this.legs[0] = root.getChild("leg_fl");
        this.legs[1] = root.getChild("leg_fr");
        this.legs[2] = root.getChild("leg_bl");
        this.legs[3] = root.getChild("leg_br");

        this.crystals[0] = body.getChild("crystal_1");
        this.crystals[1] = body.getChild("crystal_2");
        this.crystals[2] = body.getChild("crystal_3");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // BODY
        ModelPartData body = root.addChild("body",
            ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-5F, -4F, -7F, 10F, 8F, 14F),
            ModelTransform.pivot(0F, 12F, 0F)
        );

        // THREE SULFUR CRYSTALS
        body.addChild("crystal_1",
            ModelPartBuilder.create()
                .uv(0, 24)
                .cuboid(-1.5F, -7F, -1.5F, 3F, 7F, 3F),
            ModelTransform.pivot(0F, -3F, -4F)
        );

        body.addChild("crystal_2",
            ModelPartBuilder.create()
                .uv(12, 24)
                .cuboid(-1.5F, -6F, -1.5F, 3F, 6F, 3F),
            ModelTransform.pivot(-3F, -3F, 0F)
        );

        body.addChild("crystal_3",
            ModelPartBuilder.create()
                .uv(24, 24)
                .cuboid(-1.5F, -6F, -1.5F, 3F, 6F, 3F),
            ModelTransform.pivot(3F, -3F, 0F)
        );

        // HEAD
        ModelPartData head = root.addChild("head",
            ModelPartBuilder.create()
                .uv(0, 32)
                .cuboid(-4F, -4F, -4F, 8F, 7F, 7F),
            ModelTransform.pivot(0F, 10F, -7F)
        );

        // HORNS
        head.addChild("horn_l",
            ModelPartBuilder.create()
                .uv(30, 32)
                .cuboid(-1F, -4F, -1F, 2F, 5F, 2F),
            ModelTransform.pivot(-3F, -3F, 0F)
        );

        head.addChild("horn_r",
            ModelPartBuilder.create()
                .uv(38, 32)
                .cuboid(-1F, -4F, -1F, 2F, 5F, 2F),
            ModelTransform.pivot(3F, -3F, 0F)
        );

        // JAW
        head.addChild("jaw",
            ModelPartBuilder.create()
                .uv(0, 48)
                .cuboid(-3F, 0F, -3F, 6F, 2F, 4F),
            ModelTransform.pivot(0F, 1F, -1F)
        );

        // TAIL
        root.addChild("tail",
            ModelPartBuilder.create()
                .uv(20, 48)
                .cuboid(-2F, -1F, 0F, 4F, 4F, 8F),
            ModelTransform.pivot(0F, 11F, 6F)
        );

        // LEGS
        root.addChild("leg_fl",
            ModelPartBuilder.create()
                .uv(44, 0)
                .cuboid(-2F, 0F, -2F, 4F, 6F, 4F),
            ModelTransform.pivot(-3F, 14F, -5F)
        );

        root.addChild("leg_fr",
            ModelPartBuilder.create()
                .uv(44, 0)
                .cuboid(-2F, 0F, -2F, 4F, 6F, 4F),
            ModelTransform.pivot(3F, 14F, -5F)
        );

        root.addChild("leg_bl",
            ModelPartBuilder.create()
                .uv(44, 0)
                .cuboid(-2F, 0F, -2F, 4F, 6F, 4F),
            ModelTransform.pivot(-3F, 14F, 5F)
        );

        root.addChild("leg_br",
            ModelPartBuilder.create()
                .uv(44, 0)
                .cuboid(-2F, 0F, -2F, 4F, 6F, 4F),
            ModelTransform.pivot(3F, 14F, 5F)
        );

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(
            SulfanEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        // HEAD
        head.yaw = headYaw * 0.017453292F;
        head.pitch = headPitch * 0.017453292F;

        // JAW
        jaw.pitch = MathHelper.sin(animationProgress * 0.15F) * 0.08F;

        // WALKING
        float speed = 0.8F;
        float degree = 0.65F;

        legs[0].pitch = MathHelper.cos(limbAngle * speed) * degree * limbDistance;
        legs[1].pitch = MathHelper.cos(limbAngle * speed + (float)Math.PI) * degree * limbDistance;
        legs[2].pitch = MathHelper.cos(limbAngle * speed + (float)Math.PI) * degree * limbDistance;
        legs[3].pitch = MathHelper.cos(limbAngle * speed) * degree * limbDistance;

        // TAIL
        tail.yaw = MathHelper.sin(animationProgress * 0.08F) * 0.35F;
        tail.pitch = MathHelper.sin(animationProgress * 0.06F) * 0.12F;

        // CRYSTALS
        crystals[0].roll = MathHelper.sin(animationProgress * 0.08F) * 0.05F;
        crystals[1].roll = MathHelper.sin(animationProgress * 0.08F + 1F) * 0.08F;
        crystals[2].roll = MathHelper.sin(animationProgress * 0.08F + 2F) * 0.08F;
    }
}
