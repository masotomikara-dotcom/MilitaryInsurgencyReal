package fryantit.militaryinsurgency.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "militaryinsurgency")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public float maxBrightness = 2.0f;

    @ConfigEntry.Gui.Tooltip
    public boolean enableCyanOverlay = true;
}
