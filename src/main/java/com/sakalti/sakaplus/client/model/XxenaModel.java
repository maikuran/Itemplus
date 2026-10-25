
package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.XxenaEntity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class XxenaModel extends SinglePartEntityModel<XxenaEntity> {

    private final ModelPart root;
    private final ModelPart core;

    private final ModelPart[][] ribs = new ModelPart[6][3];

    private static final int TEXTURE_WIDTH = 512;
    private static final int TEXTURE_HEIGHT = 192;

    private static final int UV_CELL_WIDTH = 64;
    private static final int UV_CELL_HEIGHT = 32;

    public XxenaModel(ModelPart root) {
        this.root = root;
        this.core = root.getChild("core");

        for (int i = 0; i < 6; i++) {
            ModelPart rib = root.getChild("rib_" + i);

            for (int j = 0; j < 3; j++) {
                ribs[i][j] = rib.getChild("segment_" + j);
            }
        }
    }

    public static TexturedModelData getTexturedModelData() {

        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        int uvIndex = 0;

        // =====================================================
        // Purple Core
        // =====================================================

        root.addChild(
                "core",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(
                                -4F, -4F, -4F,
                                8F, 8F, 8F
                        ),
                ModelTransform.NONE
        );

        uvIndex++;

        // =====================================================
        // Six Ribs
        // =====================================================

        for (int i = 0; i < 6; i++) {

            float angle = (float) (Math.PI * 2.0D * i / 6.0D);

            ModelPartData rib = root.addChild(
                    "rib_" + i,
                    ModelPartBuilder.create(),
                    ModelTransform.of(
                            0F,
                            0F,
                            0F,
                            0F,
                            angle,
                            0F
                    )
            );

            // -------------------------------------------------
            // Segment 1: Inner rib
            // -------------------------------------------------

            int uvX1 = (uvIndex % 8) * UV_CELL_WIDTH;
            int uvY1 = (uvIndex / 8) * UV_CELL_HEIGHT;
            uvIndex++;

            rib.addChild(
                    "segment_0",
                    ModelPartBuilder.create()
                            .uv(uvX1, uvY1)
                            .cuboid(
                                    -1F, -2.5F, -1F,
                                    2F, 5F, 2F
                            ),
                    ModelTransform.of(
                            4F, -2F, 0F,
                            0F, 0F, -0.35F
                    )
            );

            // -------------------------------------------------
            // Segment 2: Middle rib
            // -------------------------------------------------

            int uvX2 = (uvIndex % 8) * UV_CELL_WIDTH;
            int uvY2 = (uvIndex / 8) * UV_CELL_HEIGHT;
            uvIndex++;

            rib.addChild(
                    "segment_1",
                    ModelPartBuilder.create()
                            .uv(uvX2, uvY2)
                            .cuboid(
                                    -1F, -2.5F, -1F,
                                    2F, 5F, 2F
                            ),
                    ModelTransform.of(
                            6F, -5F, 0F,
                            0F, 0F, 0.55F
                    )
            );

            // -------------------------------------------------
            // Segment 3: Outer rib
            // -------------------------------------------------

            int uvX3 = (uvIndex % 8) * UV_CELL_WIDTH;
            int uvY3 = (uvIndex / 8) * UV_CELL_HEIGHT;
            uvIndex++;

            rib.addChild(
                    "segment_2",
                    ModelPartBuilder.create()
                            .uv(uvX3, uvY3)
                            .cuboid(
                                    -1F, -2.5F, -1F,
                                    2F, 5F, 2F
                            ),
                    ModelTransform.of(
                            8F, -8F, 0F,
                            0F, 0F, -0.45F
                    )
            );
        }

        return TexturedModelData.of(
                modelData,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
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
            XxenaEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {

        float time = animationProgress * 0.12F;

        // -----------------------------------------------------
        // Core floating animation
        // -----------------------------------------------------

        core.pivotY = (float) Math.sin(time) * 0.5F;

        core.pitch = (float) Math.sin(time * 0.7F) * 0.08F;
        core.yaw = (float) Math.cos(time * 0.5F) * 0.08F;

        // -----------------------------------------------------
        // Rib undulation
        // -----------------------------------------------------

        for (int i = 0; i < 6; i++) {

            float phase = time + i * 1.05F;

            ModelPart rib = this.root.getChild("rib_" + i);

            // Entire rib movement
            rib.roll = (float) Math.sin(phase) * 0.08F;
            rib.pitch = (float) Math.cos(phase * 0.8F) * 0.06F;

            // Segment 1
            ribs[i][0].roll =
                    -0.35F + (float) Math.sin(phase) * 0.16F;

            // Segment 2
            ribs[i][1].roll =
                    0.55F + (float) Math.sin(phase + 0.8F) * 0.20F;

            // Segment 3
            ribs[i][2].roll =
                    -0.45F + (float) Math.sin(phase + 1.6F) * 0.18F;
        }
    }
}
