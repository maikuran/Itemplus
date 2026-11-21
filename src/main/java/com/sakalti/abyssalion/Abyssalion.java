
package com.sakalti.abyssalion;

import net.fabricmc.api.ModInitializer;

public class Abyssalion implements ModInitializer {

    public static final String MOD_ID = "abyssalion";
    public static final int WATER_LEVEL = 384;

    @Override
    public void onInitialize() {
        System.out.println("[Abyssalion] Abyssalion initialized!");
        System.out.println("[Abyssalion] Water level: Y=" + WATER_LEVEL);
    }
}
