@Override
public void onInitialize() {

    // Kruzi Rifle Server Reload
    KruziRifle.registerServer();

    // Sounds
    SakaplusSounds.registerSounds();

    // Potions
    LongtimePotionRegistry.registerAll();

    // Blocks
    ModBlocks2.registerBlocks();

    // Effects
    ModEffects.registerEffects();

    // Items
    CatalystItems.registerCatalysts();
    Weapons.registerItems();
    ModItems.register();
    ModItems5.register();

    // Entities
    ModEntities333.register();
    ModBeamEntities.register();

    // Dimensions
    SakaplusDimensions.register();

    // Greeny Features
    GreenyFeatures.register();

    // Commands
    CommandRegistrationCallback.EVENT.register(
        (dispatcher, registryAccess, environment) -> {
            KruzivilimCommand.register(dispatcher);
        }
    );

    // Kill Skill
    KillSkillLogic.initialize();

    // Walk Skill
    WalkSkillLogic.initialize();
}
