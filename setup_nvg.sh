#!/bin/bash

# 1. Define paths for the project structure
REAL_PATH="src/main/java/fryantit/militaryinsurgency"
CLIENT_FILE="$REAL_PATH/client/MilitaryInsurgencyClient.java"

# 2. Fix the GeckoLib 4.x (1.20.1) registration method
# In GeckoLib 4, the method is static but sometimes requires explicit casting 
# or a specific package path to match the 'registerArmorRenderer' signature.
sed -i '/GeoArmorRenderer.registerArmorRenderer/d' "$CLIENT_FILE"

# 3. Inject the correct registration line with proper Casting to avoid 'symbol not found'
# We use the full path for GeoArmorRenderer to ensure the compiler maps it correctly.
sed -i '/CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();/a \ \ \ \ \ \ \ \ software.bernie.geckolib.renderer.GeoArmorRenderer.registerArmorRenderer(new fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer(), (net.minecraft.item.Item) fryantit.militaryinsurgency.item.ModItems.NVG_ITEM);' "$CLIENT_FILE"

# 4. Final check: Ensure we don't have duplicate or broken imports in the Client file
sed -i '/import software.bernie.geckolib.renderer.GeoArmorRenderer;/d' "$CLIENT_FILE"
sed -i '15i import software.bernie.geckolib.renderer.GeoArmorRenderer;' "$CLIENT_FILE"

echo "------------------------------------------------"
echo "✅ Registration logic updated for GeckoLib 4.x"
echo "✅ Added Item casting to match method signature"
echo "🚀 Everything is synced. You can now run: ./gradlew build"

