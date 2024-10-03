package com.bobby.artistweb.controller;

import com.bobby.artistweb.model.Applications;

import com.bobby.artistweb.model.JwtObject;
import com.bobby.artistweb.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<?> login(@RequestBody Applications app) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(app.getAppName(), app.getAppKey()));

            if (authentication.isAuthenticated()) {
                JwtObject jwtObject = this.jwtService.generateToken(app.getAppName());
                System.out.println(jwtObject);
                return new ResponseEntity<>(jwtObject, HttpStatus.OK);
            } else {
                System.out.println("authentication faild!");
            }
        } catch(BadCredentialsException e) {
            System.out.println("Invalid credentialsssss: " + e.getMessage());
            return new ResponseEntity<>(new JwtObject(), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(new JwtObject(), HttpStatus.UNAUTHORIZED);
    }
}
