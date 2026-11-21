
package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.CryofreezerEntity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class CryofreezerModel extends SinglePartEntityModel<CryofreezerEntity> {

    private final ModelPart root;

    private static final int UV_CELL_WIDTH = 64;
    private static final int UV_CELL_HEIGHT = 32;
    private static final int TEXTURE_WIDTH = 512;
    private static final int TEXTURE_HEIGHT = 192;

    // モデル全体の下方向への移動量
    private static final float MODEL_Y_OFFSET = 16F;

    public CryofreezerModel(ModelPart root) {
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // モデル全体を下げる親パーツ
        ModelPartData body = root.addChild(
                "body",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0F, MODEL_Y_OFFSET, 0F)
        );

        int uvIndex = 0;

        // Center cube
        addCube(body, "center",
                0F, 0F, 0F,
                12F, uvIndex++);

        // Six first-layer cubes
        float distance = 8F;

        float[][] directions = {
                { distance, 0F, 0F },
                {-distance, 0F, 0F },
                { 0F, distance, 0F },
                { 0F,-distance, 0F },
                { 0F, 0F, distance },
                { 0F, 0F,-distance }
        };

        for (int i = 0; i < directions.length; i++) {
            float x = directions[i][0];
            float y = directions[i][1];
            float z = directions[i][2];

            addCube(body, "outer_cube_" + i,
                    x, y, z,
                    4F, uvIndex++);

            float tinyDistance = 3F;

            float[][] tinyDirections = {
                    { tinyDistance, 0F, 0F },
                    {-tinyDistance, 0F, 0F },
                    { 0F, tinyDistance, 0F },
                    { 0F,-tinyDistance, 0F },
                    { 0F, 0F, tinyDistance },
                    { 0F, 0F,-tinyDistance }
            };

            for (int j = 0; j < tinyDirections.length; j++) {
                float tx = x + tinyDirections[j][0];
                float ty = y + tinyDirections[j][1];
                float tz = z + tinyDirections[j][2];

                addCube(
                        body,
                        "tiny_cube_" + i + "_" + j,
                        tx, ty, tz,
                        1.5F, uvIndex++
                );
            }
        }

        return TexturedModelData.of(
                modelData,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );
    }

    private static void addCube(
            ModelPartData root,
            String name,
            float x,
            float y,
            float z,
            float size,
            int uvIndex
    ) {
        int uvX = (uvIndex % 8) * UV_CELL_WIDTH;
        int uvY = (uvIndex / 8) * UV_CELL_HEIGHT;

        ModelPartBuilder builder = ModelPartBuilder.create()
                .uv(uvX, uvY)
                .cuboid(
                        -size / 2F,
                        -size / 2F,
                        -size / 2F,
                        size,
                        size,
                        size
                );

        root.addChild(
                name,
                builder,
                ModelTransform.pivot(x, y, z)
        );
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(
            CryofreezerEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        // Static cubic structure
    }
}
