package fryantit.militaryinsurgency.armor;

import fryantit.militaryinsurgency.MilitaryInsurgencyMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MilitaryArmorItems {
    // 1. Register normal armor sets
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

    // 2. Register NVG with right-click equip logic
    public static final Item CIVIL_NVG = new NVGArmorItem(MilitaryArmorMaterials.MILITARY, ArmorItem.Type.HELMET, new Item.Settings().maxCount(1)) {
        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            ItemStack itemStack = user.getStackInHand(hand);
            EquipmentSlot equipmentSlot = EquipmentSlot.HEAD;
            ItemStack itemStack2 = user.getEquippedStack(equipmentSlot);
            if (itemStack2.isEmpty()) {
                user.equipStack(equipmentSlot, itemStack.copy());
                itemStack.setCount(0);
                return TypedActionResult.success(itemStack, world.isClient());
            }
            return TypedActionResult.fail(itemStack);
        }
    };

    private static Item registerArmor(String name, MilitaryArmorMaterials material, ArmorItem.Type type) {
        return Registry.register(Registries.ITEM, new Identifier(MilitaryInsurgencyMod.MOD_ID, name), new ArmorItem(material, type, new Item.Settings()));
    }

    public static void registerModArmor() {
        // Register NVG separately to ensure special logic is applied
        Registry.register(Registries.ITEM, new Identifier(MilitaryInsurgencyMod.MOD_ID, "civil_nvg"), CIVIL_NVG);

        // Add all items to the Combat Creative Tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(HARDENED_STEEL_HELMET);
            entries.add(HARDENED_STEEL_CHESTPLATE);
            entries.add(HARDENED_STEEL_LEGGINGS);
            entries.add(HARDENED_STEEL_BOOTS);
            entries.add(KEVLAR_HELMET);
            entries.add(KEVLAR_CHESTPLATE);
            entries.add(KEVLAR_LEGGINGS);
            entries.add(KEVLAR_BOOTS);
            entries.add(TITANIUM_HELMET);
            entries.add(TITANIUM_CHESTPLATE);
            entries.add(TITANIUM_LEGGINGS);
            entries.add(TITANIUM_BOOTS);
            entries.add(CARBON_FIBER_HELMET);
            entries.add(CARBON_FIBER_CHESTPLATE);
            entries.add(CARBON_FIBER_LEGGINGS);
            entries.add(CARBON_FIBER_BOOTS);
            entries.add(CIVIL_NVG);
        });
    }
}
