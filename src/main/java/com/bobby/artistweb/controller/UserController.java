package com.bobby.artistweb.controller;

import com.bobby.artistweb.model.AboutMe;
import com.bobby.artistweb.model.UniqueValues;
import com.bobby.artistweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/aboutme")
    public AboutMe getAboutMe() {
        return this.userService.getAboutMe();
    }

    @PostMapping("/uniqueValues")
    public List<UniqueValues> getUniqueValues() {
        return (List<UniqueValues>) this.userService.getUniqueValues();
    }
}
