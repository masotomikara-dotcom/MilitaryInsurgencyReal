package fryantit.militaryinsurgency.armor;

import fryantit.militaryinsurgency.item.MilitaryItems;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

public enum MilitaryArmorMaterials implements ArmorMaterial {
    HARDENED_STEEL("hardened_steel", 25, new int[]{3, 6, 7, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F, 0.0F, () -> Ingredient.ofItems(MilitaryItems.HARDENED_STEEL_PLATE)),
    KEVLAR("kevlar", 35, new int[]{3, 7, 9, 3}, 12, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2.5F, 0.05F, () -> Ingredient.ofItems(MilitaryItems.KEVLAR_MESH)),
    TITANIUM("titanium", 40, new int[]{4, 7, 9, 4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3.5F, 0.1F, () -> Ingredient.ofItems(MilitaryItems.TITANIUM_INGOT)),
    CARBON_FIBER("carbon_fiber", 37, new int[]{4, 8, 10, 4}, 18, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 4.0F, 0.15F, () -> Ingredient.ofItems(MilitaryItems.CARBON_FIBER_SHEET));

    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredientSupplier;

    MilitaryArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredientSupplier) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredientSupplier = repairIngredientSupplier;
    }

    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY[type.getEquipmentSlot().getEntitySlotId()] * this.durabilityMultiplier;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return this.protectionAmounts[type.getEquipmentSlot().getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredientSupplier.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
