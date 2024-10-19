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

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createAboutMe")
    public ResponseEntity<String> createAboutMe(@RequestPart(value="aboutMe") AboutMe aboutMe,
                                                @RequestPart(value="imageFile", required = false ) MultipartFile imageFile, HttpServletRequest request) {
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
        if (aboutMe == null) {
            return new ResponseEntity<>(new AboutMeDTO(-1, "Unknown", "Unknown"), HttpStatus.OK);
        }
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

    @PostMapping("/addTopic")
    public ResponseEntity<String> addTopic(@RequestPart(value="topic") Topic topic,
                                           @RequestPart(value="imageFile") MultipartFile imageFile, HttpServletRequest request) {
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("addTopic: Token verify failed.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            this.userService.saveTopic(topic, imageFile);
        } catch (ImageTypeDoesNotSupportException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @GetMapping("/fetchTopics")
    @ResponseBody
    public ResponseEntity<List<TopicDTO>> fetchTopics(HttpServletRequest request) {
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("fetchTopics: Token verify failed.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<TopicDTO> topicDtoList = this.userService.fetchTitleAndDescriptionInTopic();

        return new ResponseEntity<>(topicDtoList, HttpStatus.OK);
    }

    @GetMapping("/getATopic/{id}/image")
    public ResponseEntity<byte[]> getATopicImage(@PathVariable int id, HttpServletRequest request) {
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("getATopic: Token verify failed.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        TopicImageDTO topicImageDto = this.userService.fetchPhotoInTopic(id);

        if (topicImageDto != null && topicImageDto.getOptimizedImageData() != null) {
            String imageType = topicImageDto.getOptimizedImageType();

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
            return new ResponseEntity<>(topicImageDto.getOptimizedImageData(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteTopic/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable int id, HttpServletRequest request) {
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("deleteTopic: Token verify failed.");
            return new ResponseEntity<>("inValidToken", HttpStatus.UNAUTHORIZED);
        }
        this.userService.deleteTopicById(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestPart ContactMe contactMe, HttpServletRequest request) {
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("sendMessage: Token verify failed.");
            return new ResponseEntity<>("inValidToken", HttpStatus.UNAUTHORIZED);
        }
        try {
            this.userService.saveMessage(contactMe);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @GetMapping("/fetchMessages")
    @ResponseBody
    public ResponseEntity<List<ContactMe>> fetchMessages(HttpServletRequest request) {
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("sendMessage: Token verify failed.");
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        List<ContactMe> messages = this.userService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
