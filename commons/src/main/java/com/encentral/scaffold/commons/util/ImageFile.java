package com.encentral.scaffold.commons.util;

import com.google.common.io.Files;

import java.io.File;

/**
 * Utility class for holding a regular image
 */
public class ImageFile {
    private  File imageFile;

    private  File invertedImageFile;

    public ImageFile(File imageFile, File invertedImageFile) {
        this.imageFile = imageFile;
        this.invertedImageFile = invertedImageFile;
    }

    public File getImageFile() {
        return imageFile;
    }

    public File getInvertedImageFile(){
        return invertedImageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public void setInvertedImageFile(File invertedImageFile) {
        this.invertedImageFile = invertedImageFile;
    }

    public String getExtension(){
        return Files.getFileExtension(imageFile.getName());
    }
}
