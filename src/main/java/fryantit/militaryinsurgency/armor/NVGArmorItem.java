package fryantit.militaryinsurgency.armor;

import net.minecraft.entity.Entity;
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

    // Right-click to toggle NVG state in NBT
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            NbtCompound nbt = stack.getOrCreateNbt();
            boolean isActive = nbt.getBoolean("nvg_active");
            nbt.putBoolean("nvg_active", !isActive);
        }
        return TypedActionResult.success(stack);
    }

    // No Night Vision potion effect here. 
    // Brightness is managed by Gamma in the Client class.
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        // Keeps the method clean to avoid vanilla interference
    }
}
