package fryantit.militaryinsurgency.item;

import fryantit.militaryinsurgency.MilitaryInsurgencyMod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MilitaryItems {
    public static final Item HARDENED_STEEL_PLATE = registerItem("hardened_steel_plate", new Item(new FabricItemSettings()));
    public static final Item KEVLAR_MESH = registerItem("kevlar_mesh", new Item(new FabricItemSettings()));
    public static final Item TITANIUM_INGOT = registerItem("titanium_ingot", new Item(new FabricItemSettings()));
    public static final Item CARBON_FIBER_SHEET = registerItem("carbon_fiber_sheet", new Item(new FabricItemSettings()));
    public static final Item COAL_COKE = registerItem("coal_coke", new Item(new FabricItemSettings()));
public static final Item COAL_COKE_DUST = registerItem("coal_coke_dust", new Item(new FabricItemSettings()));
    public static final Item MOLTEN_COAL_COKE = registerItem("molten_coal_coke", new Item(new FabricItemSettings()));
    public static final Item IRON_COAL_MIX = registerItem("iron_coal_mix", new Item(new FabricItemSettings()));
    public static final Item GOLD_PLATED_STEEL = registerItem("gold_plated_steel", new Item(new FabricItemSettings()));
    public static final Item TACTICAL_UPGRADE_TEMPLATE = registerItem("tactical_upgrade_template", 
    new Item(new FabricItemSettings().rarity(Rarity.EPIC)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MilitaryInsurgencyMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        MilitaryInsurgencyMod.LOGGER.info("Registering materials for " + MilitaryInsurgencyMod.MOD_ID);
    }
}