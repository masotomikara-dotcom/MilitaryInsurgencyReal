package fryantit.militaryinsurgency.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundEvent;
import java.util.function.Supplier;

public enum MilitaryArmorMaterials implements ArmorMaterial {
    MILITARY("military", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> Ingredient.ofItems(Items.IRON_INGOT)),
    HARDENED_STEEL("hardened_steel", 25, new int[]{3, 6, 7, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F, 0.0F, () -> Ingredient.ofItems(Items.IRON_INGOT)), // Tạm sửa MilitaryItems thành Items để tránh lỗi cyclic
    KEVLAR("kevlar", 35, new int[]{3, 7, 9, 3}, 12, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2.5F, 0.05F, () -> Ingredient.ofItems(Items.LEATHER)),
    TITANIUM("titanium", 40, new int[]{4, 7, 9, 4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3.5F, 0.1F, () -> Ingredient.ofItems(Items.IRON_INGOT)),
    CARBON_FIBER("carbon_fiber", 37, new int[]{4, 8, 10, 4}, 18, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 4.0F, 0.15F, () -> Ingredient.ofItems(Items.NETHERITE_INGOT));

}
