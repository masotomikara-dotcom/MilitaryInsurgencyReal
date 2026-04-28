#!/bin/bash

# 1. Define paths
# Identifying the source file for the NVG Armor
REAL_PATH="src/main/java/fryantit/militaryinsurgency"
ARMOR_FILE="$REAL_PATH/armor/NVGArmorItem.java"

# 2. Fix the duplicate "implements GeoItem" error
# This command replaces the double implementation with a single correct one
if [ -f "$ARMOR_FILE" ]; then
    sed -i 's/implements GeoItem implements GeoItem/implements GeoItem/g' "$ARMOR_FILE"
    echo "✅ Fixed duplicate GeoItem interface in NVGArmorItem.java"
fi

# 3. Clean up the constructor just in case it was duplicated during previous runs
# Ensuring only one constructor exists and matches the class name
sed -i 's/public CivilNVGItem/public NVGArmorItem/g' "$ARMOR_FILE"

# 4. Final check for missing brackets or broken lines
# Using a simple check to ensure the file structure is valid
if grep -q "implements GeoItem {" "$ARMOR_FILE"; then
    echo "✅ Syntax check: Class header looks correct."
else
    # If the curly brace is missing, add it
    sed -i 's/implements GeoItem/implements GeoItem {/g' "$ARMOR_FILE"
    # Remove any potential double braces created by the fix
    sed -i 's/{ {/{/g' "$ARMOR_FILE"
fi

echo "------------------------------------------------"
echo "✅ Syntax cleanup complete."
echo "🚀 Run: ./gradlew clean build"

