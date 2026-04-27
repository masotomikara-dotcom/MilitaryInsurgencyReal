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
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    public static ModConfig CONFIG;
    private static KeyBinding toggleNvgKey;
    // Resource location for your "Golden" PNG overlay
    private static final Identifier NVG_OVERLAY = new Identifier("militaryinsurgency", "textures/misc/nvg_overlay.png");

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

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
            boolean isActive = helmet.getItem() instanceof NVGArmorItem && helmet.getOrCreateNbt().getBoolean("nvg_active");

            // Logic: Multiply current gamma by 6, capped at 15.0 to replace Night Vision effect
            if (isActive) {
                double currentGamma = client.options.getGamma().getValue();
                client.options.getGamma().setValue(Math.min(15.0, currentGamma * 6.0)); 
            } else if (client.options.getGamma().getValue() > 1.0) {
                client.options.getGamma().setValue(1.0); // Reset to standard brightness
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

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.options.hudHidden) return;
            
            ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
            if (helmet.getItem() instanceof NVGArmorItem && helmet.getOrCreateNbt().getBoolean("nvg_active")) {
                int w = client.getWindow().getScaledWidth();
                int h = client.getWindow().getScaledHeight();
                
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                // 1. Render Cyan tint (Tone 1) - Replaces old "window" drawing logic
                drawContext.fill(0, 0, w, h, 0x4400FFFF); 

                // 2. Render Black PNG Overlay (Tone 2) - Naturally anti-aliased via texture
                RenderSystem.setShaderTexture(0, NVG_OVERLAY);
                drawContext.drawTexture(NVG_OVERLAY, 0, 0, 0, 0, w, h, w, h);
                
                // Note: White tone (Tone 3) is provided by the game's brightened world (Gamma)

                RenderSystem.disableBlend();
            }
        });
    }
}
