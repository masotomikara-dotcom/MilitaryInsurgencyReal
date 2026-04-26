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
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    public static ModConfig CONFIG;
    private static KeyBinding toggleNvgKey;
    private static float currentAmplification = 1.0f;

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

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (toggleNvgKey.wasPressed()) {
                ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
                if (helmet.getItem() instanceof NVGArmorItem) {
                    NbtCompound nbt = helmet.getOrCreateNbt();
                    boolean newState = !nbt.getBoolean("nvg_active");
                    nbt.putBoolean("nvg_active", newState);
                    
                    if (newState) {
                        client.player.playSound(SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, 1.0f, 2.0f);
                    } else {
                        client.player.playSound(SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, 1.0f, 2.0f);
                        currentAmplification = 1.0f;
                        client.options.getGamma().setValue(1.0);
                    }
                    client.player.sendMessage(Text.literal("NVG " + (newState ? "On" : "Off")), true);
                }
            }

            ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
            if (helmet.getItem() instanceof NVGArmorItem && helmet.getOrCreateNbt().getBoolean("nvg_active")) {
                currentAmplification = MathHelper.lerp(0.15f, currentAmplification, CONFIG.maxBrightness);
                client.options.getGamma().setValue((double) currentAmplification);
            } else if (currentAmplification > 1.0f) {
                currentAmplification = MathHelper.lerp(0.15f, currentAmplification, 1.0f);
                client.options.getGamma().setValue((double) currentAmplification);
            }
        });

        // Fixed anti-aliasing issue by rendering overlay BEFORE items.
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (!CONFIG.enableCyanOverlay) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
                if (helmet.getItem() instanceof NVGArmorItem && helmet.getOrCreateNbt().getBoolean("nvg_active")) {
                    int width = client.getWindow().getScaledWidth();
                    int height = client.getWindow().getScaledHeight();

                    // Pre-render state
                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();

                    // Draw Thinner Base Cyan Tint (0x2200FFFF) to avoid artifacts
                    drawContext.fill(0, 0, width, height, 0x2200FFFF); 

                    // Correct Vignette Lens using 4 gradients
                    int vW = (int)(width / 2.5);
                    int vH = (int)(height / 2.5);

                    // Top
                    drawContext.fillGradient(0, 0, width, vH, 0xCC000000, 0x00000000);
                    // Bottom
                    drawContext.fillGradient(0, height - vH, width, height, 0x00000000, 0xCC000000);
                    // Left
                    drawContext.fillGradient(0, 0, vW, height, 0xCC000000, 0x00000000);
                    // Right
                    drawContext.fillGradient(width - vW, 0, width, height, 0x00000000, 0xCC000000);

                    // Post-render state restore
                    RenderSystem.depthMask(true);
                    RenderSystem.enableDepthTest();
                }
            }
        });
    }
}
