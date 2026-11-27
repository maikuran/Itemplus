# Lunar equipment, portals, combat tuning and asset refresh

- Added full Selenite and Lunarium tool sets (sword, pickaxe, axe, shovel, hoe) and armor sets (helmet, chestplate, leggings, boots).
- Added separate raw-material drops, ingots, smelting/blasting recipes, equipment recipes, repair tags and equipment-layer textures.
- Selenite held sword grants Strength I; its mining tools grant Haste I. Two Selenite armor pieces grant Slow Falling; a full set grants Night Vision and Slow Falling.
- Lunarium held sword grants Strength II; its mining tools grant Haste II. Two Lunarium armor pieces grant Resistance I; a full set grants Resistance I and Strength I.
- Either lunar boots set grants Speed I while the player is on the Moon.
- Added craftable Moon and Mars Gateway blocks. Walking into a gateway transfers the player to its dimension; the same gateway returns the player to the Overworld when used from that dimension.
- Added synchronized combat levels: Moon hostiles span levels 40-68 and Selene is level 70; Mars hostiles span levels 50-78 and Ares is level 80.
- Increased Selene to 1,600 HP and Ares to 2,200 HP. Boss phase-two health recovery and damage/speed multipliers were increased.
- Rebuilt tool icons from vanilla iron-tool silhouettes and tinted only metal pixels. Original dark-brown wooden handle pixels are preserved. Added distinct pixel-art axe/hoe silhouettes, mineral ore textures, armor icons and gateway textures.

## Validation performed

- All resource JSON files parse with `jq`.
- All resource PNGs decode with ImageMagick.
- Model texture references and equipment texture references were checked.
- Java parser scan found no syntax diagnostics; full type-checking is not possible without Minecraft/Fabric 26.3 dependencies.
- Gradle build, game launch, portal transfer, combat and gameplay tests have not been run in this environment.
