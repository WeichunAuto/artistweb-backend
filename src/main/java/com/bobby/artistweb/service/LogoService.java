package com.bobby.artistweb.service;

import com.bobby.artistweb.model.Logo;
import com.bobby.artistweb.repo.LogoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class LogoService {

    @Autowired
    private LogoRepo logoRepo;

    public void saveLogoImage(MultipartFile logoImage) throws IOException {
        Logo savedLogo = null;
        if(this.logoRepo.findAll().size() == 0) {
            savedLogo = new Logo();
        } else {
            savedLogo = this.logoRepo.findAll().get(0);
        }
        savedLogo.setImageName(logoImage.getOriginalFilename());
        savedLogo.setImageType(logoImage.getContentType());
        savedLogo.setImageData(logoImage.getBytes());
        this.logoRepo.save(savedLogo);
    }

    public Logo fetchSavedLogo() {
        Logo savedLogo = null;
        if(this.logoRepo.findAll().size() != 0) {
            savedLogo = this.logoRepo.findAll().get(0);
        }
        return savedLogo;
    }
}
