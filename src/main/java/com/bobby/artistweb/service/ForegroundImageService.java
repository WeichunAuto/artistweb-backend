package com.bobby.artistweb.service;

import com.bobby.artistweb.model.ForegroundImage;
import com.bobby.artistweb.repo.ForegroundImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ForegroundImageService {

    @Autowired
    private ForegroundImageRepo foregroundImageRepo;

    public void saveForegroundImage(MultipartFile foregroundImage) throws IOException {
        ForegroundImage savedForegroundImage = null;
        if(this.foregroundImageRepo.findAll().size() == 0) {
            savedForegroundImage = new ForegroundImage();
        } else {
            savedForegroundImage = this.foregroundImageRepo.findAll().get(0);
        }
        savedForegroundImage.setImageName(foregroundImage.getOriginalFilename());
        savedForegroundImage.setImageType(foregroundImage.getContentType());
        savedForegroundImage.setImageData(foregroundImage.getBytes());
        this.foregroundImageRepo.save(savedForegroundImage);
    }

    public ForegroundImage fetchSavedForegroundImage() {
        ForegroundImage savedForegroundImage = null;
        if(this.foregroundImageRepo.findAll().size() != 0) {
            savedForegroundImage = this.foregroundImageRepo.findAll().get(0);
        }
        return savedForegroundImage;
    }
}
