#!/bin/sh
# Builds docs/cakedcalc.jar for the web demo (GitHub Pages serves the docs folder).
# Run from the project root: sh demo/build.sh
set -e
out=$(mktemp -d)
javac --release 17 -d "$out" src/CakedCalc.java demo/DemoBridge.java
jar --create --file docs/cakedcalc.jar --main-class CakedCalc -C "$out" .
rm -rf "$out"
echo "Built docs/cakedcalc.jar"
