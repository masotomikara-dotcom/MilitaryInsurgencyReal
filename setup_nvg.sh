#!/bin/bash

# 1. Define paths
# Setting up variables for the files that need fixing
REAL_PATH="src/main/java/fryantit/militaryinsurgency"
CLIENT_FILE="$REAL_PATH/client/MilitaryInsurgencyClient.java"
ARMOR_FILE="$REAL_PATH/armor/NVGArmorItem.java"

# 2. Rewrite NVGArmorItem.java to include the missing createRenderer method
# This follows the mandatory GeckoLib 4.x structure for Fabric
cat <<EOF > "$ARMOR_FILE"
package fryantit.militaryinsurgency.armor;

import fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.core.animation.AnimatableManager;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NVGArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NVGArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    // Mandatory method for GeoItem to link the renderer
    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private CivilNVGRenderer renderer;

            @Override
            public net.minecraft.client.render.entity.model.BipedEntityModel<net.minecraft.entity.LivingEntity> getHumanoidArmorModel(net.minecraft.entity.LivingEntity livingEntity, net.minecraft.item.ItemStack itemStack, net.minecraft.entity.EquipmentSlot equipmentSlot, net.minecraft.client.render.entity.model.BipedEntityModel<net.minecraft.entity.LivingEntity> original) {
                if (this.renderer == null) {
                    this.renderer = new CivilNVGRenderer();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return GeoItem.makeRenderer(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
EOF

# 3. Rewrite MilitaryInsurgencyClient.java
# Removing the global registration since the Item now handles its own renderer
cat <<EOF > "$CLIENT_FILE"
package fryantit.militaryinsurgency.client;

import net.fabricmc.api.ClientModInitializer;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // In GeckoLib 4 Fabric, the item handles its own render provider via createRenderer
    }
}
EOF
