#!/bin/bash

# Define base paths
PACKAGE_PATH="src/main/java/com/example/mod"
RES_PATH="src/main/resources/assets"
MOD_ID="militaryinsurgency"

# 1. Create Directories
mkdir -p "$PACKAGE_PATH/item"
mkdir -p "$PACKAGE_PATH/client/model"
mkdir -p "$PACKAGE_PATH/client/renderer"
mkdir -p "$RES_PATH/$MOD_ID/geo"
mkdir -p "$RES_PATH/minecraft/textures/models/armor"

# 2. Create CivilNVGItem.java
cat <<EOF > "$PACKAGE_PATH/item/CivilNVGItem.java"
package com.example.mod.item;

import com.example.mod.client.renderer.CivilNVGRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CivilNVGItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public CivilNVGItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private CivilNVGRenderer renderer;

            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                if (this.renderer == null)
                    this.renderer = new CivilNVGRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
EOF

# 3. Create CivilNVGModel.java
cat <<EOF > "$PACKAGE_PATH/client/model/CivilNVGModel.java"
package com.example.mod.client.model;

import com.example.mod.item.CivilNVGItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class CivilNVGModel extends GeoModel<CivilNVGItem> {
    @Override
    public Identifier getModelResource(CivilNVGItem animatable) {
        // Model JSON is in mod namespace
        return new Identifier("militaryinsurgency", "geo/civil_nvg.geo.json");
    }

    @Override
    public Identifier getTextureResource(CivilNVGItem animatable) {
        // Armor texture is in minecraft namespace as requested
        return new Identifier("minecraft", "textures/models/armor/civil_nvg.png");
    }

    @Override
    public Identifier getAnimationResource(CivilNVGItem animatable) {
        return new Identifier("militaryinsurgency", "animations/civil_nvg.animation.json");
    }
}
EOF

# 4. Create CivilNVGRenderer.java
cat <<EOF > "$PACKAGE_PATH/client/renderer/CivilNVGRenderer.java"
package com.example.mod.client.renderer;

import com.example.mod.client.model.CivilNVGModel;
import com.example.mod.item.CivilNVGItem;
import software.bernie.geckolib.renderer.layer.GeoArmorRenderer;

public class CivilNVGRenderer extends GeoArmorRenderer<CivilNVGItem> {
    public CivilNVGRenderer() {
        super(new CivilNVGModel());
    }
}
EOF

# 5. Clean up old renderers (Edit names if necessary)
# rm -f "$PACKAGE_PATH/client/renderer/OldNVGRenderer.java"

echo "Setup complete. Folders and files created. Please run ./gradlew build"

