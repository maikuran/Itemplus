#!/usr/bin/env bash
set -euo pipefail
SRC_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
RES="$SRC_ROOT/main/resources"
DATA="$RES/data/thesolar"
ASSET="$RES/assets/thesolar"
JAVA_ROOT="$SRC_ROOT/main/java"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
pass() { printf 'PASS  %s\n' "$1"; }
fail() { printf 'FAIL  %s\n' "$1" >&2; exit 1; }

find "$RES" -type f -name '*.json' -print0 | xargs -0 -r -n1 jq empty
pass "JSON syntax: $(find "$RES" -type f -name '*.json' | wc -l) files"
if grep -R -n '"Name"|"Properties"' "$DATA" --include="*.json"; then fail "Legacy serialized block-state keys remain"; fi
pass "26.3 block-state id/properties keys"

[[ "$(find "$DATA/worldgen/biome" -maxdepth 1 -name '*.json' | wc -l)" -eq 15 ]] || fail "Expected 15 Moon/Mars biomes"
for f in "$DATA"/worldgen/biome/*.json; do
  jq -e '.attributes["minecraft:gameplay/natural_mob_spawns"].modifier == "overlay" and (.attributes["minecraft:gameplay/natural_mob_spawns"].argument.spawns_by_category.monster | length > 0 and all(.[]; has("type") and has("count") and has("weight")))' "$f" >/dev/null || fail "Missing 26.3 natural_mob_spawns: $f"
  while IFS= read -r id; do
    short="${id#thesolar:}"
    grep -q "register(\"$short\"" "$JAVA_ROOT/com/sakalti/thesolar/entity/ModEntities.java" || fail "Unknown biome spawn entity $id in $f"
  done < <(jq -r '.attributes["minecraft:gameplay/natural_mob_spawns"].argument.spawns_by_category | .. | objects | .type? // empty' "$f")
done
pass "15 biomes: 26.3 natural spawn tables and entity IDs"

for f in "$DATA"/worldgen/noise_settings/{moon,mars}.json; do
  id="$(jq -r '.noise_router.final_density' "$f")"
  [[ -f "$DATA/worldgen/density_function/${id#thesolar:}.json" ]] || fail "Missing density function $id"
done
pass "Moon/Mars final_density references"

for f in "$DATA"/worldgen/structure/*.json; do
  pool="$(jq -r '.start_pool' "$f")"
  [[ -f "$DATA/worldgen/template_pool/${pool#thesolar:}.json" ]] || fail "Missing start pool $pool"
  while IFS= read -r biome; do
    [[ -f "$DATA/worldgen/biome/${biome#thesolar:}.json" ]] || fail "Unknown structure biome $biome"
  done < <(jq -r '.biomes[]?' "$f")
done
for f in "$DATA"/worldgen/structure_set/*.json; do
  while IFS= read -r id; do [[ -f "$DATA/worldgen/structure/${id#thesolar:}.json" ]] || fail "Missing structure $id"; done < <(jq -r '.structures[].structure' "$f")
done
for f in "$DATA"/worldgen/template_pool/*.json; do
  while IFS= read -r id; do [[ -f "$DATA/structure/${id#thesolar:}.nbt" ]] || fail "Missing structure NBT $id"; done < <(jq -r '.. | objects | .location? // empty' "$f")
done
pass "6 jigsaw structures, 6 structure sets, 8 template pools, 12 NBT templates referenced"

for f in "$DATA"/dimension_type/*.json; do grep -q '"destroy_on_use"' "$f" || fail "Missing 26.3 bed rule: $f"; done
if grep -R -n '"explodes"' "$DATA"/dimension_type; then fail "Legacy bed-rule key explodes remains"; fi
pass "26.3 dimension type bed-rule fields"
for spec in "moon:moon_ambience:music_moon" "mars:mars_ambience:music_mars"; do IFS=: read -r dim ambient music <<< "$spec"; jq -e --arg a "thesolar:$ambient" --arg m "thesolar:$music" '.attributes["minecraft:audio/ambient_sounds"].loop == $a and .attributes["minecraft:audio/background_music"].default.sound == $m' "$DATA/dimension_type/$dim.json" >/dev/null || fail "Missing dimension audio in $dim"; done
pass "Moon/Mars ambient loop and background music attributes"

while IFS= read -r sound; do
  [[ -f "$ASSET/sounds/${sound#thesolar:}.ogg" ]] || fail "Missing OGG $sound"
done < <(jq -r 'to_entries[].value.sounds[].name' "$ASSET/sounds.json")
pass "Custom sound event to OGG references"
grep -q 'selene_telegraph"' "$JAVA_ROOT/com/sakalti/thesolar/DimensionMod.java" || fail "Selene sound event not registered"
grep -q 'ares_telegraph"' "$JAVA_ROOT/com/sakalti/thesolar/DimensionMod.java" || fail "Ares sound event not registered"
pass "Boss telegraph sound event registrations"

for f in "$ASSET"/post_effect/*.json; do
  while IFS= read -r shader; do
    [[ "$shader" == minecraft:* ]] && continue
    path="${shader#thesolar:}"
    [[ -f "$ASSET/shaders/${path}.vsh" || -f "$ASSET/shaders/${path}.fsh" ]] || fail "Missing post-effect shader $shader"
  done < <(jq -r '.passes[] | .vertex_shader,.fragment_shader' "$f")
done
pass "Moon/Mars post-effect shader references"
grep -q "posteffect add @s thesolar:moon_sky" "$JAVA_ROOT/com/sakalti/thesolar/DimensionMod.java" || fail "Moon sky effect is not activated"
grep -q "posteffect add @s thesolar:mars_sky" "$JAVA_ROOT/com/sakalti/thesolar/DimensionMod.java" || fail "Mars sky effect is not activated"
pass "Automatic dimension-specific sky effect switching"

for f in "$DATA"/structure/*.nbt; do gzip -t "$f" || fail "Corrupt NBT gzip: $f"; done
javac -d "$TMP" "$SRC_ROOT/tools/StructureNbtValidator.java"
java -cp "$TMP" StructureNbtValidator "$DATA/structure"
pass "NBT structure payloads"

if command -v ffprobe >/dev/null 2>&1; then
  for f in "$ASSET"/sounds/**/*.ogg; do ffprobe -v error -show_entries format=duration -of csv=p=0 "$f" >/dev/null || fail "Invalid OGG $f"; done
  pass "OGG decode checks"
else
  for f in "$ASSET"/sounds/**/*.ogg; do [[ -s "$f" ]] || fail "Empty audio $f"; done
  pass "OGG file presence (ffprobe unavailable)"
fi


missing_refs=0
while IFS= read -r id; do
  [[ -z "$id" ]] && continue
  ns=minecraft; path="$id"
  if [[ "$id" == *:* ]]; then ns="${id%%:*}"; path="${id#*:}"; fi
  if [[ ! -f "$RES/assets/$ns/models/$path.json" ]]; then printf 'Missing model: %s\n' "$id" >&2; missing_refs=$((missing_refs+1)); fi
done < <(find "$ASSET/blockstates" "$ASSET/items" -type f -name '*.json' -print0 | xargs -0 -r jq -r '.. | objects | .model? | select(type=="string")' | sort -u)
while IFS= read -r id; do
  [[ -z "$id" || "$id" == \#* ]] && continue
  ns=minecraft; path="$id"
  if [[ "$id" == *:* ]]; then ns="${id%%:*}"; path="${id#*:}"; fi
  if [[ ! -f "$RES/assets/$ns/textures/$path.png" ]]; then printf 'Missing texture: %s\n' "$id" >&2; missing_refs=$((missing_refs+1)); fi
done < <(find "$ASSET/models" -type f -name '*.json' -print0 | xargs -0 -r jq -r '.textures[]? | select(type=="string")' | sort -u)
[[ "$missing_refs" -eq 0 ]] || fail "$missing_refs model/texture references are unresolved"
pass "Blockstate/item model and model texture references"

while IFS= read -r -d '' f; do
  file "$f" | grep -q 'PNG image data' || fail "Invalid PNG file $f"
done < <(find "$ASSET" -type f -name '*.png' -print0)
pass "PNG file signatures"
QUEST_UI="$SRC_ROOT/client/java/com/sakalti/thesolar/client/SolarQuestScreen.java"
grep -q 'getAdvancements()' "$QUEST_UI" || fail "Quest UI does not read synced advancement progress"
grep -q 'experienceProgress' "$QUEST_UI" || fail "Quest UI has no XP progress bar"
pass "Advancement-backed quest objectives and XP progression UI"

printf "\nMoon/Mars source validation completed. This script does not replace a Minecraft 26.3 Gradle build or in-game test.\n"
