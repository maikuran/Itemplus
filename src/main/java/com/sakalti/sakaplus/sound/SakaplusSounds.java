
package com.sakalti.sakaplus.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SakaplusSounds {

    public static final SoundEvent Shoot = register("shoot");
    public static final SoundEvent Magazine = register("magazine");
    public static final SoundEvent StaffUse = register("staff_use");

    // Kruzi Ant
    public static final SoundEvent KruziantAmbient = register("kruziant_ambient");
    public static final SoundEvent KruziantHurt = register("kruziant_hurt");
    public static final SoundEvent KruziantDeath = register("kruziant_death");

    // Goblin
    public static final SoundEvent GoblinAmbient = register("goblin_ambient");
    public static final SoundEvent GoblinHurt = register("goblin_hurt");
    public static final SoundEvent GoblinDeath = register("goblin_death");

    // Volcanos
    public static final SoundEvent VolcanosAmbient = register("volcanos_ambient");
    public static final SoundEvent VolcanosHurt = register("volcanos_hurt");
    public static final SoundEvent VolcanosDeath = register("volcanos_death");

    // Kryoson
    public static final SoundEvent KryosonAmbient = register("kryoson_ambient");
    public static final SoundEvent KryosonHurt = register("kryoson_hurt");
    public static final SoundEvent KryosonDeath = register("kryoson_death");

    // Arse
    public static final SoundEvent ArseAmbient = register("arse_ambient");
    public static final SoundEvent ArseHurt = register("arse_hurt");
    public static final SoundEvent ArseDeath = register("arse_death");

    // Shyron
    public static final SoundEvent ShyronAmbient = register("shyron_ambient");
    public static final SoundEvent ShyronHurt = register("shyron_hurt");
    public static final SoundEvent ShyronDeath = register("shyron_death");

    // Beast
    public static final SoundEvent BeastAmbient = register("beast_ambient");
    public static final SoundEvent BeastHurt = register("beast_hurt");
    public static final SoundEvent BeastDeath = register("beast_death");

    // Boss BGM
    public static final SoundEvent WarperdunBGM =
            register("warperdunbgm");

    public static final SoundEvent CryopteryxBGM =
            register("cryopteryxbgm");

    public static final SoundEvent VolcanicKingBGM =
            register("volcanickingbgm");

    private static SoundEvent register(String name) {
        Identifier id = new Identifier("sakaplus", name);

        return Registry.register(
                Registry.SOUND_EVENT,
                id,
                new SoundEvent(id)
        );
    }

    public static void registerSounds() {
        // クラス初期化によって全サウンドイベントを登録
    }
}
