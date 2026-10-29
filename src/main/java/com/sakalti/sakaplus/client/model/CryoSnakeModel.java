
package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.CryoSnakeEntity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class CryoSnakeModel extends SinglePartEntityModel<CryoSnakeEntity> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart[] segments = new ModelPart[8];

    private static final float BASE_Y = 20.0F;

    public CryoSnakeModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");

        for (int i = 0; i < 8; i++) {
            segments[i] = root.getChild("segment_" + i);
        }
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        root.addChild("head",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0F, -3.0F, -6.0F, 8.0F, 6.0F, 8.0F),
                ModelTransform.pivot(0.0F, BASE_Y, -5.0F));

        for (int i = 0; i < 8; i++) {
            float size = 4.0F - i * 0.25F;

            root.addChild("segment_" + i,
                    ModelPartBuilder.create()
                            .uv(0, 16)
                            .cuboid(-size / 2.0F, -size / 2.0F,
                                    -size / 2.0F,
                                    size, size, size),
                    ModelTransform.pivot(
                            0.0F,
                            BASE_Y,
                            i * 3.0F
                    ));
        }

        return TexturedModelData.of(modelData, 64, 32);
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
        head.yaw = headYaw * ((float) Math.PI / 180.0F);
        head.pitch = headPitch * ((float) Math.PI / 180.0F);

        // Head animation
        head.pivotY = BASE_Y;
        head.pivotZ = -5.0F;

        // Body segments
        for (int i = 0; i < 8; i++) {
            float phase = animationProgress * 0.25F - i * 0.65F;

            segments[i].pivotY = BASE_Y
                    + MathHelper.sin(phase) * 0.5F;

            segments[i].yaw =
                    MathHelper.sin(phase) * 0.18F;

            segments[i].pitch =
                    MathHelper.cos(phase) * 0.04F;

            segments[i].pivotZ = i * 3.0F;
        }

        // Movement animation
        float movement = Math.min(limbDistance, 1.0F);

        for (int i = 0; i < 8; i++) {
            float phase = animationProgress * 0.5F - i * 0.7F;

            segments[i].yaw +=
                    MathHelper.sin(phase) * movement * 0.25F;
        }

        // Attack animation
        float swing = entity.getHandSwingProgress(0.0F);

        if (swing > 0.0F) {
            float attack = MathHelper.sin(
                    swing * (float) Math.PI
            );

            float power = attack;

            head.pitch -= power * 0.75F;
            head.pivotZ -= power * 4.0F;
            head.pivotY -= power * 1.5F;

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
