package fryantit.militaryinsurgency.client;

import com.mojang.blaze3d.systems.RenderSystem;
import fryantit.militaryinsurgency.armor.NVGArmorItem;
import fryantit.militaryinsurgency.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    public static ModConfig CONFIG;
    private static KeyBinding toggleNvgKey;

    @Override
    public void onInitializeClient() {
        // Initialize Cloth Config
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        // Register Keybinding for NVG toggle
        toggleNvgKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.militaryinsurgency.toggle_nvg", 
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N, 
                "category.militaryinsurgency.general" 
        ));

        // Logic for toggling NVG and playing sounds
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            while (toggleNvgKey.wasPressed()) {
                ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
                if (helmet.getItem() instanceof NVGArmorItem) {
                    NbtCompound nbt = helmet.getOrCreateNbt();
                    boolean newState = !nbt.getBoolean("nvg_active");
                    nbt.putBoolean("nvg_active", newState);
                    
                    float pitch = newState ? 2.0f : 1.5f;
                    client.player.playSound(newState ? SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE : SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, 1.0f, pitch);
                    client.player.sendMessage(Text.literal("NVG Mode: " + (newState ? "Active" : "Deactivated")), true);
                }
            }
        });

        // HUD Rendering: Multi-tone Vision Effect (Black, White, Cyan shades)
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            
            ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
            if (helmet.getItem() instanceof NVGArmorItem && helmet.getOrCreateNbt().getBoolean("nvg_active")) {
                int w = client.getWindow().getScaledWidth();
                int h = client.getWindow().getScaledHeight();
                
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                // 1. Base Multi-tone Layer: Cyan filtered by Config Brightness
                // Using a lighter cyan-white mix for the base tint
                int intensity = (int)(CONFIG.maxBrightness * 30); 
                int baseColor = (intensity << 24) | 0x88FFFF; // Light Cyan-White mix
                drawContext.fill(0, 0, w, h, baseColor);

                // 2. High-Contrast Center Highlight (White-Cyan Peak)
                // This simulates the "Bright" part of the 3-color palette
                drawContext.fillGradient(w/2 - 100, h/2 - 100, w/2 + 100, h/2 + 100, 0x44FFFFFF, 0x0000FFFF);

                // 3. Circular Lens Shadow (Vignette) - Transition from Cyan to Deep Black
                // Side Vignette (Left & Right)
                int vWidth = w / 3;
                // Deep Black edges
                drawContext.fill(0, 0, vWidth / 2, h, 0xEE000000); 
                drawContext.fill(w - (vWidth / 2), 0, w, h, 0xEE000000);
                
                // Cyan-to-Black Gradient transition (The "Diverse" shades)
                drawContext.fillGradient(vWidth / 2, 0, vWidth + 40, h, 0xEE000000, 0x0000FFFF);
                drawContext.fillGradient(w - vWidth - 40, 0, w - (vWidth / 2), h, 0x0000FFFF, 0xEE000000);

                // Top & Bottom Vignette for rounded feel
                int vHeight = h / 4;
                drawContext.fillGradient(0, 0, w, vHeight, 0xDD000000, 0x0000FFFF);
                drawContext.fillGradient(0, h - vHeight, w, h, 0x0000FFFF, 0xDD000000);

                // 4. Center Grain/Shade: Adding a subtle white-cyan overlay for more color variety
                drawContext.fill(w/4, h/4, w - (w/4), h - (h/4), 0x11FFFFFF);
                
                RenderSystem.disableBlend();
            }
        });
    }
}
