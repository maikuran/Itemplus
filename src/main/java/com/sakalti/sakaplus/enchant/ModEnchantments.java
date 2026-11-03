package com.sakalti.sakaplus.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEnchantments {

    public static final Enchantment MAGNUM =
            Registry.register(
                    Registry.ENCHANTMENT,
                    new Identifier("sakaplus", "magnum"),
                    new MagnumEnchantment()
            );

    public static void register() {
        System.out.println("Sakaplus: Magnum Enchantment Registered!");
    }
}
