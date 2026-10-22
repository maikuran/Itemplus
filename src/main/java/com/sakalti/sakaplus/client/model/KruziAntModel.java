package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.KruziAntEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

/**
 * KruziAnt model for Minecraft 1.19.2 (Fabric/Yarn).
 * Every cuboid has its own explicitly allocated box-UV rectangle; no shared or
 * accidentally overlapping UV origins. The paired wing roots flap around the
 * shoulder axis and the distal wing sections lag behind for a layered motion.
 */
public final class KruziAntModel extends EntityModel<KruziAntEntity> {
    public static final EntityModelLayer LAYER = new EntityModelLayer(new Identifier("sakaplus", "kruzi_ant"), "main");

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
    private final ModelPart[] legs;

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
        this.legs = new ModelPart[] {
            root.getChild("leg_front_left"), root.getChild("leg_front_right"),
            root.getChild("leg_mid_left"), root.getChild("leg_mid_right"),
            root.getChild("leg_rear_left"), root.getChild("leg_rear_right")
        };
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData r = data.getRoot();

        // Thorax: layered chitin plates. Each cuboid's UV origin is unique.
        ModelPartData thorax = r.addChild("thorax", ModelPartBuilder.create(), ModelTransform.pivot(0, 0, 0));
        thorax.addChild("thorax_core", ModelPartBuilder.create().uv(0, 0).cuboid(-2.5F, -2.5F, -3.0F, 5, 5, 6), ModelTransform.NONE);
        thorax.addChild("dorsal_plate", ModelPartBuilder.create().uv(24, 0).cuboid(-2.0F, -3.0F, -2.5F, 4, 1, 5), ModelTransform.NONE);
        thorax.addChild("front_collar", ModelPartBuilder.create().uv(44, 0).cuboid(-2.0F, -1.8F, -4.0F, 4, 3, 1.5F), ModelTransform.NONE);
        thorax.addChild("rear_collar", ModelPartBuilder.create().uv(44, 10).cuboid(-1.8F, -1.5F, 2.6F, 3.6F, 2.6F, 1.4F), ModelTransform.NONE);
        thorax.addChild("left_shoulder", ModelPartBuilder.create().uv(0, 17).cuboid(2.0F, -1.7F, -2.0F, 1.8F, 2.4F, 2.2F), ModelTransform.NONE);
        thorax.addChild("right_shoulder", ModelPartBuilder.create().uv(16, 17).cuboid(-3.8F, -1.7F, -2.0F, 1.8F, 2.4F, 2.2F), ModelTransform.NONE);

        // Head faces toward negative Z; eye sockets and lenses have independent UV boxes.
        ModelPartData head = r.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0, -0.1F, -4.0F));
        head.addChild("head_capsule", ModelPartBuilder.create().uv(32, 18).cuboid(-2.5F, -2.2F, -3.0F, 5, 4.4F, 3), ModelTransform.NONE);
        head.addChild("clypeus", ModelPartBuilder.create().uv(52, 18).cuboid(-1.5F, -1.1F, -3.5F, 3, 2, 0.7F), ModelTransform.NONE);
        head.addChild("left_eye_socket", ModelPartBuilder.create().uv(0, 29).cuboid(1.8F, -1.5F, -2.8F, 1.1F, 2.5F, 1.1F), ModelTransform.NONE);
        head.addChild("right_eye_socket", ModelPartBuilder.create().uv(8, 29).cuboid(-2.9F, -1.5F, -2.8F, 1.1F, 2.5F, 1.1F), ModelTransform.NONE);
        head.addChild("left_eye_glow", ModelPartBuilder.create().uv(16, 29).cuboid(2.05F, -1.0F, -3.0F, 0.6F, 1.5F, 0.5F), ModelTransform.NONE);
        head.addChild("right_eye_glow", ModelPartBuilder.create().uv(20, 29).cuboid(-2.65F, -1.0F, -3.0F, 0.6F, 1.5F, 0.5F), ModelTransform.NONE);
        ModelPartData leftAnt = head.addChild("left_antenna", ModelPartBuilder.create(), ModelTransform.pivot(1.2F, -1.8F, -2.0F));
        leftAnt.addChild("stem", ModelPartBuilder.create().uv(26, 29).cuboid(-0.25F, -2.0F, -0.2F, 0.5F, 2.2F, 0.5F), ModelTransform.of(0, 0, 0, 0, 0, -0.35F));
        leftAnt.addChild("club", ModelPartBuilder.create().uv(30, 29).cuboid(-0.45F, -2.6F, -0.35F, 0.9F, 0.9F, 0.8F), ModelTransform.of(0, -1.7F, 0, 0, 0, -0.5F));
        ModelPartData rightAnt = head.addChild("right_antenna", ModelPartBuilder.create(), ModelTransform.pivot(-1.2F, -1.8F, -2.0F));
        rightAnt.addChild("stem", ModelPartBuilder.create().uv(35, 29).cuboid(-0.25F, -2.0F, -0.2F, 0.5F, 2.2F, 0.5F), ModelTransform.of(0, 0, 0, 0, 0, 0.35F));
        rightAnt.addChild("club", ModelPartBuilder.create().uv(39, 29).cuboid(-0.45F, -2.6F, -0.35F, 0.9F, 0.9F, 0.8F), ModelTransform.of(0, -1.7F, 0, 0, 0, 0.5F));

        // Segmented abdomen with a small terminal point; all parts remain attached to the body axis.
        ModelPartData abdomen = r.addChild("abdomen", ModelPartBuilder.create(), ModelTransform.pivot(0, 0.2F, 3.2F));
        abdomen.addChild("petiole", ModelPartBuilder.create().uv(48, 29).cuboid(-1.0F, -1.0F, -0.5F, 2, 2, 2.4F), ModelTransform.NONE);
        abdomen.addChild("gaster", ModelPartBuilder.create().uv(0, 40).cuboid(-2.5F, -2.3F, 1.0F, 5, 4.6F, 6), ModelTransform.NONE);
        abdomen.addChild("gaster_dorsal", ModelPartBuilder.create().uv(24, 40).cuboid(-1.7F, -2.6F, 1.7F, 3.4F, 0.7F, 4.3F), ModelTransform.NONE);
        abdomen.addChild("terminal_spine", ModelPartBuilder.create().uv(42, 40).cuboid(-0.65F, -0.45F, 6.5F, 1.3F, 1.2F, 1.8F), ModelTransform.NONE);

        // Six segmented legs, each with a coxa, femur, and tibia. Rotations are local to the thorax.
        addLeg(r, "leg_front_left", 2.0F, -0.3F, -2.0F, 0, 94, -0.62F, 0.65F, 0, -0.48F, 0.95F, 0, -0.16F, 0.75F, 0);
        addLeg(r, "leg_front_right", -2.0F, -0.3F, -2.0F, 40, 94, -0.62F, -0.65F, 0, 0.48F, -0.95F, 0, 0.16F, -0.75F, 0);
        addLeg(r, "leg_mid_left", 2.3F, 0.0F, 0.0F, 80, 94, -0.08F, 0.62F, 0, 0.05F, 1.0F, 0, 0.05F, 0.85F, 0);
        addLeg(r, "leg_mid_right", -2.3F, 0.0F, 0.0F, 0, 110, -0.08F, -0.62F, 0, 0.05F, -1.0F, 0, 0.05F, -0.85F, 0);
        addLeg(r, "leg_rear_left", 2.0F, 0.1F, 2.0F, 40, 110, 0.52F, 0.58F, 0, 0.65F, 0.9F, 0, 0.12F, 0.8F, 0);
        addLeg(r, "leg_rear_right", -2.0F, 0.1F, 2.0F, 80, 110, 0.52F, -0.58F, 0, 0.65F, -0.9F, 0, 0.12F, -0.8F, 0);

        // Four broad, thin, translucent insect wings. Roots are at the thorax's dorsal shoulder line.
        // Each membrane is a cuboid slab with a unique box UV allocation; the texture supplies veins.
        ModelPartData lfw = r.addChild("left_fore_wing", ModelPartBuilder.create(), ModelTransform.pivot(2.2F, -2.0F, -1.7F));
        lfw.addChild("fore_membrane", ModelPartBuilder.create().uv(64, 0).cuboid(0.0F, -0.12F, -4.2F, 7.2F, 0.24F, 5.0F), ModelTransform.of(0, 0, 0, 0, -0.10F, -0.04F));
        lfw.addChild("fore_vein_ridge", ModelPartBuilder.create().uv(64, 12).cuboid(0.15F, -0.18F, -1.8F, 6.8F, 0.12F, 0.28F), ModelTransform.of(0, 0, 0, 0, -0.10F, -0.04F));
        ModelPartData lft = lfw.addChild("tip", ModelPartBuilder.create(), ModelTransform.pivot(6.4F, 0, -2.0F));
        lft.addChild("tip_membrane", ModelPartBuilder.create().uv(64, 18).cuboid(0, -0.08F, -1.8F, 2.2F, 0.16F, 2.2F), ModelTransform.of(0, 0, 0, 0, -0.04F, -0.025F));

        ModelPartData rfw = r.addChild("right_fore_wing", ModelPartBuilder.create(), ModelTransform.pivot(-2.2F, -2.0F, -1.7F));
        rfw.addChild("fore_membrane", ModelPartBuilder.create().uv(64, 23).cuboid(-7.2F, -0.12F, -4.2F, 7.2F, 0.24F, 5.0F), ModelTransform.of(0, 0, 0, 0, 0.10F, 0.04F));
        rfw.addChild("fore_vein_ridge", ModelPartBuilder.create().uv(64, 35).cuboid(-6.95F, -0.18F, -1.8F, 6.8F, 0.12F, 0.28F), ModelTransform.of(0, 0, 0, 0, 0.10F, 0.04F));
        ModelPartData rft = rfw.addChild("tip", ModelPartBuilder.create(), ModelTransform.pivot(-6.4F, 0, -2.0F));
        rft.addChild("tip_membrane", ModelPartBuilder.create().uv(64, 41).cuboid(-2.2F, -0.08F, -1.8F, 2.2F, 0.16F, 2.2F), ModelTransform.of(0, 0, 0, 0, 0.04F, 0.025F));

        ModelPartData lhw = r.addChild("left_hind_wing", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, -1.7F, 1.0F));
        lhw.addChild("hind_membrane", ModelPartBuilder.create().uv(64, 47).cuboid(0, -0.1F, -0.2F, 5.7F, 0.2F, 5.1F), ModelTransform.of(0, 0, 0, 0, 0.20F, -0.08F));
        lhw.addChild("hind_vein_ridge", ModelPartBuilder.create().uv(64, 59).cuboid(0.1F, -0.16F, 2.0F, 5.3F, 0.12F, 0.25F), ModelTransform.of(0, 0, 0, 0, 0.20F, -0.08F));
        ModelPartData lht = lhw.addChild("tip", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 0, 3.2F));
        lht.addChild("tip_membrane", ModelPartBuilder.create().uv(64, 65).cuboid(0, -0.07F, -0.4F, 1.8F, 0.14F, 1.8F), ModelTransform.of(0, 0, 0, 0, 0.06F, -0.04F));

        ModelPartData rhw = r.addChild("right_hind_wing", ModelPartBuilder.create(), ModelTransform.pivot(-2.0F, -1.7F, 1.0F));
        rhw.addChild("hind_membrane", ModelPartBuilder.create().uv(64, 70).cuboid(-5.7F, -0.1F, -0.2F, 5.7F, 0.2F, 5.1F), ModelTransform.of(0, 0, 0, 0, -0.20F, 0.08F));
        rhw.addChild("hind_vein_ridge", ModelPartBuilder.create().uv(64, 82).cuboid(-5.4F, -0.16F, 2.0F, 5.3F, 0.12F, 0.25F), ModelTransform.of(0, 0, 0, 0, -0.20F, 0.08F));
        ModelPartData rht = rhw.addChild("tip", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 0, 3.2F));
        rht.addChild("tip_membrane", ModelPartBuilder.create().uv(64, 88).cuboid(-1.8F, -0.07F, -0.4F, 1.8F, 0.14F, 1.8F), ModelTransform.of(0, 0, 0, 0, -0.06F, 0.04F));

        return TexturedModelData.of(data, 128, 128);
    }

    private static void addLeg(ModelPartData root, String name, float x, float y, float z, int uvX, int uvY,
                               float coxaZ, float coxaX, float coxaY,
                               float femurZ, float femurX, float femurY,
                               float tibiaZ, float tibiaX, float tibiaY) {
        // Separate articulated segments; each receives its own box-UV region.
        ModelPartData leg = root.addChild(name, ModelPartBuilder.create(), ModelTransform.pivot(x, y, z));
        leg.addChild("coxa", ModelPartBuilder.create().uv(uvX, uvY).cuboid(-0.45F, -0.45F, -0.3F, 0.9F, 0.9F, 1.3F), ModelTransform.of(0, 0, 0, 0, coxaY, coxaZ));
        leg.addChild("femur", ModelPartBuilder.create().uv(uvX, uvY + 7).cuboid(-0.35F, -0.35F, 0.7F, 0.7F, 0.7F, 2.4F), ModelTransform.of(0, 0, 0, femurY, femurX, femurZ));
        leg.addChild("tibia", ModelPartBuilder.create().uv(uvX + 12, uvY + 7).cuboid(-0.22F, -0.22F, 2.6F, 0.44F, 0.44F, 3.2F), ModelTransform.of(0, 0, 0, tibiaY, tibiaX, tibiaZ));
        leg.addChild("tarsus", ModelPartBuilder.create().uv(uvX + 20, uvY + 7).cuboid(-0.14F, -0.14F, 5.5F, 0.28F, 0.28F, 1.6F), ModelTransform.NONE);
    }

    @Override
    public void setAngles(KruziAntEntity entity, float limbAngle, float limbDistance, float animationProgress,
                          float headYaw, float headPitch) {
        float t = animationProgress;
        // Fast, readable wing beat. Forewings and hindwings are slightly phase-shifted,
        // with the distal sections lagging to suggest membrane inertia.
        float beat = MathHelper.sin(t * 2.45F) * 0.62F;
        float hindBeat = MathHelper.sin(t * 2.45F + 0.42F) * 0.53F;
        leftForeWing.roll = -0.10F - beat;
        rightForeWing.roll = 0.10F + beat;
        leftHindWing.roll = -0.08F + hindBeat;
        rightHindWing.roll = 0.08F - hindBeat;
        leftForeWing.yaw = -0.08F - beat * 0.10F;
        rightForeWing.yaw = 0.08F + beat * 0.10F;
        leftHindWing.yaw = 0.18F + hindBeat * 0.08F;
        rightHindWing.yaw = -0.18F - hindBeat * 0.08F;
        leftForeTip.roll = -beat * 0.30F;
        rightForeTip.roll = beat * 0.30F;
        leftHindTip.roll = hindBeat * 0.28F;
        rightHindTip.roll = -hindBeat * 0.28F;
        thorax.pivotY = MathHelper.sin(t * 0.20F) * 0.06F;
        abdomen.pitch = MathHelper.sin(t * 0.17F) * 0.035F;
        leftAntenna.roll = -0.28F + MathHelper.sin(t * 0.32F) * 0.08F;
        rightAntenna.roll = 0.28F - MathHelper.sin(t * 0.32F + 0.5F) * 0.08F;
        head.yaw = MathHelper.clamp(headYaw * ((float)Math.PI / 180F) * 0.18F, -0.22F, 0.22F);
        head.pitch = MathHelper.clamp(headPitch * ((float)Math.PI / 180F) * 0.12F, -0.18F, 0.18F);
        for (int i = 0; i < legs.length; i++) {
            float phase = (i / 2) * 2.1F + (i % 2 == 0 ? 0.0F : (float)Math.PI);
            legs[i].pitch = MathHelper.sin(t * 0.65F + phase) * 0.045F;
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay,
                       float red, float green, float blue, float alpha) {
        root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
