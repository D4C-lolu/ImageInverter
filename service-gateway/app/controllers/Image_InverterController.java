package controllers;

import com.encentral.image_inverter.api.IImage_Inverter;
import com.google.inject.Inject;
import io.swagger.annotations.*;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Api("ImageInverter")
public class Image_InverterController extends Controller {

    String dir = "assets/";

    @Inject
    IImage_Inverter imageInverter;

    public Image_InverterController() {
        try {
            Files.createDirectories(Paths.get(dir));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "Upload Image")
    @ApiResponses(
            value = {
                    @ApiResponse( code=201, message = "File upload successful", response = String.class),
                    @ApiResponse(code = 400, message = "Invalid file", response = String.class),
                    @ApiResponse(code = 404, message = "Image not found", response = String.class),
                    @ApiResponse(code = 500, message = "Error occurred saving file", response = String.class)
            }
    )
    @ApiImplicitParam(
            name = "image",
            value = "Image-file",
            dataType = "file",
            paramType = "form",
            required = true
    )
    public Result handleUpload() {

        Http.MultipartFormData<File> formData = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> imageFile = formData.getFile("image");

        if (imageFile == null) {
            return badRequest("No image found in the request");
        }
        String contentType = imageFile.getContentType();
        File file = imageFile.getFile();
        if (!contentType.startsWith("image/") || imageInverter.isImage(file)) {
            return badRequest("Only image files are allowed");
        }

        String fileName = "D4C" + "_" + imageFile.getFilename() + "_" + UUID.randomUUID().toString();
        Path destinationPath = Paths.get(dir, fileName);

        try {
            Files.move(file.toPath(), destinationPath);
        } catch (IOException e) {
            return internalServerError("Unable to save file");
        }
        String invertedFilename = "inverted_" + fileName;
        imageInverter.invertImage(destinationPath.toString(), invertedFilename);
        return ok("Image upload successful: " + invertedFilename);
    }

    @ApiOperation(value= "Download Image")
    @ApiResponses(
            value = {
                    @ApiResponse( code=201, message = "File retrieved", response = String.class),
                    @ApiResponse(code = 404, message = "Image not found", response = String.class)
            }
    )

    public Result handleDownload(@ApiParam(value = "File name", required = true) String imgURI) {
        File file = new File(dir, imgURI);
        if (!file.exists()) {
            return notFound("Image not found");
        }
        return ok(file);
    }
}
