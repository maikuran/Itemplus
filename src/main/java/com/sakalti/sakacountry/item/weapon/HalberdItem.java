
package com.sakalti.sakacountry.item.weapon;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class HalberdItem extends Item {

    public HalberdItem(
            ToolMaterial material,
            float damage,
            float attackSpeed,
            Properties properties
    ) {
        super(material.applySwordProperties(
                properties,
                damage,
                attackSpeed
        ).attributes(
                ItemAttributeModifiers.builder()
                        .add(
                                Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(
                                        Identifier.fromNamespaceAndPath(
                                                "minecraft",
                                                "base_attack_damage"
                                        ),
                                        damage + material.attackDamageBonus(),
                                        AttributeModifier.Operation.ADD_VALUE
                                ),
                                EquipmentSlotGroup.MAINHAND
                        )
                        .add(
                                Attributes.ATTACK_SPEED,
                                new AttributeModifier(
                                        Identifier.fromNamespaceAndPath(
                                                "minecraft",
                                                "base_attack_speed"
                                        ),
                                        attackSpeed,
                                        AttributeModifier.Operation.ADD_VALUE
                                ),
                                EquipmentSlotGroup.MAINHAND
                        )
                        .add(
                                Attributes.ENTITY_INTERACTION_RANGE,
                                new AttributeModifier(
                                        Identifier.fromNamespaceAndPath(
                                                "sakacountry",
                                                "halberd_reach"
                                        ),
                                        1.5,
                                        AttributeModifier.Operation.ADD_VALUE
                                ),
                                EquipmentSlotGroup.MAINHAND
                        )
                        .build()
        ));
    }
}
