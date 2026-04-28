#!/bin/bash

# Define paths based on your provided file
CLIENT_JAVA="src/main/java/fryantit/militaryinsurgency/client/MilitaryInsurgencyClient.java"
PACKAGE_PATH="src/main/java/fryantit/militaryinsurgency"
MOD_ID="militaryinsurgency"

# 1. Update MilitaryInsurgencyClient.java to register GeckoLib Renderer
# We use 'sed' to insert the registration line into onInitializeClient
sed -i '/CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();/a \ \ \ \ \ \ \ \ software.bernie.geckolib.renderer.GeoArmorRenderer.registerArmorRenderer(new fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer(), fryantit.militaryinsurgency.item.ModItems.CIVIL_NVG);' "$CLIENT_JAVA"

# 2. Add missing imports to Client file
sed -i '14i import software.bernie.geckolib.renderer.GeoArmorRenderer;' "$CLIENT_JAVA"

# 3. Ensure CivilNVGRenderer.java uses the correct GeckoLib 4.x path (No .layer)
RENDERER_FILE="$PACKAGE_PATH/client/renderer/CivilNVGRenderer.java"
if [ -f "$RENDERER_FILE" ]; then
    sed -i 's/geckolib.renderer.layer.GeoArmorRenderer/geckolib.renderer.GeoArmorRenderer/g' "$RENDERER_FILE"
fi

# 4. Final safety check: Clean old build artifacts
./gradlew clean

echo "------------------------------------------------"
echo "✅ Fix complete: Renderer registered in ClientInitializer."
echo "✅ Import paths adjusted for GeckoLib 4.x."
echo "🚀 Now run: ./gradlew build"

