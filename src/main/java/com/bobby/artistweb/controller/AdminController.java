package com.bobby.artistweb.controller;

import com.bobby.artistweb.model.PaintWork;
import com.bobby.artistweb.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")

public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/isTokenValid")
    public ResponseEntity<String> isTokenValid(HttpServletRequest request){
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("Token verify failed.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token verify failed.");
        }
        return ResponseEntity.ok("Token verify passed.");
    }

    @PostMapping(value="/addPaintWork")
    @ResponseBody
    public ResponseEntity<?> addPaintWork(@RequestPart(value="paintWork") PaintWork paintWork,
                                          @RequestPart MultipartFile imageFile, HttpServletRequest request) {
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("addPaintWork: Token verify failed.");
            return new ResponseEntity<>("inValidToken", HttpStatus.UNAUTHORIZED);
        }

        PaintWork savedPaintWork = null;
        try {
            savedPaintWork = this.adminService.addPaintWork(paintWork, imageFile);
            return new ResponseEntity<>(savedPaintWork, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MaxUploadSizeExceededException e) {
            System.out.println("Max upload size exceeded........");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/fetchPaintWorks")
    @ResponseBody
    public ResponseEntity<List<PaintWork>> fetchPaintWorks(HttpServletRequest request) {
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("fetchPaintWorks: Token verify failed.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<PaintWork> paintWorksList = this.adminService.fetchAllPaintWorks();

        return new ResponseEntity<>(paintWorksList, HttpStatus.OK);
    }

    @GetMapping("/getAPaintWork/{id}/image")
    public ResponseEntity<byte[]> getAPaintWork(@PathVariable int id) {
        PaintWork paintWork = this.adminService.getPaintWorkById(id);
        if (paintWork != null && paintWork.getImageData() != null) {
            String imageType = paintWork.getImageType(); // Determine the content type based on the imageType field

            MediaType mediaType;

            switch (imageType.toLowerCase()) {
                case "image/jpeg":
                case "image:jpg":
                    mediaType = MediaType.IMAGE_JPEG;
                    break;
                case "image/png":
                    mediaType = MediaType.IMAGE_PNG;
                    break;
                default:
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;  // Default fallback
                    break;
            }
            HttpHeaders headers = new HttpHeaders(); // Set the appropriate content type in the headers
            headers.setContentType(mediaType);
            return new ResponseEntity<>(paintWork.getImageData(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteAPaintWork/{id}")
    public ResponseEntity<String> deletePaintWork(@PathVariable int id, HttpServletRequest request) {
        boolean isValidToken = (boolean) request.getAttribute("isValidToken");
        if(!isValidToken){
            System.out.println("deleteAPaintWork: Token verify failed.");
            return new ResponseEntity<>("inValidToken", HttpStatus.UNAUTHORIZED);
        }

        PaintWork paintWork = this.adminService.getPaintWorkById(id);
        if(paintWork.getId() == -1) {
            return new ResponseEntity<>("Failed", HttpStatus.NOT_FOUND);
        } else {
            this.adminService.deletePaintWorkById(id);
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
    }
}
