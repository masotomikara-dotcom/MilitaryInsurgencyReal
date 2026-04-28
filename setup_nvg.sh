#!/bin/bash

# 1. Define paths
REAL_PATH="src/main/java/fryantit/militaryinsurgency"
ITEM_PATH="$REAL_PATH/item"
CLIENT_FILE="$REAL_PATH/client/MilitaryInsurgencyClient.java"
ARMOR_PACKAGE="fryantit.militaryinsurgency.armor"

# 2. Create the item directory if it doesn't exist
mkdir -p "$ITEM_PATH"

# 3. Create or Overwrite ModItems.java to ensure NVG_ITEM exists
cat <<EOF > "$ITEM_PATH/ModItems.java"
package fryantit.militaryinsurgency.item;

import $ARMOR_PACKAGE.NVGArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    // Registry for the NVG Item
    public static final Item NVG_ITEM = new NVGArmorItem(ArmorMaterials.NETHERITE, NVGArmorItem.Type.HELMET, new Item.Settings().maxCount(1));

    public static void registerModItems() {
        Registry.register(Registries.ITEM, new Identifier("militaryinsurgency", "civil_nvg"), NVG_ITEM);
    }
}
EOF

# 4. Final fix for MilitaryInsurgencyClient.java
# Ensure it points exactly to ModItems.NVG_ITEM
sed -i 's/ModItems.CIVIL_NVG/ModItems.NVG_ITEM/g' "$CLIENT_FILE"

# 5. Fix NVGArmorItem constructor (safety check)
if [ -f "$REAL_PATH/armor/NVGArmorItem.java" ]; then
    sed -i 's/public CivilNVGItem/public NVGArmorItem/g' "$REAL_PATH/armor/NVGArmorItem.java"
fi

echo "------------------------------------------------"
echo "Item Registry Synced: ModItems.NVG_ITEM created."
echo "Path: $ITEM_PATH/ModItems.java"

