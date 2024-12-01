package com.bobby.artistweb.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController extends BaseController {


    @PostMapping("/isTokenValid")
    public ResponseEntity<String> isTokenValid(HttpServletRequest request){
        return ResponseEntity.ok("Token verify passed.");
    }








}
