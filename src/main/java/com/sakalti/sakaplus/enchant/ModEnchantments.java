
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

    public static final Enchantment POISON =
            Registry.register(
                    Registry.ENCHANTMENT,
                    new Identifier("sakaplus", "poison"),
                    new PoisonEnchantment()
            );

    public static final Enchantment SWEEP_BURST =
            Registry.register(
                    Registry.ENCHANTMENT,
                    new Identifier("sakaplus", "sweep_burst"),
                    new SweepBurstEnchantment()
            );

    // 経験値増加エンチャント
    public static final Enchantment EXPERIENCE =
            Registry.register(
                    Registry.ENCHANTMENT,
                    new Identifier("sakaplus", "experience"),
                    new ExperienceEnchant()
            );

    public static void register() {
        System.out.println("Sakaplus: Enchantments Registered!");
    }
}
