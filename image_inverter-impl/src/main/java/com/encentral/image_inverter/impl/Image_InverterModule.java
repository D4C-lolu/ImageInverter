package com.encentral.image_inverter.impl;

import com.encentral.image_inverter.api.IImage_Inverter;
import com.google.inject.AbstractModule;

public class Image_InverterModule extends AbstractModule {
    @Override
    protected void configure(){
        bind(IImage_Inverter.class).to(DefaultImage_InverterImpl.class);
    }
}
