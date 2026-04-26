package fryantit.militaryinsurgency.armor;

import fryantit.militaryinsurgency.MilitaryInsurgencyMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MilitaryArmorItems {
    public static final Item HARDENED_STEEL_HELMET = registerArmor("hardened_steel_helmet", MilitaryArmorMaterials.HARDENED_STEEL, ArmorItem.Type.HELMET);
    public static final Item HARDENED_STEEL_CHESTPLATE = registerArmor("hardened_steel_chestplate", MilitaryArmorMaterials.HARDENED_STEEL, ArmorItem.Type.CHESTPLATE);
    public static final Item HARDENED_STEEL_LEGGINGS = registerArmor("hardened_steel_leggings", MilitaryArmorMaterials.HARDENED_STEEL, ArmorItem.Type.LEGGINGS);
    public static final Item HARDENED_STEEL_BOOTS = registerArmor("hardened_steel_boots", MilitaryArmorMaterials.HARDENED_STEEL, ArmorItem.Type.BOOTS);

    public static final Item KEVLAR_HELMET = registerArmor("kevlar_helmet", MilitaryArmorMaterials.KEVLAR, ArmorItem.Type.HELMET);
    public static final Item KEVLAR_CHESTPLATE = registerArmor("kevlar_chestplate", MilitaryArmorMaterials.KEVLAR, ArmorItem.Type.CHESTPLATE);
    public static final Item KEVLAR_LEGGINGS = registerArmor("kevlar_leggings", MilitaryArmorMaterials.KEVLAR, ArmorItem.Type.LEGGINGS);
    public static final Item KEVLAR_BOOTS = registerArmor("kevlar_boots", MilitaryArmorMaterials.KEVLAR, ArmorItem.Type.BOOTS);

    public static final Item TITANIUM_HELMET = registerArmor("titanium_helmet", MilitaryArmorMaterials.TITANIUM, ArmorItem.Type.HELMET);
    public static final Item TITANIUM_CHESTPLATE = registerArmor("titanium_chestplate", MilitaryArmorMaterials.TITANIUM, ArmorItem.Type.CHESTPLATE);
    public static final Item TITANIUM_LEGGINGS = registerArmor("titanium_leggings", MilitaryArmorMaterials.TITANIUM, ArmorItem.Type.LEGGINGS);
    public static final Item TITANIUM_BOOTS = registerArmor("titanium_boots", MilitaryArmorMaterials.TITANIUM, ArmorItem.Type.BOOTS);

    public static final Item CARBON_FIBER_HELMET = registerArmor("carbon_fiber_helmet", MilitaryArmorMaterials.CARBON_FIBER, ArmorItem.Type.HELMET);
    public static final Item CARBON_FIBER_CHESTPLATE = registerArmor("carbon_fiber_chestplate", MilitaryArmorMaterials.CARBON_FIBER, ArmorItem.Type.CHESTPLATE);
    public static final Item CARBON_FIBER_LEGGINGS = registerArmor("carbon_fiber_leggings", MilitaryArmorMaterials.CARBON_FIBER, ArmorItem.Type.LEGGINGS);
    public static final Item CARBON_FIBER_BOOTS = registerArmor("carbon_fiber_boots", MilitaryArmorMaterials.CARBON_FIBER, ArmorItem.Type.BOOTS);

    public static final Item CIVIL_NVG = registerNVG("civil_nvg", MilitaryArmorMaterials.MILITARY, ArmorItem.Type.HELMET);
    private static Item registerArmor(String name, MilitaryArmorMaterials material, ArmorItem.Type type) {
        return Registry.register(Registries.ITEM, new Identifier(MilitaryInsurgencyMod.MOD_ID, name), new ArmorItem(material, type, new FabricItemSettings()));
    }
    
    private static Item registerNVG(String name, MilitaryArmorMaterials material, ArmorItem.Type type) {
        return Registry.register(Registries.ITEM, new Identifier(MilitaryInsurgencyMod.MOD_ID, name), new NVGArmorItem(material, type, new FabricItemSettings()));
    }

    public static void registerModArmor() {
        MilitaryInsurgencyMod.LOGGER.info("Registering tactical armor for " + MilitaryInsurgencyMod.MOD_ID);
    }
}
