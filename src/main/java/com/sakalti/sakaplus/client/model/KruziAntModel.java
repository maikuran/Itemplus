package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.KruziAntEntity;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public final class KruziAntModel extends SinglePartEntityModel<KruziAntEntity> {

    private final ModelPart root;
    private final ModelPart thorax;
    private final ModelPart head;
    private final ModelPart abdomen;

    private final ModelPart leftForeWing;
    private final ModelPart rightForeWing;
    private final ModelPart leftHindWing;
    private final ModelPart rightHindWing;

    private final ModelPart leftForeTip;
    private final ModelPart rightForeTip;
    private final ModelPart leftHindTip;
    private final ModelPart rightHindTip;

    private final ModelPart leftAntenna;
    private final ModelPart rightAntenna;

    private final ModelPart[] legs = new ModelPart[6];

    public KruziAntModel(ModelPart root) {
        this.root = root;

        this.thorax = root.getChild("thorax");
        this.head = root.getChild("head");
        this.abdomen = root.getChild("abdomen");

        this.leftForeWing = root.getChild("left_fore_wing");
        this.rightForeWing = root.getChild("right_fore_wing");
        this.leftHindWing = root.getChild("left_hind_wing");
        this.rightHindWing = root.getChild("right_hind_wing");

        this.leftForeTip = leftForeWing.getChild("tip");
        this.rightForeTip = rightForeWing.getChild("tip");
        this.leftHindTip = leftHindWing.getChild("tip");
        this.rightHindTip = rightHindWing.getChild("tip");

        this.leftAntenna = head.getChild("left_antenna");
        this.rightAntenna = head.getChild("right_antenna");

        this.legs[0] = root.getChild("leg_front_left");
        this.legs[1] = root.getChild("leg_front_right");
        this.legs[2] = root.getChild("leg_mid_left");
        this.legs[3] = root.getChild("leg_mid_right");
        this.legs[4] = root.getChild("leg_rear_left");
        this.legs[5] = root.getChild("leg_rear_right");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // =====================================================
        // 胸部
        // =====================================================

        ModelPartData thorax = root.addChild(
                "thorax",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F)
        );

        thorax.addChild(
                "thorax_core",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 6.0F),
                ModelTransform.NONE
        );

        thorax.addChild(
                "dorsal_plate",
                ModelPartBuilder.create()
                        .uv(24, 0)
                        .cuboid(-2.0F, -3.0F, -2.5F, 4.0F, 1.0F, 5.0F),
                ModelTransform.NONE
        );

        thorax.addChild(
                "front_collar",
                ModelPartBuilder.create()
                        .uv(44, 0)
                        .cuboid(-2.0F, -1.8F, -4.0F, 4.0F, 3.0F, 1.5F),
                ModelTransform.NONE
        );

        thorax.addChild(
                "rear_collar",
                ModelPartBuilder.create()
                        .uv(44, 10)
                        .cuboid(-1.8F, -1.5F, 2.6F, 3.6F, 2.6F, 1.4F),
                ModelTransform.NONE
        );

        thorax.addChild(
                "left_shoulder",
                ModelPartBuilder.create()
                        .uv(0, 17)
                        .cuboid(2.0F, -1.7F, -2.0F, 1.8F, 2.4F, 2.2F),
                ModelTransform.NONE
        );

        thorax.addChild(
                "right_shoulder",
                ModelPartBuilder.create()
                        .uv(16, 17)
                        .cuboid(-3.8F, -1.7F, -2.0F, 1.8F, 2.4F, 2.2F),
                ModelTransform.NONE
        );

        // =====================================================
        // 頭部・複眼
        // =====================================================

        ModelPartData head = root.addChild(
                "head",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, -0.1F, -4.0F)
        );

        head.addChild(
                "head_capsule",
                ModelPartBuilder.create()
                        .uv(32, 18)
                        .cuboid(-2.5F, -2.2F, -3.0F, 5.0F, 4.4F, 3.0F),
                ModelTransform.NONE
        );

        head.addChild(
                "clypeus",
                ModelPartBuilder.create()
                        .uv(52, 18)
                        .cuboid(-1.5F, -1.1F, -3.5F, 3.0F, 2.0F, 0.7F),
                ModelTransform.NONE
        );

        head.addChild(
                "left_eye_socket",
                ModelPartBuilder.create()
                        .uv(0, 29)
                        .cuboid(1.8F, -1.5F, -2.8F, 1.1F, 2.5F, 1.1F),
                ModelTransform.NONE
        );

        head.addChild(
                "right_eye_socket",
                ModelPartBuilder.create()
                        .uv(8, 29)
                        .cuboid(-2.9F, -1.5F, -2.8F, 1.1F, 2.5F, 1.1F),
                ModelTransform.NONE
        );

        head.addChild(
                "left_eye_glow",
                ModelPartBuilder.create()
                        .uv(16, 29)
                        .cuboid(2.05F, -1.0F, -3.0F, 0.6F, 1.5F, 0.5F),
                ModelTransform.NONE
        );

        head.addChild(
                "right_eye_glow",
                ModelPartBuilder.create()
                        .uv(20, 29)
                        .cuboid(-2.65F, -1.0F, -3.0F, 0.6F, 1.5F, 0.5F),
                ModelTransform.NONE
        );

        // 左触角
        ModelPartData leftAntenna = head.addChild(
                "left_antenna",
                ModelPartBuilder.create(),
                ModelTransform.pivot(1.2F, -1.8F, -2.0F)
        );

        leftAntenna.addChild(
                "stem",
                ModelPartBuilder.create()
                        .uv(26, 29)
                        .cuboid(-0.25F, -2.0F, -0.2F, 0.5F, 2.2F, 0.5F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.35F)
        );

        leftAntenna.addChild(
                "club",
                ModelPartBuilder.create()
                        .uv(30, 29)
                        .cuboid(-0.45F, -2.6F, -0.35F, 0.9F, 0.9F, 0.8F),
                ModelTransform.of(0.0F, -1.7F, 0.0F, 0.0F, 0.0F, -0.5F)
        );

        // 右触角
        ModelPartData rightAntenna = head.addChild(
                "right_antenna",
                ModelPartBuilder.create(),
                ModelTransform.pivot(-1.2F, -1.8F, -2.0F)
        );

        rightAntenna.addChild(
                "stem",
                ModelPartBuilder.create()
                        .uv(35, 29)
                        .cuboid(-0.25F, -2.0F, -0.2F, 0.5F, 2.2F, 0.5F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.35F)
        );

        rightAntenna.addChild(
                "club",
                ModelPartBuilder.create()
                        .uv(39, 29)
                        .cuboid(-0.45F, -2.6F, -0.35F, 0.9F, 0.9F, 0.8F),
                ModelTransform.of(0.0F, -1.7F, 0.0F, 0.0F, 0.0F, 0.5F)
        );

        // =====================================================
        // 腹部
        // =====================================================

        ModelPartData abdomen = root.addChild(
                "abdomen",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 0.2F, 3.2F)
        );

        abdomen.addChild(
                "petiole",
                ModelPartBuilder.create()
                        .uv(48, 29)
                        .cuboid(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 2.4F),
                ModelTransform.NONE
        );

        abdomen.addChild(
                "gaster",
                ModelPartBuilder.create()
                        .uv(0, 40)
                        .cuboid(-2.5F, -2.3F, 1.0F, 5.0F, 4.6F, 6.0F),
                ModelTransform.NONE
        );

        abdomen.addChild(
                "gaster_dorsal",
                ModelPartBuilder.create()
                        .uv(24, 40)
                        .cuboid(-1.7F, -2.6F, 1.7F, 3.4F, 0.7F, 4.3F),
                ModelTransform.NONE
        );

        abdomen.addChild(
                "terminal_spine",
                ModelPartBuilder.create()
                        .uv(42, 40)
                        .cuboid(-0.65F, -0.45F, 6.5F, 1.3F, 1.2F, 1.8F),
                ModelTransform.NONE
        );

        // =====================================================
        // 6本の脚
        // =====================================================

        addLeg(root, "leg_front_left",
                2.0F, -0.3F, -2.0F, 0, 94,
                -0.62F, 0.65F, 0.0F,
                -0.48F, 0.95F, 0.0F,
                -0.16F, 0.75F, 0.0F);

        addLeg(root, "leg_front_right",
                -2.0F, -0.3F, -2.0F, 40, 94,
                -0.62F, -0.65F, 0.0F,
                0.48F, -0.95F, 0.0F,
                0.16F, -0.75F, 0.0F);

        addLeg(root, "leg_mid_left",
                2.3F, 0.0F, 0.0F, 80, 94,
                -0.08F, 0.62F, 0.0F,
                0.05F, 1.0F, 0.0F,
                0.05F, 0.85F, 0.0F);

        addLeg(root, "leg_mid_right",
                -2.3F, 0.0F, 0.0F, 0, 110,
                -0.08F, -0.62F, 0.0F,
                0.05F, -1.0F, 0.0F,
                0.05F, -0.85F, 0.0F);

        addLeg(root, "leg_rear_left",
                2.0F, 0.1F, 2.0F, 40, 110,
                0.52F, 0.58F, 0.0F,
                0.65F, 0.9F, 0.0F,
                0.12F, 0.8F, 0.0F);

        addLeg(root, "leg_rear_right",
                -2.0F, 0.1F, 2.0F, 80, 110,
                0.52F, -0.58F, 0.0F,
                0.65F, -0.9F, 0.0F,
                0.12F, -0.8F, 0.0F);

        // =====================================================
        // 前翅
        // =====================================================

        ModelPartData leftForeWing = root.addChild(
                "left_fore_wing",
                ModelPartBuilder.create(),
                ModelTransform.pivot(2.2F, -2.0F, -1.7F)
        );

        leftForeWing.addChild(
                "fore_membrane",
                ModelPartBuilder.create()
                        .uv(64, 0)
                        .cuboid(0.0F, -0.12F, -4.2F, 7.2F, 0.24F, 5.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.10F, -0.04F)
        );

        leftForeWing.addChild(
                "fore_vein_ridge",
                ModelPartBuilder.create()
                        .uv(64, 12)
                        .cuboid(0.15F, -0.18F, -1.8F, 6.8F, 0.12F, 0.28F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.10F, -0.04F)
        );

        ModelPartData leftForeTip = leftForeWing.addChild(
                "tip",
                ModelPartBuilder.create(),
                ModelTransform.pivot(6.4F, 0.0F, -2.0F)
        );

        leftForeTip.addChild(
                "tip_membrane",
                ModelPartBuilder.create()
                        .uv(64, 18)
                        .cuboid(0.0F, -0.08F, -1.8F, 2.2F, 0.16F, 2.2F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.04F, -0.025F)
        );

        ModelPartData rightForeWing = root.addChild(
                "right_fore_wing",
                ModelPartBuilder.create(),
                ModelTransform.pivot(-2.2F, -2.0F, -1.7F)
        );

        rightForeWing.addChild(
                "fore_membrane",
                ModelPartBuilder.create()
                        .uv(64, 23)
                        .cuboid(-7.2F, -0.12F, -4.2F, 7.2F, 0.24F, 5.0F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.10F, 0.04F)
        );

        rightForeWing.addChild(
                "fore_vein_ridge",
                ModelPartBuilder.create()
                        .uv(64, 35)
                        .cuboid(-6.95F, -0.18F, -1.8F, 6.8F, 0.12F, 0.28F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.10F, 0.04F)
        );

        ModelPartData rightForeTip = rightForeWing.addChild(
                "tip",
                ModelPartBuilder.create(),
                ModelTransform.pivot(-6.4F, 0.0F, -2.0F)
        );

        rightForeTip.addChild(
                "tip_membrane",
                ModelPartBuilder.create()
                        .uv(64, 41)
                        .cuboid(-2.2F, -0.08F, -1.8F, 2.2F, 0.16F, 2.2F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.04F, 0.025F)
        );

        // =====================================================
        // 後翅
        // =====================================================

        ModelPartData leftHindWing = root.addChild(
                "left_hind_wing",
                ModelPartBuilder.create(),
                ModelTransform.pivot(2.0F, -1.7F, 1.0F)
        );

        leftHindWing.addChild(
                "hind_membrane",
                ModelPartBuilder.create()
                        .uv(64, 47)
                        .cuboid(0.0F, -0.1F, -0.2F, 5.7F, 0.2F, 5.1F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.20F, -0.08F)
        );

        leftHindWing.addChild(
                "hind_vein_ridge",
                ModelPartBuilder.create()
                        .uv(64, 59)
                        .cuboid(0.1F, -0.16F, 2.0F, 5.3F, 0.12F, 0.25F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.20F, -0.08F)
        );

        ModelPartData leftHindTip = leftHindWing.addChild(
                "tip",
                ModelPartBuilder.create(),
                ModelTransform.pivot(5.0F, 0.0F, 3.2F)
        );

        leftHindTip.addChild(
                "tip_membrane",
                ModelPartBuilder.create()
                        .uv(64, 65)
                        .cuboid(0.0F, -0.07F, -0.4F, 1.8F, 0.14F, 1.8F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.06F, -0.04F)
        );

        ModelPartData rightHindWing = root.addChild(
                "right_hind_wing",
                ModelPartBuilder.create(),
                ModelTransform.pivot(-2.0F, -1.7F, 1.0F)
        );

        rightHindWing.addChild(
                "hind_membrane",
                ModelPartBuilder.create()
                        .uv(64, 70)
                        .cuboid(-5.7F, -0.1F, -0.2F, 5.7F, 0.2F, 5.1F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.20F, 0.08F)
        );

        rightHindWing.addChild(
                "hind_vein_ridge",
                ModelPartBuilder.create()
                        .uv(64, 82)
                        .cuboid(-5.4F, -0.16F, 2.0F, 5.3F, 0.12F, 0.25F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.20F, 0.08F)
        );

        ModelPartData rightHindTip = rightHindWing.addChild(
                "tip",
                ModelPartBuilder.create(),
                ModelTransform.pivot(-5.0F, 0.0F, 3.2F)
        );

        rightHindTip.addChild(
                "tip_membrane",
                ModelPartBuilder.create()
                        .uv(64, 88)
                        .cuboid(-1.8F, -0.07F, -0.4F, 1.8F, 0.14F, 1.8F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.06F, 0.04F)
        );

        return TexturedModelData.of(modelData, 128, 128);
    }

    private static void addLeg(
            ModelPartData root,
            String name,
            float x,
            float y,
            float z,
            int uvX,
            int uvY,
            float coxaZ,
            float coxaX,
            float coxaY,
            float femurZ,
            float femurX,
            float femurY,
            float tibiaZ,
            float tibiaX,
            float tibiaY
    ) {
        ModelPartData leg = root.addChild(
                name,
                ModelPartBuilder.create(),
                ModelTransform.pivot(x, y, z)
        );

        leg.addChild(
                "coxa",
                ModelPartBuilder.create()
                        .uv(uvX, uvY)
                        .cuboid(-0.45F, -0.45F, -0.3F, 0.9F, 0.9F, 1.3F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, coxaY, coxaZ)
        );

        leg.addChild(
                "femur",
                ModelPartBuilder.create()
                        .uv(uvX, uvY + 7)
                        .cuboid(-0.35F, -0.35F, 0.7F, 0.7F, 0.7F, 2.4F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, femurY, femurX, femurZ)
        );

        leg.addChild(
                "tibia",
                ModelPartBuilder.create()
                        .uv(uvX + 12, uvY + 7)
                        .cuboid(-0.22F, -0.22F, 2.6F, 0.44F, 0.44F, 3.2F),
                ModelTransform.of(0.0F, 0.0F, 0.0F, tibiaY, tibiaX, tibiaZ)
        );

        leg.addChild(
                "tarsus",
                ModelPartBuilder.create()
                        .uv(uvX + 20, uvY + 7)
                        .cuboid(-0.14F, -0.14F, 5.5F, 0.28F, 0.28F, 1.6F),
                ModelTransform.NONE
        );
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(
            KruziAntEntity entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        float t = animationProgress;

        // 前翅の羽ばたき
        float foreBeat = MathHelper.sin(t * 2.45F) * 0.62F;

        // 後翅は位相をずらす
        float hindBeat = MathHelper.sin(t * 2.45F + 0.42F) * 0.53F;

        this.leftForeWing.roll = -0.10F - foreBeat;
        this.rightForeWing.roll = 0.10F + foreBeat;

        this.leftHindWing.roll = -0.08F + hindBeat;
        this.rightHindWing.roll = 0.08F - hindBeat;

        this.leftForeWing.yaw = -0.08F - foreBeat * 0.10F;
        this.rightForeWing.yaw = 0.08F + foreBeat * 0.10F;

        this.leftHindWing.yaw = 0.18F + hindBeat * 0.08F;
        this.rightHindWing.yaw = -0.18F - hindBeat * 0.08F;

        // 翼端
        this.leftForeTip.roll = -foreBeat * 0.30F;
        this.rightForeTip.roll = foreBeat * 0.30F;

        this.leftHindTip.roll = hindBeat * 0.28F;
        this.rightHindTip.roll = -hindBeat * 0.28F;

        // 胸部の上下動
        this.thorax.pivotY = MathHelper.sin(t * 0.20F) * 0.06F;

        // 腹部の揺れ
        this.abdomen.pitch = MathHelper.sin(t * 0.17F) * 0.035F;

        // 触角の動き
        this.leftAntenna.roll =
                -0.28F + MathHelper.sin(t * 0.32F) * 0.08F;

        this.rightAntenna.roll =
                0.28F - MathHelper.sin(t * 0.32F + 0.5F) * 0.08F;

        // 頭部の向き
        this.head.yaw = MathHelper.clamp(
                headYaw * ((float) Math.PI / 180.0F) * 0.18F,
                -0.22F,
                0.22F
        );

        this.head.pitch = MathHelper.clamp(
                headPitch * ((float) Math.PI / 180.0F) * 0.12F,
                -0.18F,
                0.18F
        );

        // 6本の脚の微細な運動
        for (int i = 0; i < this.legs.length; i++) {
            float phase =
                    (i / 2) * 2.1F
                    + (i % 2 == 0 ? 0.0F : (float) Math.PI);

            this.legs[i].pitch =
                    MathHelper.sin(t * 0.65F + phase) * 0.045F;
        }
    }
}
