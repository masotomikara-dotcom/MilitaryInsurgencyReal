package fryantit.militaryinsurgency.client;

import fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer;
import fryantit.militaryinsurgency.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Correct registration for GeckoLib 4.x on Fabric
        GeoArmorRenderer.registerArmorRenderer(new CivilNVGRenderer(), ModItems.NVG_ITEM);
    }
}
