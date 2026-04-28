#!/bin/bash

# 1. Define paths
# Set path for the Armor Item and Client file
REAL_PATH="src/main/java/fryantit/militaryinsurgency"
ARMOR_FILE="$REAL_PATH/armor/NVGArmorItem.java"
CLIENT_FILE="$REAL_PATH/client/MilitaryInsurgencyClient.java"

# 2. Update NVGArmorItem.java to handle its own rendering
# Adding necessary imports for GeckoLib 4 rendering
sed -i 's/import net.minecraft.item.ArmorItem;/import net.minecraft.item.ArmorItem;\nimport software.bernie.geckolib.animatable.GeoItem;\nimport software.bernie.geckolib.animatable.client.RenderProvider;\nimport fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer;\nimport java.util.function.Consumer;\nimport java.util.function.Supplier;/g' "$ARMOR_FILE"

# Make the class implement GeoItem and add the createRenderer method
sed -i 's/public class NVGArmorItem extends ArmorItem/public class NVGArmorItem extends ArmorItem implements GeoItem/g' "$ARMOR_FILE"

# Insert the createRenderer logic before the last closing brace
# This is the standard way GeckoLib 4 (1.20.1) handles armor rendering on Fabric
sed -i '$d' "$ARMOR_FILE"
cat <<EOF >> "$ARMOR_FILE"
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
}
EOF

# 3. Clean up MilitaryInsurgencyClient.java
# We no longer need to register it here because the Item handles it now
sed -i '/RenderProvider.registerArmorRenderer/d' "$CLIENT_FILE"
sed -i '/import software.bernie.geckolib.animatable.client.RenderProvider;/d' "$CLIENT_FILE"

echo "------------------------------------------------"
echo "✅ Moved rendering logic into NVGArmorItem class"
echo "✅ Cleaned up MilitaryInsurgencyClient"
echo "🚀 Everything is synced for GeckoLib 4.x. Run build now."

