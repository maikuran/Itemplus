
package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.ShyronEntity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class ShyronModel extends SinglePartEntityModel<ShyronEntity> {

    private final ModelPart root;

    public ShyronModel(ModelPart root) {
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {

        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        int uvIndex = 0;

        // =====================================================
        // Central Cube
        // Gold outer cube + Black inner cube
        // =====================================================

        addCube(root, "center",
                0F, 0F, 0F,
                12F, 10F, uvIndex++);

        // =====================================================
        // First Layer
        // Six small cubes in six directions
        // =====================================================

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

            addCube(root, "outer_cube_" + i,
                    x, y, z,
                    4F, 3.2F, uvIndex++);

            // =================================================
            // Second Layer
            // Six tiny cubes around each first-layer cube
            // =================================================

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

                addCube(root,
                        "tiny_cube_" + i + "_" + j,
                        tx, ty, tz,
                        1.5F, 1.1F, uvIndex++);
            }
        }

        return TexturedModelData.of(modelData, 256, 256);
    }

    // =========================================================
    // Cube Generator
    // Gold frame + Black inner cube
    // =========================================================

    private static void addCube(
            ModelPartData root,
            String name,
            float x,
            float y,
            float z,
            float outerSize,
            float innerSize,
            int uvIndex
    ) {

        float outerHalf = outerSize / 2F;
        float innerHalf = innerSize / 2F;

        int uvX = (uvIndex % 16) * 16;
        int uvY = (uvIndex / 16) * 16;

        ModelPartBuilder builder = ModelPartBuilder.create()

                // Gold outer cube
                .uv(uvX, uvY)
                .cuboid(
                        -outerHalf,
                        -outerHalf,
                        -outerHalf,
                        outerSize,
                        outerSize,
                        outerSize
                )

                // Black inner cube
                .uv(uvX, uvY + 8)
                .cuboid(
                        -innerHalf,
                        -innerHalf,
                        -innerHalf,
                        innerSize,
                        innerSize,
                        innerSize
                );

        root.addChild(
                name,
                builder,
                ModelTransform.pivot(x, y, z)
        );
    }

    // =========================================================
    // Model Part
    // =========================================================

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    // =========================================================
    // Animation
    // =========================================================

    @Override
    public void setAngles(
            ShyronEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {

        // Static cubic structure
        // No walking animation
    }
}
