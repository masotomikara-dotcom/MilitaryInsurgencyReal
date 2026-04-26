package fryantit.militaryinsurgency.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "militaryinsurgency")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 6) 
    public float maxBrightness = 6.0f; 
}
