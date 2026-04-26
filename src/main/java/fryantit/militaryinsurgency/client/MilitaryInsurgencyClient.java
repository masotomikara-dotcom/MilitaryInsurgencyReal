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
                    
                    // English: Sound effects without vanilla night vision status
                    client.player.playSound(newState ? SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE : SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, 1.0f, 2.0f);
                    client.player.sendMessage(Text.literal("NVG: " + (newState ? "ON" : "OFF")), true);
                }
            }
        });

        // English: HUD Rendering with 3-tone color logic (Black, White, Cyan)
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            
            ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
            if (helmet.getItem() instanceof NVGArmorItem && helmet.getOrCreateNbt().getBoolean("nvg_active")) {
                int w = client.getWindow().getScaledWidth();
                int h = client.getWindow().getScaledHeight();
                
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                // English: 1. Render 4-way Vignette first to define the lens shape (Black Tone)
                // This prevents the "TikTok PC" vertical line issue
                drawContext.fillGradient(0, 0, w, h / 2, 0xFF000000, 0x00000000); // Top
                drawContext.fillGradient(0, h / 2, w, h, 0x00000000, 0xFF000000); // Bottom
                drawContext.fillGradient(0, 0, w / 2, h, 0xFF000000, 0x00000000); // Left
                drawContext.fillGradient(w / 2, 0, w, h, 0x00000000, 0xFF000000); // Right

                // English: 2. Render Main Color Tint (Cyan Tone)
                // Brightness is controlled by the alpha of this single layer
                int alpha = (int)(CONFIG.maxBrightness * 25); 
                drawContext.fill(0, 0, w, h, (alpha << 24) | 0x00FFFF);

                // English: 3. Render Center Glow (White Tone)
                // Subtle highlight in the middle to create depth
                drawContext.fillGradient(w/2 - 60, h/2 - 60, w/2 + 60, h/2 + 60, 0x33FFFFFF, 0x00FFFFFF);

                RenderSystem.disableBlend();
            }
        });
    }
}
