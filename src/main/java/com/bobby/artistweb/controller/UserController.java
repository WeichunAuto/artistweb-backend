package com.bobby.artistweb.controller;

import com.bobby.artistweb.model.AboutMe;
import com.bobby.artistweb.model.Applications;
import com.bobby.artistweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/aboutme")
    public AboutMe getAboutMe(@RequestBody Applications app) {
        System.out.println(app);
        return this.userService.getAboutMe();
    }
}
