package com.bobby.artistweb.controller;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.AboutMe;
import com.bobby.artistweb.model.AboutMeDTO;
import com.bobby.artistweb.model.AboutMeImageDTO;
import com.bobby.artistweb.service.AboutMeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class AboutMeController extends BaseController {

    private final AboutMeService aboutMeService;

    @PostMapping("/createAboutMe")
    public ResponseEntity<String> createAboutMe(@RequestPart(value="aboutMe") AboutMe aboutMe,
                                                @RequestPart(value="imageFile", required = false ) MultipartFile imageFile, HttpServletRequest request) {
        try {
            this.aboutMeService.saveAboutMe(aboutMe, imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ImageTypeDoesNotSupportException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @GetMapping("/fetchAboutMe")
    public ResponseEntity<AboutMeDTO> fetchAboutMe(HttpServletRequest request) {
        AboutMeDTO aboutMe = this.aboutMeService.fetchNamdAndDescInAboutMe();
        if (aboutMe == null) {
            return new ResponseEntity<>(new AboutMeDTO(-1, "Unknown", "Unknown"), HttpStatus.OK);
        }
        return new ResponseEntity<>(aboutMe, HttpStatus.OK);
    }

    @GetMapping("/getProfilePhoto/{id}/image")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable int id) {
        AboutMeImageDTO aboutMeImageDTO = this.aboutMeService.fetchProfilePhotoInAboutMe(id);
        if (aboutMeImageDTO != null && aboutMeImageDTO.getOptimizedImageData() != null) {
            String imageType = aboutMeImageDTO.getOptimizedImageType();

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
            return new ResponseEntity<>(aboutMeImageDTO.getOptimizedImageData(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
