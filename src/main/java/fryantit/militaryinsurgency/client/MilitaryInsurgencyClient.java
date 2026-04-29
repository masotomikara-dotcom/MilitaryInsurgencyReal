package fryantit.militaryinsurgency.client;

import net.fabricmc.api.ClientModInitializer;

public class MilitaryInsurgencyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Safe zone: Item handles itself to prevent early NullPointer access
    }
}
