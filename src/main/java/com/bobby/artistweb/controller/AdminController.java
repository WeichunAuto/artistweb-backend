package com.bobby.artistweb.controller;

import com.bobby.artistweb.model.PaintWork;
import com.bobby.artistweb.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")

public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/isTokenValid")
    public boolean isTokenValid(HttpServletRequest request){
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(isValidToken){
            System.out.println("Token verify passed.");
        } else {
            System.out.println("Token verify failed.");
        }
        return isValidToken;
    }

    @PostMapping(value="/addPaintWork")
    @ResponseBody
    public ResponseEntity<?> addPaintWork(@RequestPart(value="paintWork") PaintWork paintWork,
                                          @RequestPart MultipartFile imageFile, HttpServletRequest request) {
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(isValidToken){
            System.out.println("Token verify passed.");
        } else {
            System.out.println("Token verify failed.");
            return new ResponseEntity<>("inValidToken", HttpStatus.UNAUTHORIZED);
        }

        PaintWork savedPaintWork = null;

        try {
            System.out.println(imageFile.getSize());
            savedPaintWork = this.adminService.addPaintWork(paintWork, imageFile);
            return new ResponseEntity<>(savedPaintWork, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
