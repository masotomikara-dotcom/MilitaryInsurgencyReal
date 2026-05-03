package fryantit.militaryinsurgency.client;

import net.fabricmc.api.ClientModInitializer;
import fryantit.militaryinsurgency.item.MilitaryItems;
import fryantit.militaryinsurgency.client.renderer.CivilNVGItemRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BuiltinItemRendererRegistry.INSTANCE.register(MilitaryItems.CIVIL_NVG, new CivilNVGItemRenderer());
    }
}
