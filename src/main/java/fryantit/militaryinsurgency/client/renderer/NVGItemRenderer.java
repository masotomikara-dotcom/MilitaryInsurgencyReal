package fryantit.militaryinsurgency.client.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import fryantit.militaryinsurgency.client.model.NVGModel;

public class NVGItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    private final NVGModel model;
    // Ensure this path matches your texture location
    private final Identifier texture = new Identifier("militaryinsurgency", "textures/models/armor/civil_nvg.png");

    public NVGItemRenderer() {
        // Initialize the Java model using the Head layer as a base
        this.model = new NVGModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(net.minecraft.client.render.entity.model.EntityModelLayers.PLAYER_HEAD));
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        // 1. Basic Orientation: Minecraft's Java models are often upside down compared to items
        matrices.translate(0.5, 0.5, 0.5);
        matrices.scale(1.0f, -1.0f, -1.0f);

        // 2. The JSON 'display' settings you made will automatically move/rotate the model from here!
        
        // 3. Draw the actual model
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(texture)), light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        
        matrices.pop();
    }
}
