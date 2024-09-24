package com.bobby.artistweb.service;

import com.bobby.artistweb.model.AboutMe;
import com.bobby.artistweb.model.Applications;
import com.bobby.artistweb.repo.AboutMeRepo;
import com.bobby.artistweb.repo.ApplicationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private AboutMeRepo aboutMeRepo;

    @Autowired
    private ApplicationRepo applicationRepo;

    public AboutMe getAboutMe() {
        return this.aboutMeRepo.findAll().get(0);
    }

    public Applications getApplication() {
        return this.applicationRepo.findAll().get(0);
    }
}

