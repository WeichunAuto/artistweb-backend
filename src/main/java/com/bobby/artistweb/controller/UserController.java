package com.bobby.artistweb.controller;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.AboutMe;
import com.bobby.artistweb.model.PaintWork;
import com.bobby.artistweb.model.UniqueValues;
import com.bobby.artistweb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createAboutMe")
    public ResponseEntity<String> createAboutMe(@RequestPart(value="aboutMe") AboutMe aboutMe,
                                                @RequestPart MultipartFile imageFile, HttpServletRequest request) {
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("createAboutMe: Token verify failed.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            this.userService.saveAboutMe(aboutMe, imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ImageTypeDoesNotSupportException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @PostMapping("/uniqueValues")
    public List<UniqueValues> getUniqueValues() {
        return (List<UniqueValues>) this.userService.getUniqueValues();
    }
}
