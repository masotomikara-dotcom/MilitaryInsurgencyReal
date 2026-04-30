package fryantit.militaryinsurgency.item.custom;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.core.animation.AnimatableManager;
import java.util.function.Consumer;
import java.util.function.Supplier;
import fryantit.militaryinsurgency.client.renderer.CivilNVGItemRenderer;

public class NVGItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public NVGItem(Settings settings) {
        super(settings);
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private CivilNVGItemRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new CivilNVGItemRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
