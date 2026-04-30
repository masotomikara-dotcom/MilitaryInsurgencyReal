package fryantit.militaryinsurgency.client.renderer;

import fryantit.militaryinsurgency.item.custom.NVGItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import fryantit.militaryinsurgency.client.model.CivilNVGModel;

public class CivilNVGItemRenderer extends GeoItemRenderer<NVGItem> {
    public CivilNVGItemRenderer() {
        super(new CivilNVGModel());
    }
}
