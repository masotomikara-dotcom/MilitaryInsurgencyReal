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
import org.lwjgl.glfw.GLFW;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    public static ModConfig CONFIG;
    private static KeyBinding toggleNvgKey;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        toggleNvgKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.militaryinsurgency.toggle_nvg", 
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N, 
                "category.militaryinsurgency.general" 
        ));

        // Actual Brightness Boost Logic
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
            boolean isActive = helmet.getItem() instanceof NVGArmorItem && helmet.getOrCreateNbt().getBoolean("nvg_active");

            if (isActive) {
                // Force extreme brightness (3 shades logic)
                client.options.getGamma().setValue(Math.min(15.0, (double)CONFIG.maxBrightness * 2.5));
            } else {
                client.options.getGamma().setValue(1.0);
            }

            while (toggleNvgKey.wasPressed()) {
                if (helmet.getItem() instanceof NVGArmorItem) {
                    NbtCompound nbt = helmet.getOrCreateNbt();
                    boolean newState = !nbt.getBoolean("nvg_active");
                    nbt.putBoolean("nvg_active", newState);
                    client.player.playSound(newState ? SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE : SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, 1.0f, 2.0f);
                }
            }
        });

        // Professional 3-Tone Vignette (No more "TikTok PC" window)
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.options.hudHidden) return;
            
            ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
            if (helmet.getItem() instanceof NVGArmorItem && helmet.getOrCreateNbt().getBoolean("nvg_active")) {
                int w = client.getWindow().getScaledWidth();
                int h = client.getWindow().getScaledHeight();
                
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                // 1. Diverse Tint (Cyan-White shade)
                int alpha = (int)(CONFIG.maxBrightness * 15); 
                drawContext.fill(0, 0, w, h, (alpha << 24) | 0x33FFFF); 

                // 2. Soft Circular Vignette using loop (Diverse shades of Black/Cyan)
                for (int i = 0; i < 50; i += 2) {
                    int gradAlpha = Math.min(220, 255 - (i * 4));
                    int blackColor = (gradAlpha << 24);
                    
                    // Smoothly blend edges towards center
                    drawContext.fillGradient(0, i, w, i + 2, blackColor, 0x0000FFFF); // Top
                    drawContext.fillGradient(0, h - i - 2, w, h - i, 0x0000FFFF, blackColor); // Bottom
                    drawContext.fillGradient(i, 0, i + 2, h, blackColor, 0x0000FFFF); // Left
                    drawContext.fillGradient(w - i - 2, 0, w, h, 0x0000FFFF, blackColor); // Right
                }

                // 3. Center Glow (White shade)
                drawContext.fillGradient(w/2 - 40, h/2 - 40, w/2 + 40, h/2 + 40, 0x22FFFFFF, 0x00FFFFFF);

                RenderSystem.disableBlend();
            }
        });
    }
}
