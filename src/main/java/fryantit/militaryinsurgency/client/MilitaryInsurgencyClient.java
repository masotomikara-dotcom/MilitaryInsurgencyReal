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
import fryantit.militaryinsurgency.armor.MilitaryArmorItems;
import fryantit.militaryinsurgency.client.renderer.NVGItemRenderer;
import fryantit.militaryinsurgency.mixin.GameRendererAccessor; // Import Accessor mới
import org.lwjgl.glfw.GLFW;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    private static KeyBinding toggleNvgKey;
    private static float currentAmplification = 1.0f;
    private static float targetAmplification = 1.0f;
    private static final float ADAPTATION_SPEED = 0.05f;

    @Override
    public void onInitializeClient() {
        toggleNvgKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.militaryinsurgency.toggle_nvg", 
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N, 
                "category.militaryinsurgency.general" 
        ));

        BuiltinItemRendererRegistry.INSTANCE.register(MilitaryArmorItems.CIVIL_NVG, new NVGItemRenderer());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            while (toggleNvgKey.wasPressed()) {
                ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
                if (helmet.getItem() instanceof NVGArmorItem) {
                    NbtCompound nbt = helmet.getOrCreateNbt();
                    boolean newState = !nbt.getBoolean("nvg_active");
                    nbt.putBoolean("nvg_active", newState);
                    
                    if (newState) {
                        ((GameRendererAccessor)client.gameRenderer).callLoadPostProcessor(new Identifier("minecraft", "shaders/post/nvg_white.json"));
                        client.player.setPitch(client.player.getPitch() + 2.0f);
                        client.player.playSound(SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, 1.0f, 2.0f);
                    } else {
                        client.gameRenderer.disablePostProcessor();
                        client.player.playSound(SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, 1.0f, 2.0f);
                        client.options.getGamma().setValue(1.0);
                    }
                    client.player.sendMessage(Text.literal("NVG " + (newState ? "Activated" : "Deactivated")), true);
                }
            }
        });
    }
}
