#!/usr/bin/env bash
set -euo pipefail

DATA_DIR="src/main/resources/data"
if [[ ! -d "$DATA_DIR" ]]; then
  echo "ERROR: $DATA_DIR not found. Run this script from the repository root." >&2
  exit 1
fi

changed=0

# Minecraft 26.3/Fabric 26.3 ingredient codecs accept item IDs (or item ID
# arrays), not the older {"item":"namespace:id"} ingredient shorthand.
while IFS= read -r -d '' file; do
  tmp="${file}.tmp"
  jq 'walk(
    if type == "object"
       and (keys == ["item"])
       and (.item | type == "string")
    then .item
    else . end
  )' "$file" > "$tmp"
  if ! cmp -s "$file" "$tmp"; then
    mv "$tmp" "$file"
    echo "Fixed recipe ingredient format: $file"
    changed=$((changed + 1))
  else
    rm -f "$tmp"
  fi
done < <(find "$DATA_DIR" -type f -path '*/recipe/*.json' -print0)

# In 26.3, player_killed_entity.conditions.entity is a loot predicate.
# Wrap the former bare entity predicate in entity_properties so its required
# predicate type is explicit while preserving the target entity ID.
while IFS= read -r -d '' file; do
  tmp="${file}.tmp"
  jq '
    if (.criteria | type) == "object" then
      .criteria |= with_entries(
        .value |= (
          if .trigger == "minecraft:player_killed_entity"
             and (.conditions.entity | type) == "object"
             and ((.conditions.entity | keys) == ["type"])
             and (.conditions.entity.type | type) == "string"
          then
            .conditions.entity as $entity
            | .conditions.entity = {
                "type": "minecraft:entity_properties",
                "entity": "this",
                "predicate": $entity
              }
          else . end
        )
      )
    else . end
  ' "$file" > "$tmp"
  if ! cmp -s "$file" "$tmp"; then
    mv "$tmp" "$file"
    echo "Fixed 26.3 killed-entity advancement condition: $file"
    changed=$((changed + 1))
  else
    rm -f "$tmp"
  fi
done < <(find "$DATA_DIR" -type f -path '*/advancement/*.json' -print0)

# Validate every data JSON file before allowing the workflow to commit.
while IFS= read -r -d '' file; do
  jq -e . "$file" >/dev/null
done < <(find "$DATA_DIR" -type f -name '*.json' -print0)

echo "Validated all JSON files under $DATA_DIR"
echo "Changed files: $changed"
