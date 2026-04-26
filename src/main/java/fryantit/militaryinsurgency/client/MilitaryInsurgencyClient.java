package fryantit.militaryinsurgency.client;

import com.mojang.blaze3d.systems.RenderSystem;
import fryantit.militaryinsurgency.armor.NVGArmorItem;
import fryantit.militaryinsurgency.config.ModConfig;
import fryantit.militaryinsurgency.mixin.GameRendererAccessor;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    public static ModConfig CONFIG;
    private static KeyBinding toggleNvgKey;
    private static float currentAmplification = 1.0f;

    @Override
    public void onInitializeClient() {
        // 1. Initialize Cloth Config
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        // 2. Register Keybinding
        toggleNvgKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.militaryinsurgency.toggle_nvg", 
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N, 
                "category.militaryinsurgency.general" 
        ));

        // 3. NVG Toggle and Fullbright logic
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (toggleNvgKey.wasPressed()) {
                ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
                if (helmet.getItem() instanceof NVGArmorItem) {
                    NbtCompound nbt = helmet.getOrCreateNbt();
                    boolean newState = !nbt.getBoolean("nvg_active");
                    nbt.putBoolean("nvg_active", newState);
                    
                    if (newState) {
                        ((GameRendererAccessor)client.gameRenderer).callLoadPostProcessor(new Identifier("minecraft", "shaders/post/creeper.json"));
                        client.player.playSound(SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, 1.0f, 2.0f);
                    } else {
                        client.gameRenderer.disablePostProcessor();
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

        // 4. HUD Render Callback with Vignette Lens Effect
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (!CONFIG.enableCyanOverlay) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
                if (helmet.getItem() instanceof NVGArmorItem && helmet.getOrCreateNbt().getBoolean("nvg_active")) {
                    int width = client.getWindow().getScaledWidth();
                    int height = client.getWindow().getScaledHeight();

                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();

                    // Draw Base Cyan Tint
                    drawContext.fill(0, 0, width, height, 0x6600FFFF); 

                    // Draw Vignette Lens (Dark Corners) using Gradients
                    // Top shadow
                    drawContext.fillGradient(0, 0, width, height / 3, 0xAA000000, 0x00000000);
                    // Bottom shadow
                    drawContext.fillGradient(0, height - (height / 3), width, height, 0x00000000, 0xAA000000);
                    // Left shadow
                    drawContext.fillGradient(0, 0, width / 3, height, 0xAA000000, 0x00000000);
                    // Right shadow
                    drawContext.fillGradient(width - (width / 3), 0, width, height, 0x00000000, 0xAA000000);

                    RenderSystem.depthMask(true);
                    RenderSystem.enableDepthTest();
                }
            }
        });
    }
}
