#!/bin/bash

# 1. Define paths for the mod files
# Defining the path to our Java source code
REAL_PATH="src/main/java/fryantit/militaryinsurgency"
CLIENT_FILE="$REAL_PATH/client/MilitaryInsurgencyClient.java"
ARMOR_FILE="$REAL_PATH/armor/NVGArmorItem.java"

# 2. Re-write NVGArmorItem to be a standard GeoItem
# We remove the complex inline renderer and keep it clean for the registry
cat <<EOF > "$ARMOR_FILE"
package fryantit.militaryinsurgency.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class NVGArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NVGArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // No animations for the static NVG model
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
EOF

# 3. Use Fabric Rendering API in the Client Initializer
# This bypasses the GeckoLib internal registry which often causes NPE on Pojav
cat <<EOF > "$CLIENT_FILE"
package fryantit.militaryinsurgency.client;

import fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer;
import fryantit.militaryinsurgency.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Registering the 3D renderer using Fabric's official API
        // This ensures the renderer is loaded before the game tries to draw the item
        ArmorRenderer.register(new CivilNVGRenderer(), ModItems.NVG_ITEM);
    }
}
EOF
