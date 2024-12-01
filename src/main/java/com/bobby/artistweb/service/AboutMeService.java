package com.bobby.artistweb.service;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.AboutMe;
import com.bobby.artistweb.model.AboutMeDTO;
import com.bobby.artistweb.model.AboutMeImageDTO;
import com.bobby.artistweb.repo.AboutMeRepo;
import com.bobby.artistweb.utils.ImageCompressor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class AboutMeService {

    private final AboutMeRepo aboutMeRepo;

    public AboutMe getAboutMe() {
        return this.aboutMeRepo.findAll().get(0);
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
}
