package fryantit.militaryinsurgency.client.model;

import fryantit.militaryinsurgency.MilitaryInsurgencyMod;
import fryantit.militaryinsurgency.item.custom.NVGItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class CivilNVGModel extends GeoModel<NVGItem> {
    @Override
    public Identifier getModelResource(NVGItem animatable) {
        return new Identifier(MilitaryInsurgencyMod.MOD_ID, "geo/civil_nvg.geo.json");
    }

    @Override
    public Identifier getTextureResource(NVGItem animatable) {
        return new Identifier(MilitaryInsurgencyMod.MOD_ID, "textures/item/civil_nvg.png");
    }

    @Override
    public Identifier getAnimationResource(NVGItem animatable) {
        return new Identifier(MilitaryInsurgencyMod.MOD_ID, "animations/civil_nvg.animation.json");
    }
}
