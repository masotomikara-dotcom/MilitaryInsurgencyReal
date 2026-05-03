package fryantit.militaryinsurgency.client;

import net.fabricmc.api.ClientModInitializer;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BuiltInItemRendererRegistry.INSTANCE.register(MilitaryItems.CIVIL_NVG, new CivilNVGItemRenderer());
    }
}
