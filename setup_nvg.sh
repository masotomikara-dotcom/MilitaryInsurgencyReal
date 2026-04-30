#!/bin/bash

# 1. Define paths
REAL_PATH="src/main/java/fryantit/militaryinsurgency"
ARMOR_FILE="$REAL_PATH/armor/NVGArmorItem.java"
CLIENT_FILE="$REAL_PATH/client/MilitaryInsurgencyClient.java"

# 2. Rewrite NVGArmorItem.java with Safety Checks
# We add a null check for the Minecraft Client to prevent early rendering
cat <<EOF > "$ARMOR_FILE"
package fryantit.militaryinsurgency.armor;

import net.minecraft.client.MinecraftClient;
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
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public NVGArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer renderer;

            @Override
            public net.minecraft.client.render.entity.model.BipedEntityModel<net.minecraft.entity.LivingEntity> getHumanoidArmorModel(net.minecraft.entity.LivingEntity livingEntity, net.minecraft.item.ItemStack itemStack, net.minecraft.entity.EquipmentSlot equipmentSlot, net.minecraft.client.render.entity.model.BipedEntityModel<net.minecraft.entity.LivingEntity> original) {
                // SAFETY CHECK: If Minecraft's render system isn't ready, skip rendering
                if (MinecraftClient.getInstance().getEntityRenderDispatcher() == null) {
                    return original;
                }
                
                if (this.renderer == null) {
                    this.renderer = new fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer();
                }
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

# 3. Reset Client Initializer
cat <<EOF > "$CLIENT_FILE"
package fryantit.militaryinsurgency.client;

import net.fabricmc.api.ClientModInitializer;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Handled by Item Provider
    }
}
EOF
