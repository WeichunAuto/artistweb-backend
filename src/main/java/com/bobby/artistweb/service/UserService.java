package com.bobby.artistweb.service;

import com.bobby.artistweb.model.AboutMe;
import com.bobby.artistweb.repo.AboutMeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private AboutMeRepo aboutMeRepo;

    public AboutMe getAboutMe() {
        return this.aboutMeRepo.findAll().get(0);
    }
}

