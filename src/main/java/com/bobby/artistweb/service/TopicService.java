package com.bobby.artistweb.service;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.Topic;
import com.bobby.artistweb.model.TopicDTO;
import com.bobby.artistweb.model.TopicImageDTO;
import com.bobby.artistweb.repo.TopicRepo;
import com.bobby.artistweb.utils.ImageCompressor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TopicService {

    private final TopicRepo topicRepo;

    public void saveTopic(Topic topic, MultipartFile imageFile) throws ImageTypeDoesNotSupportException, IOException {
        String contentType = imageFile.getContentType();
        if (!contentType.toLowerCase().equals("image/jpeg")) {
            throw new ImageTypeDoesNotSupportException("Please only upload jpeg image files!");
        }

        String originalFilename = imageFile.getOriginalFilename();
        topic.setImageName(originalFilename);
        topic.setImageType(imageFile.getContentType());
        topic.setImageData(imageFile.getBytes());

        float compressionQuality = 0.1f;
        byte[] optimizedImageBytes = ImageCompressor.compressAndConvertToJpeg(imageFile, compressionQuality);
        topic.setOptimizedImageName(originalFilename.substring(0, originalFilename.lastIndexOf('.'))+ "optimized.jpg");
        topic.setOptimizedImageType("image/jpeg");
        topic.setOptimizedImageData(optimizedImageBytes);

        this.topicRepo.save(topic);
    }

    public List<TopicDTO> fetchTitleAndDescriptionInTopic() {
        List<TopicDTO> topicDtos = (List<TopicDTO>) this.topicRepo.findAboutMeOnlyNameAndDescription();
        return topicDtos;
    }

    public TopicImageDTO fetchPhotoInTopic(long id) {
        TopicImageDTO topicImage = this.topicRepo.findTopicPhotoById(id);
        return topicImage;
    }

    public void deleteTopicById(long id) {
        this.topicRepo.deleteById(id);
    }
}
