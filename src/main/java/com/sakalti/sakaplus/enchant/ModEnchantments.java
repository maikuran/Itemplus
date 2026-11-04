package com.sakalti.sakaplus.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.sakalti.sakaplus.enchantment.SweepBurstEnchantment; // エンチャントのパッケージに合わせて適宜調整してください

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

    // 大剣専用エンチャントの登録を追加
    public static final Enchantment SWEEP_BURST =
            Registry.register(
                    Registry.ENCHANTMENT,
                    new Identifier("sakaplus", "sweep_burst"),
                    new SweepBurstEnchantment()
            );

    public static void register() {
        System.out.println("Sakaplus: Enchantments Registered!");
    }
}
