#!/usr/bin/env bash
set -euo pipefail
SRC_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
javac -d "$TMP" "$SRC_ROOT/tools/StructureNbtGenerator.java" "$SRC_ROOT/tools/StructureNbtValidator.java"
java -cp "$TMP" StructureNbtGenerator "$SRC_ROOT/main/resources/data/thesolar/structure"
java -cp "$TMP" StructureNbtValidator "$SRC_ROOT/main/resources/data/thesolar/structure"
