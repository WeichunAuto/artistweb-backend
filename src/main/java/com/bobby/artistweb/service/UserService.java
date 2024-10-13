package com.bobby.artistweb.service;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.AboutMe;
import com.bobby.artistweb.model.Applications;
import com.bobby.artistweb.model.UniqueValues;
import com.bobby.artistweb.repo.AboutMeRepo;
import com.bobby.artistweb.repo.ApplicationRepo;
import com.bobby.artistweb.repo.UniqueValuesRepo;
import com.bobby.artistweb.utils.ImageCompressor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private AboutMeRepo aboutMeRepo;

    @Autowired
    private ApplicationRepo applicationRepo;

    @Autowired
    private UniqueValuesRepo uniqueValuesRepo;

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

        AboutMe previousAboutMe = this.aboutMeRepo.findFirst();
        if(previousAboutMe == null) {
            this.aboutMeRepo.save(aboutMe);
        } else {
            previousAboutMe.setName(aboutMe.getName());
            previousAboutMe.setDescription(aboutMe.getDescription());
            previousAboutMe.setImageName(aboutMe.getImageName());
            previousAboutMe.setImageData(aboutMe.getImageData());
            previousAboutMe.setImageType(aboutMe.getImageType());
            previousAboutMe.setOptimizedImageName(aboutMe.getOptimizedImageName());
            previousAboutMe.setOptimizedImageData(aboutMe.getOptimizedImageData());
            previousAboutMe.setOptimizedImageType(aboutMe.getOptimizedImageType());
            this.aboutMeRepo.save(previousAboutMe);
        }
    }
}

