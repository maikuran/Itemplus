
package com.sakalti.sakacountry;

import net.fabricmc.api.ModInitializer;

import com.sakalti.sakacountry.item.ModWeapons;
import com.sakalti.sakacountry.item.ModCreativeTabs;
import com.sakalti.sakacountry.nation.command.NationCommands;

public class Sakacountry implements ModInitializer {

    public static final String MOD_ID = "sakacountry";

    @Override
    public void onInitialize() {
        ModWeapons.initialize();
        ModCreativeTabs.initialize();

        NationCommands.register();

        System.out.println("[Sakacountry] Nation System initialized.");
    }
}
