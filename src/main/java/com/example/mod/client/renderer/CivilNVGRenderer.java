package com.example.mod.client.renderer;

import com.example.mod.client.model.CivilNVGModel;
import com.example.mod.item.CivilNVGItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer; // Fixed import path

public class CivilNVGRenderer extends GeoArmorRenderer<CivilNVGItem> {
    public CivilNVGRenderer() {
        super(new CivilNVGModel());
    }
}
