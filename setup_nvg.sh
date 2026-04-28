#!/bin/bash

# 1. Define paths based on your real project
REAL_PATH="src/main/java/fryantit/militaryinsurgency"
CLIENT_FILE="$REAL_PATH/client/MilitaryInsurgencyClient.java"

# 2. Create required directories
mkdir -p "$REAL_PATH/client/renderer"
mkdir -p "$REAL_PATH/client/model"
mkdir -p "$REAL_PATH/armor"
mkdir -p "$REAL_PATH/item"

# 3. Rename and move Item Class to match NVGArmorItem
# Check both possible previous locations
if [ -f "src/main/java/com/example/mod/item/CivilNVGItem.java" ]; then
    mv src/main/java/com/example/mod/item/CivilNVGItem.java "$REAL_PATH/armor/NVGArmorItem.java"
elif [ -f "$REAL_PATH/item/CivilNVGItem.java" ]; then
    mv "$REAL_PATH/item/CivilNVGItem.java" "$REAL_PATH/armor/NVGArmorItem.java"
fi

# 4. Update NVGArmorItem.java content
if [ -f "$REAL_PATH/armor/NVGArmorItem.java" ]; then
    sed -i 's/package .*;/package fryantit.militaryinsurgency.armor;/g' "$REAL_PATH/armor/NVGArmorItem.java"
    sed -i 's/public class CivilNVGItem/public class NVGArmorItem/g' "$REAL_PATH/armor/NVGArmorItem.java"
    # Fix renderer import path
    sed -i 's/import .*CivilNVGRenderer;/import fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer;/g' "$REAL_PATH/armor/NVGArmorItem.java"
fi

# 5. Update CivilNVGModel.java content
if [ -f "$REAL_PATH/client/model/CivilNVGModel.java" ]; then
    sed -i 's/package .*;/package fryantit.militaryinsurgency.client.model;/g' "$REAL_PATH/client/model/CivilNVGModel.java"
    sed -i 's/import .*CivilNVGItem;/import fryantit.militaryinsurgency.armor.NVGArmorItem;/g' "$REAL_PATH/client/model/CivilNVGModel.java"
    sed -i 's/CivilNVGItem/NVGArmorItem/g' "$REAL_PATH/client/model/CivilNVGModel.java"
fi

# 6. Update CivilNVGRenderer.java content (Fixing 1.20.1 GeckoLib path)
if [ -f "$REAL_PATH/client/renderer/CivilNVGRenderer.java" ]; then
    sed -i 's/package .*;/package fryantit.militaryinsurgency.client.renderer;/g' "$REAL_PATH/client/renderer/CivilNVGRenderer.java"
    sed -i 's/import .*CivilNVGItem;/import fryantit.militaryinsurgency.armor.NVGArmorItem;/g' "$REAL_PATH/client/renderer/CivilNVGRenderer.java"
    sed -i 's/import .*CivilNVGModel;/import fryantit.militaryinsurgency.client.model.CivilNVGModel;/g' "$REAL_PATH/client/renderer/CivilNVGRenderer.java"
    sed -i 's/CivilNVGItem/NVGArmorItem/g' "$REAL_PATH/client/renderer/CivilNVGRenderer.java"
    # Ensure it uses standard GeckoLib 4 path
    sed -i 's/renderer.layer.GeoArmorRenderer/renderer.GeoArmorRenderer/g' "$REAL_PATH/client/renderer/CivilNVGRenderer.java"
fi

# 7. Final Fix for MilitaryInsurgencyClient.java
# Clean up any failed previous registration attempts to avoid duplicates
sed -i '/GeoArmorRenderer.registerArmorRenderer/d' "$CLIENT_FILE"
sed -i '/import software.bernie.geckolib.renderer.GeoArmorRenderer;/d' "$CLIENT_FILE"
sed -i '/import fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer;/d' "$CLIENT_FILE"

# Re-insert clean imports and registration
sed -i '15i import software.bernie.geckolib.renderer.GeoArmorRenderer;' "$CLIENT_FILE"
sed -i '16i import fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer;' "$CLIENT_FILE"

# Based on your logs, I'll use ModItems.CIVIL_NVG as the reference
sed -i '/CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();/a \ \ \ \ \ \ \ \ GeoArmorRenderer.registerArmorRenderer(new CivilNVGRenderer(), fryantit.militaryinsurgency.item.ModItems.CIVIL_NVG);' "$CLIENT_FILE"

echo "------------------------------------------------"
echo "Done! All files renamed to match NVGArmorItem."
echo "Package structure: fryantit.militaryinsurgency"
echo "Client registration: Linked to ModItems.CIVIL_NVG"

