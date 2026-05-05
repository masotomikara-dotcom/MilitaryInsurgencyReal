package fryantit.militaryinsurgency.mixin;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.util.Identifier;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Invoker("loadPostProcessor")
    void invokeLoadPostProcessor(Identifier identifier);
}
