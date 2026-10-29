
package com.sakalti.sakaplus.attribute;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModAttributes {

    public static final EntityAttribute ADAPTATION =
            new ClampedEntityAttribute(
                    "attribute.name.generic.adaptation",
                    0.0D,
                    0.0D,
                    1024.0D
            ).setTracked(true);

    private static boolean registered = false;

    public static void register() {
        if (registered) {
            return;
        }

        Registry.register(
                Registry.ATTRIBUTE,
                new Identifier("sakaplus", "adaptation"),
                ADAPTATION
        );

        registered = true;
    }
}
