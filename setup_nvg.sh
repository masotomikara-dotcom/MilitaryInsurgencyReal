#!/bin/bash

# 1. Define paths
REAL_PATH="src/main/java/fryantit/militaryinsurgency"
CLIENT_FILE="$REAL_PATH/client/MilitaryInsurgencyClient.java"

# 2. Update the registration logic to the standard GeckoLib 4.x Fabric format
# We will use the Fabric-specific ArmorRenderer registration which is more stable in 1.20.1
sed -i '/GeoArmorRenderer.registerArmorRenderer/d' "$CLIENT_FILE"

# 3. Inject the compatible registration line
# This uses the ArmorRenderer.register method which GeckoLib 4 expects for Fabric
sed -i '/CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();/a \ \ \ \ \ \ \ \ software.bernie.geckolib.animatable.client.RenderProvider.registerArmorRenderer(new fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer(), fryantit.militaryinsurgency.item.ModItems.NVG_ITEM);' "$CLIENT_FILE"

# 4. Clean up any broken imports
sed -i '/import software.bernie.geckolib.renderer.GeoArmorRenderer;/d' "$CLIENT_FILE"
sed -i '15i import software.bernie.geckolib.animatable.client.RenderProvider;' "$CLIENT_FILE"

echo "------------------------------------------------"
echo "✅ Registration method updated to RenderProvider.registerArmorRenderer"
echo "✅ Method signature adjusted for GeckoLib 4.x (Fabric 1.20.1)"
echo "🚀 Run: ./gradlew build"

