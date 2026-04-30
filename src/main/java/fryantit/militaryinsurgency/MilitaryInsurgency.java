package fryantit.militaryinsurgency;

import fryantit.militaryinsurgency.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MilitaryInsurgency implements ModInitializer {
    public static final String MOD_ID = "militaryinsurgency";
    public static final Logger LOGGER = LoggerFactory.LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
    }
}
