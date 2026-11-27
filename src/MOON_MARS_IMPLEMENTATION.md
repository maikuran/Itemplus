# DimensionMod: The Solar — Moon & Mars Implementation

This document records the Moon/Mars work added to the source tree. The target is Minecraft Java 26.3 with Fabric.

## World generation

- `moon_final_density` uses the 26.3 density functions `distance_to_point`, `slice`, `div`, `clamp`, `lerp`, `gradient`, and `range_choice` to lower several crater basins and shape raised crater rims.
- `mars_final_density` combines a chain of overlapping density cuts for a long canyon with separate caldera basins and raised rims.
- Both custom noise settings route `noise_router.final_density` through the new dimension-specific density function.
- The 15 Moon/Mars biomes now use the 26.3 `minecraft:gameplay/natural_mob_spawns` environment attribute. Bosses remain altar-only.

## Structures

The data pack adds six jigsaw structures:

- Moon: lunar temple, lunar research station, underground lunar city.
- Mars: Martian war temple, Martian research station, underground Martian city.

Twelve 17×9×17 structure NBT templates are generated with four oriented jigsaw connectors per room. Template pools allow rooms to connect recursively. Temple start rooms include their dimension's boss altar. Chests in the templates reference dimension-specific loot tables.

## Boss combat and animation

`SolarHostileEntity` synchronizes attack type and remaining telegraph ticks. Selene and Ares now wind up their signature attacks before impact. The server emits a ground ring of particles and a dedicated sound during the wind-up. `SolarMobRenderer` copies the synchronized state into `SolarMobRenderState`, and `SolarMobModel` uses it for distinct attack poses.

Attack IDs:

| ID | Boss action |
|---:|---|
| 1 | Selene moonbeam |
| 2 | Selene crystal shockwave |
| 3 | Selene lunar healing |
| 4 | Ares ground-breaking quake |
| 5 | Ares fire spear |

## Audio and sky presentation

- Six original OGG assets are generated from command-line audio synthesis: Moon/Mars ambience, Moon/Mars music, and the two boss wind-up cues.
- Dimension and biome environment attributes select the ambient loops and background music.
- `moon_sky` and `mars_sky` are 26.3 post effects. They draw procedural, depth-masked celestial bodies only where the reversed depth buffer indicates sky. Server-side join/dimension-change handling adds/removes the relevant effect for each player.
- The planet overlay is currently screen-space and depth-masked; it is not yet a camera-orientation-locked 3D sky object.

## Quest / RPG interface

Press **J** to open the quest journal. It shows the current dimension, a Solar Rank derived from vanilla XP level, an XP progress bar, a Divinity Level, and six story objectives. Quest completion is derived from synced vanilla advancement progress, current dimension, and relic inventory.

## Validation

Run from this folder:

```bash
bash tools/validate_moon_mars.sh
```

The script checks JSON syntax, biome spawn entity IDs, density-function references, jigsaw pools/structures, dimension-type keys, sound and shader references, NBT structure payloads, and OGG assets.

## Known limitations / tests not completed

- No Minecraft 26.3 game process was launched. The supplied environment has Java 21, while Minecraft 26.3 requires Java 25. The source upload did not include Gradle project files or a Gradle wrapper, and no system Gradle was available.
- The post-effect shaders have not been compiled by Minecraft's ShaderC renderer. Their in-game appearance and compatibility with other post effects remain unverified.
- Density functions and jigsaw structures were validated structurally and as JSON/NBT, but not by an actual Minecraft chunk-generation run. Terrain shape, structure placement rate, room connection geometry, chest opening, and boss altar activation still require in-game verification.
- The quest journal is client UI backed by vanilla XP, synced advancement progress, dimension state, and relic inventory. A separate persistent skill tree, weapon mastery, and custom server-side Solar XP ledger are not implemented here.
- The Moon's Earth and Mars' satellites are depth-masked screen-space sky bodies, not fully world-rotating celestial meshes.

## Added Overworld and Nether material families
Overworld: Cobalt (コバルト), Limonite (リモナイト), Jade (翡翠), Ruby (ルビー), Krillul (クリルル).
Nether: Sitherian (シテリアン), Hellphyros-Remnantius-Forgotina (ヘルファイロス・レムナンティウス・フォルゴティナ).
The complete Hellphyros-Remnantius-Forgotina name is intentionally never abbreviated in any item, block, recipe, tag, translation, or documentation entry.
Each material has an ore, raw material, ingot, five tools, and four armor pieces.
