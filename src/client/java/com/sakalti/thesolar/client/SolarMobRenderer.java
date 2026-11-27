package com.sakalti.thesolar.client;
import com.sakalti.thesolar.DimensionMod;
import com.sakalti.thesolar.entity.SolarHostileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
public final class SolarMobRenderer extends MobRenderer<SolarHostileEntity, SolarMobRenderState, SolarMobModel> {
    private final Identifier texture;
    public SolarMobRenderer(EntityRendererProvider.Context context, String textureName, float shadow) {
        super(context, new SolarMobModel(context.bakeLayer(SolarMobLayers.layer(textureName))), shadow);
        this.texture = DimensionMod.id("textures/entity/" + textureName + ".png");
    }
    @Override public SolarMobRenderState createRenderState() { return new SolarMobRenderState(); }
    @Override public void extractRenderState(SolarHostileEntity entity, SolarMobRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.attackType = entity.getAttackType();
        state.attackTicks = entity.getAttackTicks();
    }
    @Override public Identifier getTextureLocation(SolarMobRenderState state) { return texture; }
}
