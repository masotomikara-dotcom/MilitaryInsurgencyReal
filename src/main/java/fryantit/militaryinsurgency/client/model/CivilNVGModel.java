package fryantit.militaryinsurgency.client.model;

import fryantit.militaryinsurgency.armor.NVGArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class CivilNVGModel extends GeoModel<NVGArmorItem> {
    @Override
    public Identifier getModelResource(NVGArmorItem animatable) {
        // Model JSON is in mod namespace
        return new Identifier("militaryinsurgency", "geo/civil_nvg.geo.json");
    }

    @Override
    public Identifier getTextureResource(NVGArmorItem animatable) {
        // Armor texture is in minecraft namespace as requested
        return new Identifier("minecraft", "textures/models/armor/civil_nvg.png");
    }

    @Override
    public Identifier getAnimationResource(NVGArmorItem animatable) {
        return new Identifier("militaryinsurgency", "animations/civil_nvg.animation.json");
    }
}
