package fryantit.militaryinsurgency.client;

import fryantit.militaryinsurgency.client.renderer.CivilNVGRenderer;
import fryantit.militaryinsurgency.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Registering the 3D renderer using Fabric's official API
        // This ensures the renderer is loaded before the game tries to draw the item
        ArmorRenderer.register(new CivilNVGRenderer(), ModItems.NVG_ITEM);
    }
}
