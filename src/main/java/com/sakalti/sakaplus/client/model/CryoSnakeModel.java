package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.CryoSnakeEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class CryoSnakeModel extends SinglePartEntityModel<CryoSnakeEntity> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart[] segments = new ModelPart[8];

    // 地面への接地を調整するための基準Y座標（通常、Mobの足元は 24.0F 前後になります）
    // もしこれでも浮く、または埋まる場合はこの数値を調整してください。
    private static final float BASE_Y = 20.0F;

    public CryoSnakeModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");

        for (int i = 0; i  0.0F) {
            float attack = MathHelper.sin(swing * (float) Math.PI);
            float power = attack * 1.0F;

            // Head lunges forward
            head.pitch -= power * 0.75F;
            head.pivotZ -= power * 4.0F;
            head.pivotY -= power * 1.5F;

            // Front body sections follow
            for (int i = 0; i < 8; i++) {
                float influence = 1.0F - (i / 8.0F);

                segments[i].pitch += power * 0.35F * influence;
                segments[i].pivotY -= power * 0.5F * influence;
                segments[i].yaw *= 1.0F - power * 0.5F;
            }
        }
    }
}
