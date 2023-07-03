package com.encentral.image_inverter.api;

import java.io.File;

public interface IImage_Inverter {
    void invertImage(String inputFilepath, String outputFilepath);

    boolean isImage(File file);
}
