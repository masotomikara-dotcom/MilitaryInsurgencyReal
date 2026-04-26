package fryantit.militaryinsurgency.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import fryantit.militaryinsurgency.armor.NVGArmorItem;

public class NVGModel<T extends LivingEntity> extends EntityModel<T> {
    private final ModelPart head;
    private final ModelPart group;
    private final ModelPart kaka;
    private float foldProgress = 0f;

    public NVGModel(ModelPart root) {
        super(RenderLayer::getArmorCutoutNoCull);
        this.head = root.getChild("head");
        this.group = this.head.getChild("group");
        this.kaka = this.head.getChild("kaka");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        // Base Head - Parent of everything
        ModelPartData headData = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        // Group (FOLDED UP) - Pivot points set to forehead area
        ModelPartData groupData = headData.addChild("group", 
            ModelPartBuilder.create()
                .uv(12, 13).cuboid(-3.99F, -21.317F, -0.783F, 0.99F, 2.0F, 1.0F)
                .uv(10, 10).cuboid(-4.05F, -20.117F, 0.217F, 4.1F, 0.688F, 0.4F)
                .uv(12, 16).cuboid(-2.5F, -20.117F, 0.617F, 1.0F, 0.15F, 0.7F)
                .uv(12, 11).cuboid(-4.0F, -20.7169F, -0.8829F, 1.0999F, 1.3998F, 1.1998F)
                .uv(10, 15).cuboid(-4.0999F, -20.7169F, -0.783F, 0.0999F, 1.3998F, 1.1998F)
                .uv(0, 16).cuboid(-0.99F, -22.327F, -0.783F, 0.99F, 0.0F, 1.0F)
                .uv(0, 16).cuboid(-3.99F, -22.327F, -0.783F, 0.99F, 0.0F, 1.0F)
                .uv(12, 11).cuboid(-1.0999F, -20.7169F, -0.8829F, 1.1998F, 1.3998F, 1.1998F)
                .uv(16, 11).cuboid(-3.99F, -20.327F, -0.783F, 0.99F, 0.0F, 1.0F)
                .uv(8, 13).cuboid(-4.0999F, -22.3169F, -0.8829F, 1.1998F, 0.9878F, 1.1998F)
                .uv(10, 9).cuboid(-4.1F, -20.318F, 0.117F, 4.2F, 1.0F, 0.3F)
                .uv(12, 13).cuboid(-0.99F, -21.317F, -0.783F, 0.99F, 2.0F, 1.0F)
                .uv(8, 13).cuboid(-1.0999F, -22.3169F, -0.8829F, 1.1998F, 0.9878F, 1.1998F),
            ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        // Kaka (FOLDED DOWN) - Using same boxes but triggered by visibility/animation
        ModelPartData kakaData = headData.addChild("kaka", 
            ModelPartBuilder.create()
                .uv(0, 11).cuboid(-10.09F, -26.5452F, 5.244F, 0.99F, 1.0F, 2.0F)
                .uv(0, 11).cuboid(-13.09F, -26.5452F, 5.244F, 0.99F, 1.0F, 2.0F)
                .uv(16, 13).cuboid(-10.09F, -26.5452F, 4.234F, 0.99F, 1.0F, 0.0F)
                .uv(16, 13).cuboid(-13.09F, -26.5452F, 4.234F, 0.99F, 1.0F, 0.0F)
                .uv(0, 14).cuboid(-13.1999F, -26.6451F, 4.2441F, 1.1998F, 1.1998F, 0.9878F)
                .uv(8, 11).cuboid(-10.1999F, -26.6451F, 5.8441F, 1.1998F, 1.1998F, 1.3998F)
                .uv(0, 14).cuboid(-10.1999F, -26.6451F, 4.2441F, 1.1998F, 1.1998F, 0.9878F)
                .uv(8, 11).cuboid(-13.1F, -26.6451F, 5.8441F, 1.0999F, 1.1998F, 1.3998F)
                .uv(0, 9).cuboid(-13.2F, -26.7452F, 6.243F, 4.2F, 0.3F, 1.0F)
                .uv(0, 10).cuboid(-13.15F, -26.9452F, 6.444F, 4.1F, 0.4F, 0.688F)
                .uv(0, 0).cuboid(-11.6F, -27.3452F, 6.444F, 1.0F, 0.4F, 0.15F)
                .uv(8, 15).cuboid(-13.1999F, -26.745F, 5.8441F, 0.0999F, 1.1998F, 1.3998F),
            ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = netHeadYaw * 0.017453292F;

        ItemStack helmet = entity.getEquippedStack(EquipmentSlot.HEAD);
        if (helmet.getItem() instanceof NVGArmorItem) {
            boolean active = helmet.getOrCreateNbt().getBoolean("nvg_active");
            
            float target = active ? 1.0f : 0.0f;
            foldProgress = MathHelper.lerp(0.15f, foldProgress, target);

            // Rotate based on progress
            float rotation = (1.0f - foldProgress) * -1.57f;
            this.group.pitch = rotation;
            this.kaka.pitch = rotation;

            this.kaka.visible = foldProgress > 0.5f;
            this.group.visible = foldProgress <= 0.5f;
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
