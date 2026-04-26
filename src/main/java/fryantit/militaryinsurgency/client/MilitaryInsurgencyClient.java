package fryantit.militaryinsurgency.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import fryantit.militaryinsurgency.armor.NVGArmorItem;
import fryantit.militaryinsurgency.armor.MilitaryArmorItems; // Matches your modified file
import fryantit.militaryinsurgency.client.renderer.NVGItemRenderer;
import org.lwjgl.glfw.GLFW;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    private static KeyBinding toggleNvgKey;
    
    // Smooth Light Adaptation Variables
    private static float currentAmplification = 1.0f;
    private static float targetAmplification = 1.0f;
    private static final float ADAPTATION_SPEED = 0.05f; // Transition takes ~0.5s

    @Override
    public void onInitializeClient() {
        // 1. Register the Keybinding (Press N to toggle)
        toggleNvgKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.militaryinsurgency.toggle_nvg", 
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N, 
                "category.militaryinsurgency.general" 
        ));

        // 2. Register the 3D Item Renderer
        // This ensures the model from NVGModel.java is used for the item
        BuiltinItemRendererRegistry.INSTANCE.register(MilitaryArmorItems.CIVIL_NVG, new NVGItemRenderer());

        // 3. Handle Key Events and Light Adaptation Logic
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            // Handle Toggle Press
            while (toggleNvgKey.wasPressed()) {
                ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
                if (helmet.getItem() instanceof NVGArmorItem) {
                    NbtCompound nbt = helmet.getOrCreateNbt();
                    boolean newState = !nbt.getBoolean("nvg_active");
                    nbt.putBoolean("nvg_active", newState);
                    
                    if (newState) {
                        // Load Cyan Shader
                        client.gameRenderer.loadPostProcessor(new Identifier("minecraft", "shaders/post/nvg_white.json"));
                        client.player.setPitch(client.player.getPitch() + 2.0f);
                        client.player.playSound(SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, 1.0f, 2.0f);
                    } else {
                        // Clear Shader and reset Gamma
                        client.gameRenderer.disablePostProcessor();
                        client.player.playSound(SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, 1.0f, 2.0f);
                        client.options.getGamma().setValue(1.0);
                    }
                    client.player.sendMessage(Text.literal("NVG " + (newState ? "Activated" : "Deactivated")), true);
                }
            }

            // Continuous Light Adaptation (Image Intensifier Logic)
            ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
            if (helmet.getItem() instanceof NVGArmorItem && helmet.getOrCreateNbt().getBoolean("nvg_active")) {
                int lightLevel = client.world.getLightLevel(client.player.getBlockPos());
                float ambientBrightness = lightLevel / 15.0f;

                // Amplify light: Darker areas get 8x boost (800%)
                // Brighter areas reduce amplification to avoid blinding
                targetAmplification = (ambientBrightness < 0.1f) ? 8.0f : (1.0f / (ambientBrightness * 2.0f));
                targetAmplification = MathHelper.clamp(targetAmplification, 1.0f, 8.0f);

                // Apply smooth transition (Lerp)
                currentAmplification = MathHelper.lerp(ADAPTATION_SPEED, currentAmplification, targetAmplification);
                client.options.getGamma().setValue((double) currentAmplification);
            }
        });

        // 4. Render Lens Overlay (AORKOS Vignette)
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
                if (helmet.getItem() instanceof NVGArmorItem && helmet.getOrCreateNbt().getBoolean("nvg_active")) {
                    Identifier vignette = new Identifier("militaryinsurgency", "textures/misc/nvg_vignette.png");
                    int w = client.getWindow().getScaledWidth();
                    int h = client.getWindow().getScaledHeight();

                    // Force texture to Black to fix the white border issue
                    RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
                    drawContext.drawTexture(vignette, 0, 0, 0, 0, w, h, w, h);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        });
    }
}
