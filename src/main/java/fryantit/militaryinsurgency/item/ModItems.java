package fryantit.militaryinsurgency.item;

import fryantit.militaryinsurgency.armor.NVGArmorItem;
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
