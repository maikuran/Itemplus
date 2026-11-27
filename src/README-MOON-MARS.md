# DimensionMod: The Solar — Moon & Mars source update

This archive contains the `src/` tree only. The target is Minecraft Java 26.3 with Fabric.

## What was added

### Moon terrain
- A dedicated `moon_final_density` router with five smooth crater basins and rim formations.
- Seven Moon biomes with biome-specific natural monster spawns.
- A lunar temple, lunar research station, and underground lunar city using jigsaw room pools.
- A Moon boss altar in the temple start room and loot chests in the connected rooms.
- Moon wind ambience, background music, a Selene wind-up sound, and a depth-masked Earth-like sky body.

### Mars terrain
- A dedicated `mars_final_density` router with a connected canyon cut and three volcanic calderas.
- Eight Mars biomes with biome-specific natural monster spawns.
- A Martian war temple, research station, and underground city using jigsaw room pools.
- An Ares boss altar in the war temple and loot chests in the connected rooms.
- Martian wind ambience, background music, an Ares wind-up sound, and two depth-masked satellite bodies.

### Boss combat / UI
- Selene and Ares now telegraph signature attacks before impact, with synced animation poses, particle rings, and dedicated audio cues.
- Press **J** to open the quest journal. It shows Solar Rank, Divinity Level, current region, and six story objectives.

## Regenerate and validate structure NBT

From the `src/` directory:

```bash
bash tools/generate_moon_mars_structures.sh
bash tools/validate_moon_mars.sh
```

The scripts use Bash, `javac`, `java`, and `jq`. They do not use Python.

## Validation scope

The validation script checks JSON syntax, all 15 biome spawn definitions and entity IDs, density-function references, jigsaw pools/structures/structure sets, serialized 26.3 block-state keys, dimension audio references, sound and shader paths, NBT palette/connectors/chests/loot tables, and OGG files.

## Not yet verified

The uploaded project did not include `build.gradle`, `settings.gradle`, `gradle.properties`, `gradlew`, or `gradle-wrapper.jar`. The shell has OpenJDK 21 and no system Gradle; the mod metadata requires Java 25+. Therefore a real Fabric compile, remapped JAR build, Minecraft 26.3 launch, chunk-generation test, boss fight test, and ShaderC compile were not run.

The celestial bodies are currently screen-space, depth-masked post-effect overlays rather than camera-locked 3D sky objects. The quest journal is a first-stage RPG UI based on vanilla XP, synced advancement progress, and relic inventory; a full persistent skill tree and weapon mastery system remain unfinished. Jigsaw placement, terrain appearance, multiplayer behaviour, and performance still need in-game verification.

## Added Overworld and Nether material families
Overworld: Cobalt (コバルト), Limonite (リモナイト), Jade (翡翠), Ruby (ルビー), Krillul (クリルル).
Nether: Sitherian (シテリアン), Hellphyros-Remnantius-Forgotina (ヘルファイロス・レムナンティウス・フォルゴティナ).
The complete Hellphyros-Remnantius-Forgotina name is intentionally never abbreviated in any item, block, recipe, tag, translation, or documentation entry.
Each material has an ore, raw material, ingot, five tools, and four armor pieces.
