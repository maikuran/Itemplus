
package com.sakalti.sakaplus.registry;

import com.mojang.serialization.Lifecycle;

import com.sakalti.sakaplus.potion.LongtimePotionItem;
import com.sakalti.sakaplus.effect.ModEffects;

import net.minecraft.potion.Potion;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class LongtimePotionRegistry {
    public static void registerAll() {
        for (StatusEffect effect : ModEffects.ALL) {
            String name = effect.getTranslationKey()
                .replace("effect.", "") + "_longtime";

            Identifier id = new Identifier("sakaplus", name);

            Potion potion = new Potion(
                new StatusEffectInstance(effect, 9999)
            );

            // ポーション: 既存IDなら同じraw IDで上書き
            if (Registry.POTION.containsId(id)) {
                int rawId = Registry.POTION.getRawId(
                    Registry.POTION.get(id)
                );

                ((MutableRegistry<Potion>) Registry.POTION).set(
                    rawId,
                    RegistryKey.of(Registry.POTION_KEY, id),
                    potion,
                    Lifecycle.stable()
                );
            } else {
                Registry.register(Registry.POTION, id, potion);
            }

            // アイテム: 既存IDなら同じraw IDで上書き
            Item item = new LongtimePotionItem(
                new Item.Settings()
                    .maxCount(7)
                    .group(ItemGroup.BREWING)
            );

            if (Registry.ITEM.containsId(id)) {
                int rawId = Registry.ITEM.getRawId(
                    Registry.ITEM.get(id)
                );

                ((MutableRegistry<Item>) Registry.ITEM).set(
                    rawId,
                    RegistryKey.of(Registry.ITEM_KEY, id),
                    item,
                    Lifecycle.stable()
                );
            } else {
                Registry.register(Registry.ITEM, id, item);
            }
        }
    }
}
