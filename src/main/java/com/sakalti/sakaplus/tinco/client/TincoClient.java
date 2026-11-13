package com.sakalti.sakaplus.tinco.client;

import com.sakalti.sakaplus.tinco.client.AssembledToolRenderer;
import com.sakalti.sakaplus.tinco.item.ModItems5;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

public class TincoClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BuiltinItemRendererRegistry.INSTANCE.register(ModItems5.ASSEMBLED_TOOL, new AssembledToolRenderer());
    }
}
