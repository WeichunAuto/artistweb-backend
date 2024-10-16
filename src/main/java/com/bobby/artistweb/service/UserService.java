package com.bobby.artistweb.service;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.*;
import com.bobby.artistweb.repo.*;
import com.bobby.artistweb.utils.GmailSender;
import com.bobby.artistweb.utils.ImageCompressor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class UserService {

    @Autowired
    private GmailSender gmailSender;

    @Autowired
    private AboutMeRepo aboutMeRepo;

    @Autowired
    private TopicRepo topicRepo;

    @Autowired
    private ApplicationRepo applicationRepo;

    @Autowired
    private UniqueValuesRepo uniqueValuesRepo;

    @Autowired
    private ContactMeRepo contactMeRepo;

    public AboutMe getAboutMe() {
        return this.aboutMeRepo.findAll().get(0);
    }

    public Applications getApplication() {
        return this.applicationRepo.findAll().get(0);
    }

    public List<UniqueValues> getUniqueValues() {
        return (List<UniqueValues>) this.uniqueValuesRepo.findAll();
    }

    public void saveAboutMe(AboutMe aboutMe, MultipartFile imageFile) throws IOException, ImageTypeDoesNotSupportException {
        if(imageFile != null) {
            String contentType = imageFile.getContentType();
            if (!contentType.equals("image/jpeg")) {
                throw new ImageTypeDoesNotSupportException("Please only upload jpeg image files!");
            }
            String originalFilename = imageFile.getOriginalFilename();
            aboutMe.setImageName(originalFilename);
            aboutMe.setImageType(imageFile.getContentType());
            aboutMe.setImageData(imageFile.getBytes());

            float compressionQuality = 0.1f;
            byte[] optimizedImageBytes = ImageCompressor.compressAndConvertToJpeg(imageFile, compressionQuality);
            aboutMe.setOptimizedImageName(originalFilename.substring(0, originalFilename.lastIndexOf('.'))+ "optimized.jpg");
            aboutMe.setOptimizedImageType("image/jpeg");
            aboutMe.setOptimizedImageData(optimizedImageBytes);
        }
        AboutMe previousAboutMe = this.aboutMeRepo.findFirst();

        if(previousAboutMe == null) {
            this.aboutMeRepo.save(aboutMe);
        } else {
            previousAboutMe.setName(aboutMe.getName());
            previousAboutMe.setDescription(aboutMe.getDescription());
            if(imageFile != null) {
                previousAboutMe.setImageName(aboutMe.getImageName());
                previousAboutMe.setImageData(aboutMe.getImageData());
                previousAboutMe.setImageType(aboutMe.getImageType());
                previousAboutMe.setOptimizedImageName(aboutMe.getOptimizedImageName());
                previousAboutMe.setOptimizedImageData(aboutMe.getOptimizedImageData());
                previousAboutMe.setOptimizedImageType(aboutMe.getOptimizedImageType());
            }

            this.aboutMeRepo.save(previousAboutMe);
        }
    }

    public AboutMeDTO fetchNamdAndDescInAboutMe() {
        AboutMeDTO aboutMeDto = this.aboutMeRepo.findAboutMeOnlyNameAndDescription();
        return aboutMeDto;
    }

    public AboutMeImageDTO fetchProfilePhotoInAboutMe(int id) {
        AboutMeImageDTO aboutMeImageDto = this.aboutMeRepo.findAboutMeProfilePhotoById(id);
        return aboutMeImageDto;
    }

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

    public TopicImageDTO fetchPhotoInTopic(int id) {
        TopicImageDTO topicImage = this.topicRepo.findTopicPhotoById(id);
        return topicImage;
    }

    public void deleteTopicById(int id) {
        this.topicRepo.deleteById(id);
    }

    public void saveMessage(ContactMe contactMe) throws MessagingException {
        this.contactMeRepo.save(contactMe);

        // start a timer schedule to send an email after 5 seconds
        Timer timer = new Timer(false);
        timer.schedule(new TimerTask() {

            @SneakyThrows
            @Override
            public void run() {
                gmailSender.setContactMe(contactMe);
                gmailSender.sendEmail();
                timer.cancel();
            }
        }, 5000);
    }
}

