#!/bin/bash

# 1. Define paths
REAL_PATH="src/main/java/fryantit/militaryinsurgency"
ARMOR_FILE="$REAL_PATH/armor/NVGArmorItem.java"
CLIENT_FILE="$REAL_PATH/client/MilitaryInsurgencyClient.java"

# 2. Fix Constructor name in NVGArmorItem.java
# Change 'public CivilNVGItem' to 'public NVGArmorItem'
if [ -f "$ARMOR_FILE" ]; then
    sed -i 's/public CivilNVGItem/public NVGArmorItem/g' "$ARMOR_FILE"
    echo "✅ Fixed constructor name in NVGArmorItem.java"
fi

# 3. Ensure Client registration uses the correct Item reference
# We need to make sure it's using the right field from your ModItems or main class
# I will use 'NVG_ITEM' as a placeholder; please verify if it's 'CIVIL_NVG' or something else
sed -i 's/ModItems.CIVIL_NVG/ModItems.NVG_ITEM/g' "$CLIENT_FILE"

# 4. Final check on imports in NVGArmorItem
# Ensure it imports the renderer from the correct package
sed -i 's/import .*CivilNVGRenderer;/import fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer;/g' "$ARMOR_FILE"

echo "------------------------------------------------"
echo "Fix applied. The class and constructor are now both named 'NVGArmorItem'."
echo "Please run your build command now."

