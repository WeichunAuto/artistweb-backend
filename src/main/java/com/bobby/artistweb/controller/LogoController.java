package com.bobby.artistweb.controller;

import com.bobby.artistweb.model.Logo;
import com.bobby.artistweb.service.LogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class LogoController extends BaseController {

    @Autowired
    private LogoService logoService;

    @PostMapping("/setLogoImage")
    public ResponseEntity<String> setLogoImage(@RequestParam MultipartFile logoImage) {
        try {
            this.logoService.saveLogoImage(logoImage);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @GetMapping("/fetchSavedLogo/image")
    public ResponseEntity<byte[]> getSavedLogoImage() {


        Logo logo = this.logoService.fetchSavedLogo();

        if (logo != null && logo.getImageData() != null) {
            String imageType = logo.getImageType();

            MediaType mediaType;

            switch (imageType.toLowerCase()) {
                case "image/jpeg":
                case "image:jpg":
                    mediaType = MediaType.IMAGE_JPEG;
                    break;
                case "image/png":
                    mediaType = MediaType.IMAGE_PNG;
                    break;

                default:
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;  // Default fallback
                    break;
            }

            HttpHeaders headers = new HttpHeaders(); // Set the appropriate content type in the headers
            headers.setContentType(mediaType);
            return new ResponseEntity<>(logo.getImageData(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
