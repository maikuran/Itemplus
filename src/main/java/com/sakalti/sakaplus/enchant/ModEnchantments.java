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

    // 毒エンチャントの登録を追加
    public static final Enchantment POISON =
            Registry.register(
                    Registry.ENCHANTMENT,
                    new Identifier("sakaplus", "poison"),
                    new PoisonEnchantment()
            );

    public static void register() {
        System.out.println("Sakaplus: Enchantments Registered!");
    }
}
