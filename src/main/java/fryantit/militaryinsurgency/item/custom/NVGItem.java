package fryantit.militaryinsurgency.item.custom;

import fryantit.militaryinsurgency.client.renderer.CivilNVGItemRenderer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class NVGItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private CivilNVGItemRenderer renderer;

    public NVGItem(Properties properties) {
        super(properties);
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new CivilNVGItemRenderer();
                }
                return renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
