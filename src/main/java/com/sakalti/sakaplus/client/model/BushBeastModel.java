
package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.BushBeastEntity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class BushBeastModel extends SinglePartEntityModel<BushBeastEntity> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart[] legs = new ModelPart[4];
    private final ModelPart[] backSpikes = new ModelPart[7];

    public BushBeastModel(ModelPart root) {
        this.root = root;

        this.body = root.getChild("body");

        this.legs[0] = root.getChild("leg_fl");
        this.legs[1] = root.getChild("leg_fr");
        this.legs[2] = root.getChild("leg_bl");
        this.legs[3] = root.getChild("leg_br");

        for (int i = 0; i < 7; i++) {
            this.backSpikes[i] = root.getChild("back_spike_" + i);
        }
    }

    public static TexturedModelData getTexturedModelData() {

        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // 四角い胴体
        root.addChild(
            "body",
            ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-5F, -9F, -5F, 10F, 9F, 10F),
            ModelTransform.pivot(0F, 13F, 0F)
        );

        // 足 FL
        root.addChild(
            "leg_fl",
            ModelPartBuilder.create()
                .uv(0, 20)
                .cuboid(-1.5F, 0F, -1.5F, 3F, 4F, 3F),
            ModelTransform.pivot(-3.5F, 13F, -3.5F)
        );

        // 足 FR
        root.addChild(
            "leg_fr",
            ModelPartBuilder.create()
                .uv(12, 20)
                .cuboid(-1.5F, 0F, -1.5F, 3F, 4F, 3F),
            ModelTransform.pivot(3.5F, 13F, -3.5F)
        );

        // 足 BL
        root.addChild(
            "leg_bl",
            ModelPartBuilder.create()
                .uv(24, 20)
                .cuboid(-1.5F, 0F, -1.5F, 3F, 4F, 3F),
            ModelTransform.pivot(-3.5F, 13F, 3.5F)
        );

        // 足 BR
        root.addChild(
            "leg_br",
            ModelPartBuilder.create()
                .uv(36, 20)
                .cuboid(-1.5F, 0F, -1.5F, 3F, 4F, 3F),
            ModelTransform.pivot(3.5F, 13F, 3.5F)
        );

        // 背中の7本の櫛状突起
        for (int i = 0; i < 7; i++) {

            float x = (i - 3) * 2.5F;

            root.addChild(
                "back_spike_" + i,
                ModelPartBuilder.create()
                    .uv(0, 32 + (i % 2) * 8)
                    .cuboid(-1F, -5F, -1F, 2F, 5F, 2F),
                ModelTransform.pivot(x, 4F, 0F)
            );
        }

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(
            BushBeastEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {

        // 歩行アニメーション
        float speed = 0.8F;
        float degree = 0.8F;

        this.legs[0].pitch =
            MathHelper.cos(limbAngle * speed) * degree * limbDistance;

        this.legs[1].pitch =
            MathHelper.cos(limbAngle * speed + (float)Math.PI)
                * degree * limbDistance;

        this.legs[2].pitch =
            MathHelper.cos(limbAngle * speed + (float)Math.PI)
                * degree * limbDistance;

        this.legs[3].pitch =
            MathHelper.cos(limbAngle * speed) * degree * limbDistance;

        // 背中の櫛を少し揺らす
        for (int i = 0; i < 7; i++) {
            this.backSpikes[i].roll =
                MathHelper.sin(animationProgress * 0.08F + i * 0.4F)
                    * 0.08F;
        }
    }
}
