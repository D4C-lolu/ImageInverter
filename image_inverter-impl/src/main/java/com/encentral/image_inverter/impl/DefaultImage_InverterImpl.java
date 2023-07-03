package com.encentral.image_inverter.impl;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.encentral.image_inverter.api.IImage_Inverter;
import com.encentral.image_inverter.impl.actor.ImageInverterActor;
import com.encentral.scaffold.commons.util.ImageFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DefaultImage_InverterImpl implements IImage_Inverter {

    @Override
    public void invertImage(String inputFilePath, String outputFilePath){

        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        if (!inputFile.exists()) {
            System.out.println("Input file does not exist.");
            return;
        }

        // Check if the output file already exists
        if (outputFile.exists()) {
            System.out.println("Output file already exists. Please choose a different output path.");
            return;
        }

        // Create the ActorSystem
        ActorSystem system = ActorSystem.create("ImageInversionSystem");
        // Create the ImageInverter actor
        ActorRef imageInverterActor = system.actorOf(ImageInverterActor.props(), "imageInverter");
        imageInverterActor.tell(new ImageFile(inputFile, outputFile), ActorRef.noSender());
    }

    @Override
    public boolean isImage(File file){
        try {
            ImageIO.read(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
