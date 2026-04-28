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
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    public static ModConfig CONFIG;
    private static KeyBinding toggleNvgKey;
    private static final Identifier NVG_OVERLAY = new Identifier("minecraft", "textures/misc/nvg_overlay.png");

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        GeoArmorRenderer.registerArmorRenderer(new CivilNVGRenderer(), fryantit.militaryinsurgency.item.ModItems.CIVIL_NVG);

        toggleNvgKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.militaryinsurgency.toggle_nvg",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "category.militaryinsurgency.general"
        ));

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
            boolean isActive = (helmet.getItem() instanceof NVGArmorItem) && helmet.getOrCreateNbt().getBoolean("nvg_active");

            /* Boost gamma to the limit to see through the lens */
            if (isActive) {
                client.options.getGamma().setValue(15.0);
            } else if (client.options.getGamma().getValue() > 1.0) {
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

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.options.hudHidden) return;

            ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
            if (helmet.getItem() instanceof NVGArmorItem && helmet.getOrCreateNbt().getBoolean("nvg_active")) {
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();

                /* Set the overlay to a very low Z-layer so the Hotbar draws on top of it */
                drawContext.getMatrices().push();
                drawContext.getMatrices().translate(0, 0, -500); // Move back in 3D space

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                /* Render Tone 1: Cyan Tint */
                drawContext.fill(0, 0, width, height, 0x3300FFFF);

                /* Render Tone 2: The Vignette Overlay */
                RenderSystem.setShaderTexture(0, NVG_OVERLAY);
                drawContext.drawTexture(NVG_OVERLAY, 0, 0, 0, 0, width, height, width, height);

                drawContext.getMatrices().pop();
                RenderSystem.disableBlend();
            }
        });
    }
}
