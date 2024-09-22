package com.bobby.artistweb.controller;

import com.bobby.artistweb.model.AboutMe;
import com.bobby.artistweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/aboutme")
    public AboutMe getAboutMe() {
        return this.userService.getAboutMe();
    }
}
