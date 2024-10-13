package com.bobby.artistweb.controller;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.*;
import com.bobby.artistweb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createAboutMe")
    public ResponseEntity<String> createAboutMe(@RequestPart(value="aboutMe") AboutMe aboutMe,
                                                @RequestPart MultipartFile imageFile, HttpServletRequest request) {
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("createAboutMe: Token verify failed.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            this.userService.saveAboutMe(aboutMe, imageFile);
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
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("fetchAboutMe: Token verify failed.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        AboutMeDTO aboutMe = this.userService.fetchNamdAndDescInAboutMe();

        return new ResponseEntity<>(aboutMe, HttpStatus.OK);
    }

    @GetMapping("/getProfilePhoto/{id}/image")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable int id) {
        AboutMeImageDTO aboutMeImageDTO = this.userService.fetchProfilePhotoInAboutMe(id);
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

    @PostMapping("/uniqueValues")
    public List<UniqueValues> getUniqueValues() {
        return (List<UniqueValues>) this.userService.getUniqueValues();
    }
}
