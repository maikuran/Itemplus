
package com.sakalti.sakaplus.client.model;

import com.sakalti.sakaplus.entity.WarperdunEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class WarperdunModel extends SinglePartEntityModel<WarperdunEntity> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart neck;
    private final ModelPart core;
    private final ModelPart core_ring;
    private final ModelPart portal;

    private final ModelPart horn_l;
    private final ModelPart horn_r;

    private final ModelPart[] ribs = new ModelPart[10];
    private final ModelPart[] upperLegs = new ModelPart[4];
    private final ModelPart[] lowerLegs = new ModelPart[4];
    private final ModelPart[] feet = new ModelPart[4];

    private final ModelPart lantern_l;
    private final ModelPart lantern_r;

    public WarperdunModel(ModelPart root) {
        this.root = root;

        body = root.getChild("body");
        head = root.getChild("head");
        jaw = root.getChild("jaw");
        neck = root.getChild("neck");

        core = root.getChild("core");
        core_ring = root.getChild("core_ring");
        portal = root.getChild("portal");

        horn_l = root.getChild("horn_l");
        horn_r = root.getChild("horn_r");

        lantern_l = root.getChild("lantern_l");
        lantern_r = root.getChild("lantern_r");

        for (int i = 0; i < 10; i++) {
            ribs[i] = root.getChild("rib" + i);
        }

        String[] names = {"front_left", "front_right", "back_left", "back_right"};

        for (int i = 0; i < 4; i++) {
            upperLegs[i] = root.getChild(names[i] + "_upper");
            lowerLegs[i] = root.getChild(names[i] + "_lower");
            feet[i] = root.getChild(names[i] + "_foot");
        }
    }

    public static TexturedModelData getTexturedModelData() {

        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        // =====================================
        // BODY : 巨大な胴体
        // =====================================

        root.addChild("body",
            ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-7F, -11F, -5F, 14, 12, 10)

                // 背中の装甲
                .uv(0, 24)
                .cuboid(-6F, -12F, 1F, 12, 3, 5)

                // 肩の装甲
                .uv(36, 0)
                .cuboid(-9F, -10F, -3F, 3, 5, 7)
                .cuboid(6F, -10F, -3F, 3, 5, 7)

                // 腹部装甲
                .uv(36, 20)
                .cuboid(-5F, -2F, -4F, 10, 2, 8),

            ModelTransform.pivot(0F, 19F, 0F)
        );

        // =====================================
        // NECK : 首
        // =====================================

        root.addChild("neck",
            ModelPartBuilder.create()
                .uv(0, 40)
                .cuboid(-4F, -5F, -4F, 8, 7, 8)
                .uv(32, 40)
                .cuboid(-3F, -6F, -3F, 6, 2, 6),
            ModelTransform.pivot(0F, 9F, -4F)
        );

        // =====================================
        // HEAD : 頭蓋骨
        // =====================================

        root.addChild("head",
            ModelPartBuilder.create()
                .uv(0, 56)
                .cuboid(-4F, -5F, -4F, 8, 8, 7)

                // 額の装甲
                .uv(30, 56)
                .cuboid(-3F, -6F, -4F, 6, 2, 3)

                // 頬の装甲
                .uv(48, 56)
                .cuboid(-5F, -2F, -2F, 2, 4, 4)
                .cuboid(3F, -2F, -2F, 2, 4, 4),

            ModelTransform.pivot(0F, 8F, -8F)
        );

        // =====================================
        // JAW : 下顎
        // =====================================

        root.addChild("jaw",
            ModelPartBuilder.create()
                .uv(0, 70)
                .cuboid(-3F, 0F, -4F, 6, 2, 5)
                .uv(24, 70)
                .cuboid(-2F, -1F, -4F, 4, 1, 2),
            ModelTransform.pivot(0F, 11F, -9F)
        );

        // =====================================
        // HORNS : 巨大な角2本
        // =====================================

        root.addChild("horn_l",
            ModelPartBuilder.create()
                .uv(64, 0)
                .cuboid(-1.5F, -1F, -1.5F, 3, 5, 3)
                .uv(64, 12)
                .cuboid(-1F, -5F, -1F, 2, 5, 2)
                .uv(64, 22)
                .cuboid(-0.5F, -8F, -0.5F, 1, 4, 1),
            ModelTransform.of(-3F, 5F, -8F,
                -0.45F, 0F, -0.35F)
        );

        root.addChild("horn_r",
            ModelPartBuilder.create()
                .uv(76, 0)
                .cuboid(-1.5F, -1F, -1.5F, 3, 5, 3)
                .uv(76, 12)
                .cuboid(-1F, -5F, -1F, 2, 5, 2)
                .uv(76, 22)
                .cuboid(-0.5F, -8F, -0.5F, 1, 4, 1),
            ModelTransform.of(3F, 5F, -8F,
                -0.45F, 0F, 0.35F)
        );

        // =====================================
        // RIB CAGE : 肋骨10本
        // =====================================

        for (int i = 0; i < 10; i++) {

            float y = -9F + i * 0.95F;

            ModelPartBuilder ribBuilder = ModelPartBuilder.create()

                // 左右の肋骨
                .uv(88, 0)
                .cuboid(-7F, y, -5.8F, 2, 1, 2)
                .cuboid(5F, y, -5.8F, 2, 1, 2)

                // 前面の肋骨
                .uv(88, 8)
                .cuboid(-5F, y, -6F, 10, 1, 1)

                // 外側の骨
                .uv(88, 12)
                .cuboid(-8F, y, -3F, 1, 1, 5)
                .cuboid(7F, y, -3F, 1, 1, 5);

            root.addChild("rib" + i,
                ribBuilder,
                ModelTransform.pivot(0F, 19F, 0F)
            );
        }

        // =====================================
        // CORE : 巨大な胸部コア
        // =====================================

        root.addChild("core",
            ModelPartBuilder.create()

                // 大型コア本体
                .uv(0, 82)
                .cuboid(-4F, -4F, -2F, 8, 8, 4)

                // コアの外殻
                .uv(24, 82)
                .cuboid(-5F, -5F, -1F, 10, 10, 2),

            ModelTransform.pivot(0F, 14F, -7F)
        );

        // コアのリング
        root.addChild("core_ring",
            ModelPartBuilder.create()
                .uv(48, 82)
                .cuboid(-6F, -6F, -0.5F, 12, 12, 1),
            ModelTransform.pivot(0F, 14F, -9F)
        );

        // =====================================
        // PORTAL : 背中の巨大ポータル
        // =====================================

        root.addChild("portal",
            ModelPartBuilder.create()

                // 外周フレーム
                .uv(0, 100)
                .cuboid(-6F, -8F, -1F, 12, 16, 2)

                // 内側のフレーム
                .uv(40, 100)
                .cuboid(-4F, -6F, -1.5F, 8, 12, 1)

                // 上部装甲
                .uv(64, 100)
                .cuboid(-7F, -9F, -1F, 14, 2, 3),

            ModelTransform.pivot(0F, 12F, 6F)
        );

        // =====================================
        // LEGS : 4本の多関節脚
        // =====================================

        String[] names = {
            "front_left",
            "front_right",
            "back_left",
            "back_right"
        };

        float[] x = {-6F, 6F, -6F, 6F};
        float[] z = {-3F, -3F, 4F, 4F};

        for (int i = 0; i < 4; i++) {

            // 上部脚
            root.addChild(names[i] + "_upper",
                ModelPartBuilder.create()
                    .uv(80, 40)
                    .cuboid(-2F, 0F, -2F, 4, 6, 4)
                    .uv(96, 40)
                    .cuboid(-2.5F, 1F, -2.5F, 5, 2, 5),
                ModelTransform.pivot(x[i], 17F, z[i])
            );

            // 下部脚
            root.addChild(names[i] + "_lower",
                ModelPartBuilder.create()
                    .uv(80, 52)
                    .cuboid(-1.5F, 0F, -1.5F, 3, 5, 3)
                    .uv(96, 52)
                    .cuboid(-2F, 2F, -2F, 4, 2, 4),
                ModelTransform.pivot(x[i], 23F, z[i])
            );

            // 足先
            root.addChild(names[i] + "_foot",
                ModelPartBuilder.create()
                    .uv(80, 64)
                    .cuboid(-2.5F, 0F, -3F, 5, 2, 6)
                    .uv(104, 64)
                    .cuboid(-1.5F, 1F, -4F, 3, 1, 2),
                ModelTransform.pivot(x[i], 28F, z[i])
            );
        }

        // =====================================
        // LANTERNS : 左右の提灯
        // =====================================

        root.addChild("lantern_l",
            ModelPartBuilder.create()
                .uv(0, 122)
                .cuboid(-2F, -4F, -2F, 4, 5, 4)
                .uv(16, 122)
                .cuboid(-2.5F, -5F, -2.5F, 5, 1, 5)
                .cuboid(-2.5F, 1F, -2.5F, 5, 1, 5),
            ModelTransform.pivot(-8F, 13F, -3F)
        );

        root.addChild("lantern_r",
            ModelPartBuilder.create()
                .uv(32, 122)
                .cuboid(-2F, -4F, -2F, 4, 5, 4)
                .uv(48, 122)
                .cuboid(-2.5F, -5F, -2.5F, 5, 1, 5)
                .cuboid(-2.5F, 1F, -2.5F, 5, 1, 5),
            ModelTransform.pivot(8F, 13F, -3F)
        );

        return TexturedModelData.of(data, 128, 128);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    // =====================================
    // ANIMATIONS
    // =====================================

    @Override
    public void setAngles(
        WarperdunEntity entity,
        float limbAngle,
        float limbDistance,
        float animationProgress,
        float headYaw,
        float headPitch
    ) {

        // 歩行
        float speed = 0.65F;
        float degree = 0.65F;

        for (int i = 0; i < 4; i++) {
            float phase = (i == 0 || i == 3)
                ? 0F : (float)Math.PI;

            float swing = MathHelper.cos(
                limbAngle * speed + phase
            ) * degree * limbDistance;

            upperLegs[i].pitch = swing;
            lowerLegs[i].pitch =
                Math.max(0F, -swing) * 0.8F;

            feet[i].pitch =
                -lowerLegs[i].pitch * 0.5F;
        }

        // 頭部
        head.yaw = headYaw * 0.017453292F;
        head.pitch = headPitch * 0.017453292F;

        // 顎の微振動
        jaw.pitch = MathHelper.sin(animationProgress * 0.08F) * 0.06F;

        // 首
        neck.pitch = MathHelper.sin(animationProgress * 0.04F) * 0.04F;

        // 肋骨の呼吸
        for (int i = 0; i < 10; i++) {
            ribs[i].roll =
                MathHelper.sin(animationProgress * 0.04F + i * 0.15F)
                * 0.012F;
        }

        // コアの鼓動
        float pulse = 1F +
            MathHelper.sin(animationProgress * 0.12F) * 0.06F;

        core.xScale = pulse;
        core.yScale = pulse;
        core.zScale = pulse;

        // コアリング回転
        core_ring.yaw = animationProgress * 0.035F;

        // ポータル回転
        portal.yaw = animationProgress * 0.025F;
        portal.roll = MathHelper.sin(animationProgress * 0.04F) * 0.08F;

        // 提灯の揺れ
        lantern_l.roll =
            MathHelper.sin(animationProgress * 0.08F) * 0.12F;

        lantern_r.roll =
            -MathHelper.sin(animationProgress * 0.08F) * 0.12F;

        // 角のわずかな振動
        horn_l.roll = -0.35F +
            MathHelper.sin(animationProgress * 0.04F) * 0.015F;

        horn_r.roll = 0.35F -
            MathHelper.sin(animationProgress * 0.04F) * 0.015F;
    }
}
