
package com.sakalti.sakaplus.registry;

import java.util.HashSet;
import java.util.Set;

import com.sakalti.sakaplus.potion.LongtimePotionItem;
import com.sakalti.sakaplus.effect.ModEffects;

import net.minecraft.potion.Potion;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public final class LongtimePotionRegistry {
    private static final Set<Identifier> REGISTERED = new HashSet<>();

    private LongtimePotionRegistry() {
    }

    public static void registerAll() {
        for (StatusEffect effect : ModEffects.ALL) {
            String name = effect.getTranslationKey()
                .replace("effect.", "") + "_longtime";

            Identifier id = new Identifier("sakaplus", name);

            // 同じIDをこのクラスから重複登録しない
            if (!REGISTERED.add(id)) {
                continue;
            }

            // ポーションを登録
            if (!Registry.POTION.containsId(id)) {
                Potion potion = new Potion(
                    new StatusEffectInstance(effect, 9999)
                );
                Registry.register(Registry.POTION, id, potion);
            }

            // アイテムは別レジストリに一度だけ登録
            if (!Registry.ITEM.containsId(id)) {
                Item item = new LongtimePotionItem(
                    new Item.Settings()
                        .maxCount(7)
                        .group(ItemGroup.BREWING)
                );
                Registry.register(Registry.ITEM, id, item);
            }
        }
    }
}
