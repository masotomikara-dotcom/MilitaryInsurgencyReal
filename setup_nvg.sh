#!/bin/bash

# 1. Define paths
REAL_PATH="src/main/java/fryantit/militaryinsurgency"
CLIENT_FILE="$REAL_PATH/client/MilitaryInsurgencyClient.java"
ARMOR_FILE="$REAL_PATH/armor/NVGArmorItem.java"

# 2. Rewrite NVGArmorItem.java
# Adding the missing getRenderProvider method required by GeoItem
cat <<EOF > "$ARMOR_FILE"
package fryantit.militaryinsurgency.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.core.animation.AnimatableManager;
import java.util.function.Supplier;

public class NVGArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NVGArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return GeoItem.makeRenderer(this);
    }
}
EOF

# 3. Rewrite MilitaryInsurgencyClient.java
# Using GeoArmorRenderer.registerArmorRenderer which is the correct way for GeckoLib 4
cat <<EOF > "$CLIENT_FILE"
package fryantit.militaryinsurgency.client;

import fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer;
import fryantit.militaryinsurgency.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Correct registration for GeckoLib 4.x on Fabric
        GeoArmorRenderer.registerArmorRenderer(new CivilNVGRenderer(), ModItems.NVG_ITEM);
    }
}
EOF
