package fryantit.militaryinsurgency.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // Since we want zero-dependency, we point directly to Minecraft's Keybind Screen
            // This allows users to change the 'N' key easily from Mod Menu
            return new net.minecraft.client.gui.screen.option.ControlsOptionsScreen(parent, net.minecraft.client.options.GameOptions.getOptions());
        };
    }
}
