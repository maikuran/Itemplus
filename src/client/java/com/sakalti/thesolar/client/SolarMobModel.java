package com.sakalti.thesolar.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public final class SolarMobModel extends EntityModel<SolarMobRenderState> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public SolarMobModel(ModelPart root) {
        super(root);
        head = root.getChild("head");
        body = root.getChild("body");
        leftArm = root.getChild("left_arm");
        rightArm = root.getChild("right_arm");
        leftLeg = root.getChild("left_leg");
        rightLeg = root.getChild("right_leg");
    }

    public static LayerDefinition createLayer() {
        return createLayer("lunar_crawler");
    }

    /** Builds a dedicated silhouette and proportions for each creature. */
    public static LayerDefinition createLayer(String kind) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        boolean quadruped = kind.equals("lunar_crawler") || kind.equals("moon_stalker")
                || kind.equals("red_stalker") || kind.equals("moon_beast")
                || kind.equals("blood_wyrm") || kind.equals("martian_beast");
        boolean wisp = kind.equals("astral_wisp");
        boolean golem = kind.equals("crystal_golem") || kind.equals("martian_brute");
        boolean boss = kind.equals("selene") || kind.equals("ares");

        float headW = boss ? 7 : golem ? 8 : wisp ? 4 : quadruped ? 6 : 8;
        float headH = boss ? 8 : golem ? 7 : wisp ? 4 : quadruped ? 5 : kind.equals("crimson_mage") ? 9 : 8;
        float headD = quadruped ? 7 : wisp ? 4 : 8;
        float bodyW = boss ? 10 : golem ? 12 : wisp ? 9 : quadruped ? 8 : 8;
        float bodyH = boss ? 14 : golem ? 13 : wisp ? 9 : quadruped ? 7 : kind.equals("crimson_mage") ? 13 : 12;
        float bodyD = quadruped ? (kind.equals("blood_wyrm") ? 15 : 12) : wisp ? 9 : 5;
        float limbW = boss ? 4 : golem ? 5 : wisp ? 1 : quadruped ? 3 : 4;
        float limbH = quadruped ? 7 : wisp ? 5 : golem ? 10 : boss ? 13 : 12;
        float limbD = quadruped ? 3 : wisp ? 1 : golem ? 5 : 4;

        CubeListBuilder head = CubeListBuilder.create().texOffs(0, 0)
                .addBox(-headW / 2, -headH, -headD / 2, headW, headH, headD);
        if (quadruped) {
            head = head.texOffs(32, 0).addBox(-2, -2, -headD / 2 - 3, 4, 3, 3);
        } else if (kind.equals("ares") || kind.equals("ares_soldier")) {
            head = head.texOffs(32, 0).addBox(-1, -headH - 4, -2, 2, 5, 3)
                    .texOffs(40, 0).addBox(-5, -headH + 1, -1, 2, 4, 2)
                    .texOffs(40, 0).addBox(3, -headH + 1, -1, 2, 4, 2);
        } else if (kind.equals("selene") || kind.equals("lunar_sentinel")) {
            head = head.texOffs(32, 0).addBox(-1, -headH - 5, -1, 2, 5, 2)
                    .texOffs(40, 0).addBox(-5, -headH + 1, -1, 2, 3, 2)
                    .texOffs(40, 0).addBox(3, -headH + 1, -1, 2, 3, 2);
        } else if (wisp) {
            head = head.texOffs(32, 0).addBox(-1, -headH - 3, -1, 2, 3, 2);
        }
        root.addOrReplaceChild("head", head, PartPose.offset(0, quadruped ? 7 : 0, quadruped ? -5 : 0));

        CubeListBuilder body = CubeListBuilder.create().texOffs(0, 16)
                .addBox(-bodyW / 2, 0, -bodyD / 2, bodyW, bodyH, bodyD);
        if (golem || boss || kind.equals("ancient_automaton")) {
            body = body.texOffs(32, 16).addBox(-bodyW / 2 - 2, 1, -bodyD / 2 - 1, 3, 5, bodyD + 2)
                    .texOffs(32, 16).addBox(bodyW / 2 - 1, 1, -bodyD / 2 - 1, 3, 5, bodyD + 2);
        }
        if (quadruped && (kind.equals("blood_wyrm") || kind.equals("moon_beast"))) {
            body = body.texOffs(48, 16).addBox(-2, 2, bodyD / 2 - 1, 4, 4, 7);
        }
        root.addOrReplaceChild("body", body, PartPose.offset(0, quadruped ? 8 : 0, quadruped ? 0 : 0));

        CubeListBuilder leftArm = CubeListBuilder.create().texOffs(24, 16)
                .addBox(-limbW / 2, -2, -limbD / 2, limbW, limbH, limbD);
        CubeListBuilder rightArm = CubeListBuilder.create().texOffs(40, 16)
                .addBox(-limbW / 2, -2, -limbD / 2, limbW, limbH, limbD);
        root.addOrReplaceChild("left_arm", leftArm, PartPose.offset(quadruped ? 5 : bodyW / 2 + 1, quadruped ? 8 : 2, 0));
        root.addOrReplaceChild("right_arm", rightArm, PartPose.offset(quadruped ? -5 : -bodyW / 2 - 1, quadruped ? 8 : 2, 0));

        CubeListBuilder leg = CubeListBuilder.create().texOffs(0, 32)
                .addBox(-limbW / 2, 0, -limbD / 2, limbW, limbH, limbD);
        CubeListBuilder otherLeg = CubeListBuilder.create().texOffs(16, 32)
                .addBox(-limbW / 2, 0, -limbD / 2, limbW, limbH, limbD);
        root.addOrReplaceChild("left_leg", leg, PartPose.offset(quadruped ? 3 : 2, quadruped ? 8 : bodyH, quadruped ? -4 : 0));
        root.addOrReplaceChild("right_leg", otherLeg, PartPose.offset(quadruped ? -3 : -2, quadruped ? 8 : bodyH, quadruped ? 4 : 0));

        if (quadruped) {
            root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(48, 32)
                    .addBox(-1.5F, -1.5F, 0, 3, 3, kind.equals("blood_wyrm") ? 12 : 8),
                    PartPose.offset(0, 10, bodyD / 2 - 1));
        }
        if (golem || boss) {
            root.addOrReplaceChild("crest_left", CubeListBuilder.create().texOffs(48, 0)
                    .addBox(-2, -7, -2, 4, 7, 4), PartPose.offset(-bodyW / 2 - 1, 2, 0));
            root.addOrReplaceChild("crest_right", CubeListBuilder.create().texOffs(48, 0)
                    .addBox(-2, -7, -2, 4, 7, 4), PartPose.offset(bodyW / 2 + 1, 2, 0));
        }
        if (wisp) {
            root.addOrReplaceChild("tendril_left", CubeListBuilder.create().texOffs(48, 32)
                    .addBox(-1, 0, -1, 2, 10, 2), PartPose.offset(-3, 3, 0));
            root.addOrReplaceChild("tendril_right", CubeListBuilder.create().texOffs(48, 32)
                    .addBox(-1, 0, -1, 2, 10, 2), PartPose.offset(3, 3, 0));
        }
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(SolarMobRenderState state) {
        head.xRot = state.xRot * Mth.DEG_TO_RAD;
        head.yRot = state.yRot * Mth.DEG_TO_RAD;
        float walk = state.walkAnimationPos;
        float speed = state.walkAnimationSpeed;
        leftLeg.xRot = Mth.cos(walk * 0.6662F) * 1.25F * speed;
        rightLeg.xRot = Mth.cos(walk * 0.6662F + Mth.PI) * 1.25F * speed;
        leftArm.xRot = Mth.cos(walk * 0.6662F + Mth.PI) * 0.85F * speed;
        rightArm.xRot = Mth.cos(walk * 0.6662F) * 0.85F * speed;
        body.xRot = speed > 0.1F ? 0.04F : 0.0F;

        if (state.attackType > 0) {
            float windup = 1.0F - Mth.clamp(state.attackTicks / 30.0F, 0.0F, 1.0F);
            float lift = 0.35F + windup * 0.85F;
            switch (state.attackType) {
                case 1 -> { leftArm.xRot = -lift; rightArm.xRot = -lift * 1.25F; body.xRot = -0.12F; }
                case 2 -> { leftArm.xRot = -1.15F - windup * 0.35F; rightArm.xRot = -1.15F - windup * 0.35F; body.xRot = -0.22F; }
                case 3 -> { leftArm.xRot = -1.8F; rightArm.xRot = -1.8F; body.xRot = -0.08F; }
                case 4 -> { leftArm.xRot = -2.1F + windup * 0.25F; rightArm.xRot = -2.1F + windup * 0.25F; body.xRot = 0.18F; }
                case 5 -> { rightArm.xRot = -2.35F + windup * 0.35F; leftArm.xRot = -0.55F; body.xRot = 0.12F; }
                default -> { }
            }
        }
    }
}
