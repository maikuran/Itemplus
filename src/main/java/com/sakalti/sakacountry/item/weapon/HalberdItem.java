package com.sakalti.sakacountry.item.weapon;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class HalberdItem extends Item {

    public HalberdItem(
            ToolMaterial material,
            float damage,
            float attackSpeed,
            Properties properties
    ) {
        super(properties.sword(
                material,
                damage,
                attackSpeed
        ).attributes(
                ItemAttributeModifiers.builder()
                        .add(
                                Attributes.ENTITY_INTERACTION_RANGE,
                                new AttributeModifier(
                                        BASE_ATTACK_RANGE_ID,
                                        1.5,
                                        Operation.ADD_VALUE
                                ),
                                ItemAttributeModifiers.EquippableSlotGroup.MAINHAND
                        )
                        .build()
        ));
    }

    private static final net.minecraft.resources.Identifier BASE_ATTACK_RANGE_ID =
            net.minecraft.resources.Identifier.fromNamespaceAndPath(
                    "sakacountry",
                    "halberd_reach"
            );
}
