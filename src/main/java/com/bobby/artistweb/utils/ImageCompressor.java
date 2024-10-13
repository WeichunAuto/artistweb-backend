package com.bobby.artistweb.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ImageCompressor {

    /**
     * Compress the input image into a jpg image.
     * @param imageFile
     * @param compressionQuality
     * @throws IOException
     */
    // Method to compress and convert an image to JPEG format
    public static byte[] compressAndConvertToJpeg(MultipartFile imageFile, float compressionQuality) throws IOException {
        // Load the input image
        BufferedImage inputImage = ImageIO.read(imageFile.getInputStream());
        byte[] outputImageBytes = writeToJpgImage(compressionQuality, inputImage);
        return outputImageBytes;
    }

    /**
     * Generate a thumbnail image.
     * @param inputFile
     * @param outputFile
     * @param compressionQuality
     * @throws IOException
     */
    public static void generateThumbnails(File inputFile, File outputFile, float compressionQuality) throws IOException {
        // Load the input image (can be any format supported by ImageIO, such as PNG, BMP, etc.)
        BufferedImage image = ImageIO.read(inputFile);

        float originalWidth = Integer.valueOf(image.getWidth()).floatValue();
        float originalHeight = Integer.valueOf(image.getHeight()).floatValue();

        float targetThumbnailWidth = 300;
        float targetThumbnailHeight = (targetThumbnailWidth/originalWidth)*originalHeight;

        // Generate and save the thumbnail
        BufferedImage thumbnailImage = resizeImage(image, Float.valueOf(targetThumbnailWidth).intValue(), Float.valueOf(targetThumbnailHeight).intValue()); // Resize for thumbnail
        writeToJpgImage(compressionQuality, thumbnailImage);
    }

    private static byte[] writeToJpgImage(float compressionQuality, BufferedImage image) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Get a JPEG writer
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No writers found for JPEG format");
        }
        ImageWriter writer = writers.next();

        // Set the compression parameters
        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);  // Enable compression
            param.setCompressionQuality(compressionQuality);  // Set compression quality: 0.0 (max compression) to 1.0 (min compression)
        }
        // Write the compressed JPEG image to the output file
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
        }
        // Cleanup
        writer.dispose();
         // Convert ByteArrayOutputStream to byte[]
        return baos.toByteArray();
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
//        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resizedImage;
    }



    public static void main(String[] args) throws IOException {

        File file = new File("/Users/wangweichun/Desktop/Project/ImageTest/part-16.jpg");

        float compressionQuality = 0.05f;
        File outputImage = new File("/Users/wangweichun/Desktop/Project/ImageTest/output_compressed_" + compressionQuality + ".jpg");
        File thumbNailOutputImage = new File("/Users/wangweichun/Desktop/Project/ImageTest/output_compressed_bicubic300" + compressionQuality + "thumbNail.jpg");

//        ImageCompressor.compressAndConvertToJpeg(file, outputImage, compressionQuality);  // Set compression quality to 75%
//        ImageCompressor.generateThumbnails(file, thumbNailOutputImage, compressionQuality);  // Set compression quality to 75%

        System.out.println("Image compression and conversion completed.");
    }
}
