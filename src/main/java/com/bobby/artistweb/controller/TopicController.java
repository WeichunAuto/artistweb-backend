package com.bobby.artistweb.controller;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.Topic;
import com.bobby.artistweb.model.TopicDTO;
import com.bobby.artistweb.model.TopicImageDTO;
import com.bobby.artistweb.service.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TopicController extends BaseController{
    private final TopicService topicService;

    @PostMapping("/addTopic")
    public ResponseEntity<String> addTopic(@RequestPart(value="topic") Topic topic,
                                           @RequestPart(value="imageFile") MultipartFile imageFile, HttpServletRequest request) {
        try {
            this.topicService.saveTopic(topic, imageFile);
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
        List<TopicDTO> topicDtoList = this.topicService.fetchTitleAndDescriptionInTopic();

        return new ResponseEntity<>(topicDtoList, HttpStatus.OK);
    }

    @GetMapping("/getATopic/{id}/image")
    public ResponseEntity<byte[]> getATopicImage(@PathVariable int id, HttpServletRequest request) {
        TopicImageDTO topicImageDto = this.topicService.fetchPhotoInTopic(id);

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
        this.topicService.deleteTopicById(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
