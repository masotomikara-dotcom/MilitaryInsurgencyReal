package fryantit.militaryinsurgency.client.renderer;

import fryantit.militaryinsurgency.client.model.CivilNVGModel;
import fryantit.militaryinsurgency.armor.NVGArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer; // Fixed import path

public class CivilNVGRenderer extends GeoArmorRenderer<NVGArmorItem> {
    public CivilNVGRenderer() {
        super(new CivilNVGModel());
    }
}
