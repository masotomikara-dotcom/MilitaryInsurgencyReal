package com.example.mod.client.model;

import com.example.mod.item.CivilNVGItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class CivilNVGModel extends GeoModel<CivilNVGItem> {
    @Override
    public Identifier getModelResource(CivilNVGItem animatable) {
        // Model JSON is in mod namespace
        return new Identifier("militaryinsurgency", "geo/civil_nvg.geo.json");
    }

    @Override
    public Identifier getTextureResource(CivilNVGItem animatable) {
        // Armor texture is in minecraft namespace as requested
        return new Identifier("minecraft", "textures/models/armor/civil_nvg.png");
    }

    @Override
    public Identifier getAnimationResource(CivilNVGItem animatable) {
        return new Identifier("militaryinsurgency", "animations/civil_nvg.animation.json");
    }
}
