package com.bobby.artistweb.controller;

import com.bobby.artistweb.model.ForegroundImage;
import com.bobby.artistweb.service.ForegroundImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ForegroundImageController extends BaseController{

    @Autowired
    private ForegroundImageService foregroundImageService;

    @PostMapping("/setForegroundImage")
    public ResponseEntity<String> setForegroundImage(@RequestParam MultipartFile foregroundImage) {
        try {
            this.foregroundImageService.saveForegroundImage(foregroundImage);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @GetMapping("/fetchSavedForegroundImage/image")
    public ResponseEntity<byte[]> getSavedForegroundImage() {
        ForegroundImage foregroundImage = this.foregroundImageService.fetchSavedForegroundImage();

        if (foregroundImage != null && foregroundImage.getImageData() != null) {
            String imageType = foregroundImage.getImageType();

            MediaType mediaType;

            switch (imageType.toLowerCase()) {
                case "image/jpeg":
                case "image:jpg":
                    mediaType = MediaType.IMAGE_JPEG;
                    break;
                default:
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;  // Default fallback
                    break;
            }
            HttpHeaders headers = new HttpHeaders(); // Set the appropriate content type in the headers
            headers.setContentType(mediaType);
            return new ResponseEntity<>(foregroundImage.getImageData(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
