package com.bobby.artistweb.controller;

import com.bobby.artistweb.model.Applications;

import com.bobby.artistweb.model.JwtObject;
import com.bobby.artistweb.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;


@Data
@NoArgsConstructor
@AllArgsConstructor

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class JwtController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @PostMapping("login")
    public JwtObject login(@RequestBody Applications app) {
         //Authenticate Appkey
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(app.getAppName(), app.getAppKey()));

        if (authentication.isAuthenticated()) {
            JwtObject jwtObject = this.jwtService.generateToken(app.getAppName());
            System.out.println(jwtObject);
            return jwtObject;
        } else {
            System.out.println("authentication faild!");
            return new JwtObject();
        }
    }
}
