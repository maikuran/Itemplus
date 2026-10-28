package com.sakalti.sakaplus.registry;

import com.sakalti.sakaplus.item.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Weapons {
    public static final Item LIGHTNING_STINGER = new LightningStinger();
    public static final Item LIGHTNING_SPARK = new LightningSpark();
    public static final Item ICE_GREATBLADE = new IceGreatblade();

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "lightning_stinger"), LIGHTNING_STINGER);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "ice_greatblade"), ICE_GREATBLADE);
        Registry.register(Registry.ITEM, new Identifier("sakaplus", "lightning_spark"), LIGHTNING_SPARK);
    }
}
