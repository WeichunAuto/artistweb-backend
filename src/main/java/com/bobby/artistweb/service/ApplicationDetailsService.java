package com.bobby.artistweb.service;

import com.bobby.artistweb.model.ApplicationPrincipal;
import com.bobby.artistweb.model.Applications;
import com.bobby.artistweb.repo.ApplicationRepo;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationDetailsService implements UserDetailsService {

    @Autowired
    private ApplicationRepo applicationRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Applications app = this.applicationRepo.findByAppName(username);
        if (app == null) {
            System.out.println(username + " not found!");
            throw new UsernameNotFoundException(username + " not found!");
        }
        return new ApplicationPrincipal(app);
    }
}
