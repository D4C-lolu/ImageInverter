package com.encentral.image_inverter.impl.actor;


import akka.actor.AbstractActor;
import akka.actor.Props;
import com.encentral.scaffold.commons.util.ImageFile;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageInverterActor extends AbstractActor {

    public static Props props() {
        return Props.create(ImageInverterActor.class, ImageInverterActor::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ImageFile.class, this::invertImage)
                .build();
    }

    private void invertImage(ImageFile message) {
        File imageFile = message.getImageFile();

        try {
            // Read the original image
            BufferedImage originalImage = ImageIO.read(imageFile);

            // Invert the image
            BufferedImage invertedImage = new BufferedImage(
                    originalImage.getWidth(),
                    originalImage.getHeight(),
                    originalImage.getType()
            );

            for (int y = 0; y < originalImage.getHeight(); y++) {
                for (int x = 0; x < originalImage.getWidth(); x++) {
                    int rgb = originalImage.getRGB(x, y);
                    int invertedRgb = ~rgb;
                    invertedImage.setRGB(x, y, invertedRgb);
                }
            }

            ImageIO.write(invertedImage, message.getExtension(),message.getInvertedImageFile());
            sender().tell(message.getInvertedImageFile().getName(), self());
        } catch (Exception e) {
            sender().tell(e.getMessage(), self());
        }
    }
}

