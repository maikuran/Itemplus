
package com.sakalti.sakacountry.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class CutlassItem extends Item {

    public CutlassItem(
            ToolMaterial material,
            float damage,
            float attackSpeed,
            Properties properties
    ) {
        super(properties.sword(
                material,
                damage,
                attackSpeed
        ));
    }
}
