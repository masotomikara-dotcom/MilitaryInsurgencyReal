package fryantit.militaryinsurgency.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class NVGArmorItem extends ArmorItem {
    public NVGArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    // This method handles the Right-Click action to toggle the NVG state via NBT
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        
        if (!world.isClient) {
            // Access or create the NBT data of the item
            NbtCompound nbt = stack.getOrCreateNbt();
            boolean isActive = nbt.getBoolean("nvg_active");
            
            // Toggle the boolean value
            nbt.putBoolean("nvg_active", !isActive);
            
            // Optional: Play a sound effect to confirm the toggle
            // world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
        return TypedActionResult.success(stack);
    }

    // This method runs every tick while the item is present in the player's inventory
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player) {
            // Check if the player is currently wearing this specific helmet
            ItemStack helmet = player.getEquippedStack(EquipmentSlot.HEAD);
            
            if (helmet == stack) {
                NbtCompound nbt = stack.getOrCreateNbt();
                // If the NBT toggle is 'true', apply the Night Vision effect
                if (nbt.getBoolean("nvg_active")) {
                    // Duration is 210 ticks (approx 10s) to prevent flickering, with 'ambient' and 'no particles' set to true
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 210, 0, false, false, true));
                }
            }
        }
    }
}
