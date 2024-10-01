package com.bobby.artistweb.service;

import com.bobby.artistweb.model.AboutMe;
import com.bobby.artistweb.model.Applications;
import com.bobby.artistweb.model.UniqueValues;
import com.bobby.artistweb.repo.AboutMeRepo;
import com.bobby.artistweb.repo.ApplicationRepo;
import com.bobby.artistweb.repo.UniqueValuesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

