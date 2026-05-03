package fryantit.militaryinsurgency.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import fryantit.militaryinsurgency.MilitaryInsurgencyMod;

@Mixin(TitleScreen.class)
public class MilitaryInsurgencyMixin {
    @Inject(at = @At("TAIL"), method = "init")
    private void onInit(CallbackInfo info) {
        // Log to confirm mod is working
        MilitaryInsurgencyMod.LOGGER.info("Military Insurgency: Menu loaded successfully!");
    }
}
