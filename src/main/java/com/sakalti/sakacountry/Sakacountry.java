
package com.sakalti.sakacountry;

import net.fabricmc.api.ModInitializer;

import com.sakalti.sakacountry.item.ModWeapons;

public class Sakacountry implements ModInitializer {

    public static final String MOD_ID = "sakacountry";

    @Override
    public void onInitialize() {
        ModWeapons.initialize();
    }
}
