package com.sakalti.sakaplus.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SakaplusSounds {

    public static final SoundEvent Shoot = register("shoot");
    public static final SoundEvent Magazine = register("magazine");

    // クルジ・アント（kruzi_ant）用のサウンドイベントを追加
    public static final SoundEvent KruziantAmbient = register("kruziant_ambient");
    public static final SoundEvent KruziantHurt = register("kruziant_hurt");
    public static final SoundEvent KruziantDeath = register("kruziant_death");

    private static SoundEvent register(String name) {
        Identifier id = new Identifier("sakaplus", name);
        return Registry.register(
            Registry.SOUND_EVENT,
            id,
            new SoundEvent(id)
        );
    }

    public static void registerSounds() {
        // クラスの初期化によって登録
    }
}
