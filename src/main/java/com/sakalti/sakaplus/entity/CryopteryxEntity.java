
package com.sakalti.sakaplus.entity;

import java.util.EnumSet;

import com.sakalti.sakaplus.sound.SakaplusSounds;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CryopteryxEntity extends HostileEntity {

    // =====================================
    // Basic Settings
    // =====================================

    private static final double MAX_HEALTH = 1800.0D;

    private static final double BEAM_RANGE = 20.0D;
    private static final float BEAM_DAMAGE = 17.5F;
    private static final float BEAM_SPEED = 9.6F;

    private static final int NORMAL_BEAM_INTERVAL = 20;
    private static final int PHASE2_BEAM_INTERVAL = 10;

    private static final int ICE_BEAM_COLOR = 0x66CCFF;

    // =====================================
    // Frost Nova
    // =====================================

    private static final int NOVA_INTERVAL = 160;
    private static final double NOVA_RANGE = 6.0D;
    private static final float NOVA_DAMAGE = 12.0F;

    private static final int NOVA_SLOW_DURATION = 100;

    // =====================================
    // Phase 2
    // =====================================

    private static final double PHASE2_HEALTH = 0.50D;
    private static final double PHASE2_ARMOR = 24.0D;

    private boolean phase2 = false;

    // =====================================
    // Ice Armor
    // =====================================

    private static final double ICE_ARMOR_HEALTH = 0.25D;
    private static final double ICE_ARMOR_VALUE = 32.0D;

    private boolean iceArmorActivated = false;

    // =====================================
    // Cooldowns
    // =====================================

    private int beamCooldown = NORMAL_BEAM_INTERVAL;
    private int novaCooldown = NOVA_INTERVAL;

    // =====================================
    // Boss Bar
    // =====================================

    private final ServerBossBar bossBar = new ServerBossBar(
            Text.literal("Cryopteryx"),
            BossBar.Color.BLUE,
            BossBar.Style.PROGRESS
    );

    // =====================================
    // Constructor
    // =====================================

    public CryopteryxEntity(
            EntityType<? extends CryopteryxEntity> type,
            World world
    ) {
        super(type, world);

        this.moveControl = new FlightMoveControl(this, 10, true);

        this.setNoGravity(true);

        this.experiencePoints = 981;
    }

    // =====================================
    // Attributes
    // =====================================

    public static DefaultAttributeContainer.Builder createAttributes() {

        return HostileEntity.createHostileAttributes()

                .add(EntityAttributes.GENERIC_MAX_HEALTH, MAX_HEALTH)

                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.32D)

                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.45D)

                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D)

                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0D)

                .add(EntityAttributes.GENERIC_ARMOR, 12.0D)

                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D);
    }

    // =====================================
    // Goals
    // =====================================

    @Override
    protected void initGoals() {

        this.targetSelector.add(
                1,
                new ActiveTargetGoal<>(
                        this,
                        PlayerEntity.class,
                        true
                )
        );

        // Fire / Lava Avoidance
        this.goalSelector.add(
                0,
                new AvoidFireGoal(this)
        );

        // Ice Beam
        this.goalSelector.add(
                2,
                new CryopteryxBeamGoal(this)
        );

        // Flying
        this.goalSelector.add(
                5,
                new FlyGoal(this, 1.0D)
        );
    }

    // =====================================
    // Boss Bar Tracking
    // =====================================

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {

        super.onStartedTrackingBy(player);

        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {

        super.onStoppedTrackingBy(player);

        this.bossBar.removePlayer(player);
    }

    // =====================================
    // Navigation
    // =====================================

    @Override
    protected EntityNavigation createNavigation(World world) {

        return new BirdNavigation(this, world);
    }

    // =====================================
    // Sounds
    // =====================================

    @Override
    protected SoundEvent getAmbientSound() {

        return SakaplusSounds.ShyronAmbient;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {

        return SakaplusSounds.ShyronHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {

        return SakaplusSounds.ShyronDeath;
    }

    // =====================================
    // Main Tick
    // =====================================

    @Override
    public void tick() {

        super.tick();

        this.setNoGravity(true);

        if (this.getWorld().isClient) {
            return;
        }

        // Cooldowns

        if (beamCooldown > 0) {
            beamCooldown--;
        }

        if (novaCooldown > 0) {
            novaCooldown--;
        }

        // Boss Bar HP

        float healthPercent = this.getHealth() / this.getMaxHealth();

        this.bossBar.setPercent(
                MathHelper.clamp(healthPercent, 0.0F, 1.0F)
        );

        // Phase 2 Activation

        if (!phase2 && healthPercent <= PHASE2_HEALTH) {

            activatePhase2();
        }

        // Ice Armor Activation

        if (!iceArmorActivated
                && healthPercent <= ICE_ARMOR_HEALTH) {

            activateIceArmor();
        }

        // Frost Nova

        if (novaCooldown <= 0 && this.getTarget() != null) {

            LivingEntity target = this.getTarget();

            if (target.isAlive()
                    && this.squaredDistanceTo(target) <= 12.0D * 12.0D) {

                useFrostNova();

                novaCooldown = NOVA_INTERVAL;
            }
        }
    }

    // =====================================
    // Phase 2
    // =====================================

    private void activatePhase2() {

        this.phase2 = true;

        // Faster beam

        this.beamCooldown = 0;

        // Boss bar changes color

        this.bossBar.setColor(BossBar.Color.RED);

        this.bossBar.setName(
                Text.literal("Cryopteryx - AWAKENED")
        );

        // Increase armor

        if (this.getAttributeInstance(
                EntityAttributes.GENERIC_ARMOR
        ) != null) {

            this.getAttributeInstance(
                    EntityAttributes.GENERIC_ARMOR
            ).setBaseValue(PHASE2_ARMOR);
        }

        // Speed increase

        if (this.getAttributeInstance(
                EntityAttributes.GENERIC_MOVEMENT_SPEED
        ) != null) {

            this.getAttributeInstance(
                    EntityAttributes.GENERIC_MOVEMENT_SPEED
            ).setBaseValue(0.42D);
        }

        if (this.getAttributeInstance(
                EntityAttributes.GENERIC_FLYING_SPEED
        ) != null) {

            this.getAttributeInstance(
                    EntityAttributes.GENERIC_FLYING_SPEED
            ).setBaseValue(0.60D);
        }
    }

    // =====================================
    // Ice Armor
    // =====================================

    private void activateIceArmor() {

        this.iceArmorActivated = true;

        if (this.getAttributeInstance(
                EntityAttributes.GENERIC_ARMOR
        ) != null) {

            this.getAttributeInstance(
                    EntityAttributes.GENERIC_ARMOR
            ).setBaseValue(ICE_ARMOR_VALUE);
        }
    }

    // =====================================
    // Ice Beam
    // =====================================

    private void fireIceBeam(LivingEntity target) {

        if (this.getWorld().isClient
                || target == null
                || !target.isAlive()) {
            return;
        }

        BeamAttack.shootAt(
                this,
                target,
                BEAM_DAMAGE,
                BEAM_SPEED,
                ICE_BEAM_COLOR
        );

        beamCooldown = phase2
                ? PHASE2_BEAM_INTERVAL
                : NORMAL_BEAM_INTERVAL;
    }

    // =====================================
    // Frost Nova
    // =====================================

    private void useFrostNova() {

        if (this.getWorld().isClient) {
            return;
        }

        for (Entity entity : this.getWorld().getOtherEntities(
                this,
                this.getBoundingBox().expand(NOVA_RANGE),
                entity -> entity instanceof LivingEntity
                        && entity.isAlive()
                        && !this.isTeammate(entity)
        )) {

            if (!(entity instanceof LivingEntity)) {
                continue;
            }

            LivingEntity living = (LivingEntity) entity;

            living.damage(
                    DamageSource.mob(this),
                    NOVA_DAMAGE
            );

            living.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.SLOWNESS,
                            NOVA_SLOW_DURATION,
                            2
                    )
            );

            living.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.WEAKNESS,
                            60,
                            0
                    )
            );
        }

        // Phase 2: stronger Nova

        if (phase2) {

            for (Entity entity : this.getWorld().getOtherEntities(
                    this,
                    this.getBoundingBox().expand(3.0D),
                    entity -> entity instanceof LivingEntity
                            && entity.isAlive()
                            && !this.isTeammate(entity)
            )) {

                if (entity instanceof LivingEntity) {

                    LivingEntity living = (LivingEntity) entity;

                    living.damage(
                            DamageSource.mob(this),
                            5.0F
                    );
                }
            }
        }

        novaCooldown = NOVA_INTERVAL;
    }

    // =====================================
    // Beam Goal
    // =====================================

    private static class CryopteryxBeamGoal extends Goal {

        private final CryopteryxEntity mob;

        private CryopteryxBeamGoal(CryopteryxEntity mob) {

            this.mob = mob;

            this.setControls(
                    EnumSet.of(Goal.Control.LOOK)
            );
        }

        @Override
        public boolean canStart() {

            LivingEntity target = mob.getTarget();

            if (target == null || !target.isAlive()) {
                return false;
            }

            return mob.squaredDistanceTo(target)
                    <= BEAM_RANGE * BEAM_RANGE
                    && mob.beamCooldown <= 0;
        }

        @Override
        public boolean shouldContinue() {

            LivingEntity target = mob.getTarget();

            if (target == null || !target.isAlive()) {
                return false;
            }

            return mob.squaredDistanceTo(target)
                    <= BEAM_RANGE * BEAM_RANGE;
        }

        @Override
        public void tick() {

            LivingEntity target = mob.getTarget();

            if (target == null || !target.isAlive()) {
                return;
            }

            mob.getLookControl().lookAt(
                    target,
                    30.0F,
                    30.0F
            );

            if (mob.squaredDistanceTo(target)
                    <= BEAM_RANGE * BEAM_RANGE
                    && mob.beamCooldown <= 0) {

                mob.fireIceBeam(target);
            }
        }
    }

    // =====================================
    // Fire / Lava Avoidance Goal
    // =====================================

    private static class AvoidFireGoal extends Goal {

        private final CryopteryxEntity mob;

        private Vec3d escapeTarget;

        private int ticks;

        private AvoidFireGoal(CryopteryxEntity mob) {

            this.mob = mob;

            this.setControls(
                    EnumSet.of(Goal.Control.MOVE)
            );
        }

        @Override
        public boolean canStart() {

            return findEscapePosition();
        }

        @Override
        public boolean shouldContinue() {

            return ticks < 60
                    && !mob.getNavigation().isIdle();
        }

        @Override
        public void start() {

            ticks = 0;

            if (escapeTarget != null) {

                mob.getNavigation().startMovingTo(
                        escapeTarget.x,
                        escapeTarget.y,
                        escapeTarget.z,
                        1.5D
                );
            }
        }

        @Override
        public void tick() {

            ticks++;

            if (ticks % 10 == 0) {

                if (findEscapePosition()) {

                    mob.getNavigation().startMovingTo(
                            escapeTarget.x,
                            escapeTarget.y,
                            escapeTarget.z,
                            1.5D
                    );
                }
            }
        }

        @Override
        public void stop() {

            mob.getNavigation().stop();

            escapeTarget = null;

            ticks = 0;
        }

        // Search dangerous blocks

        private boolean findEscapePosition() {

            BlockPos center = mob.getBlockPos();

            double awayX = 0.0D;
            double awayY = 0.0D;
            double awayZ = 0.0D;

            int dangerCount = 0;

            for (BlockPos pos : BlockPos.iterate(
                    center.add(-5, -3, -5),
                    center.add(5, 3, 5)
            )) {

                BlockState state = mob.getWorld().getBlockState(pos);

                boolean dangerous =
                        state.isOf(Blocks.FIRE)
                        || state.isOf(Blocks.SOUL_FIRE)
                        || state.isOf(Blocks.LAVA)
                        || state.isOf(Blocks.MAGMA_BLOCK)
                        || state.isOf(Blocks.CAMPFIRE)
                        || state.isOf(Blocks.SOUL_CAMPFIRE)
                        || state.getFluidState().isIn(FluidTags.LAVA);

                if (!dangerous) {
                    continue;
                }

                double dx = mob.getX() - (pos.getX() + 0.5D);
                double dy = mob.getY() - (pos.getY() + 0.5D);
                double dz = mob.getZ() - (pos.getZ() + 0.5D);

                double distanceSq = dx * dx + dy * dy + dz * dz;

                if (distanceSq < 0.01D) {
                    continue;
                }

                double distance = Math.sqrt(distanceSq);

                awayX += dx / distance;
                awayY += dy / distance;
                awayZ += dz / distance;

                dangerCount++;
            }

            if (dangerCount == 0) {

                escapeTarget = null;

                return false;
            }

            double length = Math.sqrt(
                    awayX * awayX
                    + awayY * awayY
                    + awayZ * awayZ
            );

            // If surrounded, choose a fallback direction.

            if (length < 0.001D) {

                awayX = Math.cos(mob.getYaw() * Math.PI / 180.0D);
                awayZ = Math.sin(mob.getYaw() * Math.PI / 180.0D);
                awayY = 0.0D;

                length = Math.sqrt(
                        awayX * awayX + awayZ * awayZ
                );
            }

            awayX /= length;
            awayY /= length;
            awayZ /= length;

            // Fly away from the danger.

            escapeTarget = new Vec3d(
                    mob.getX() + awayX * 9.0D,
                    mob.getY() + awayY * 4.0D,
                    mob.getZ() + awayZ * 9.0D
            );

            return true;
        }
    }
}
