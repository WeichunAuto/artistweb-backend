package com.bobby.artistweb.controller;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.*;
import com.bobby.artistweb.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/isTokenValid")
    public ResponseEntity<String> isTokenValid(HttpServletRequest request){
        return ResponseEntity.ok("Token verify passed.");
    }

    @PostMapping(value="/addPaintWork")
    @ResponseBody
    public ResponseEntity<?> addPaintWork(@RequestPart(value="paintWork") PaintWork paintWork,
                                          @RequestPart MultipartFile imageFile, HttpServletRequest request) {

        PaintWork savedPaintWork = null;
        try {
            savedPaintWork = this.adminService.addPaintWork(paintWork, imageFile);
            return new ResponseEntity<>(savedPaintWork, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MaxUploadSizeExceededException e) {
            System.out.println("Max upload size exceeded........");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ImageTypeDoesNotSupportException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/fetchPaintWorks")
    @ResponseBody
    public ResponseEntity<List<PaintWorkDTO>> fetchPaintWorks() {
        List<PaintWorkDTO> paintWorksList = this.adminService.fetchAllPaintWorks();

        return new ResponseEntity<>(paintWorksList, HttpStatus.OK);
    }

    @GetMapping("/getPaintWorkCover/{id}/image")
    public ResponseEntity<byte[]> getAPaintWork(@PathVariable int id) {
        PaintWorkDecorationImageDTO decorationImageDTO = this.adminService.getPaintWorkCoverById(id);
        if (decorationImageDTO != null && decorationImageDTO.getImageData() != null) {

            MediaType mediaType = MediaType.IMAGE_JPEG;

            HttpHeaders headers = new HttpHeaders(); // Set the appropriate content type in the headers
            headers.setContentType(mediaType);
            return new ResponseEntity<>(decorationImageDTO.getImageData(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteAPaintWork/{id}")
    public ResponseEntity<String> deletePaintWork(@PathVariable int id) {
        Optional<PaintWork> paintWork = this.adminService.findPaintWorkById(id);
        if(paintWork == null || !paintWork.isPresent()) {
            return new ResponseEntity<>("Failed", HttpStatus.NOT_FOUND);
        } else {
            this.adminService.deletePaintWorkById(id);
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
    }

    @PostMapping("/addDecoration/{paintWorkId}")
    public ResponseEntity<String> addDecoration(@PathVariable int paintWorkId, @RequestPart(value="imageFile") MultipartFile imageFile) {
        try {
            this.adminService.saveDecoration(paintWorkId, imageFile);
        } catch (ImageTypeDoesNotSupportException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @GetMapping("/fetchDecorations/{paintWorkId}")
    public ResponseEntity<List<PaintWorkDecorationDTO>> fetchDecorations(@PathVariable int paintWorkId) {
        List<PaintWorkDecorationDTO> decorationsList = this.adminService.fetchDecorationsByPaintWorkId(paintWorkId);
        return new ResponseEntity<>(decorationsList, HttpStatus.OK);
    }

    @GetMapping("/getOptimizedDecorationImage/{id}/image")
    public ResponseEntity<byte[]> getOptimizedDecorationImage(@PathVariable int id) {
        PaintWorkDecorationImageDTO decorationImageDTO = this.adminService.getOptimizedDecorationImageById(id);
        if (decorationImageDTO != null && decorationImageDTO.getImageData() != null) {

            MediaType mediaType = MediaType.IMAGE_JPEG;

            HttpHeaders headers = new HttpHeaders(); // Set the appropriate content type in the headers
            headers.setContentType(mediaType);
            return new ResponseEntity<>(decorationImageDTO.getImageData(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteDecoration/{id}")
    public ResponseEntity<String> deleteDecoration(@PathVariable int id) {
        Optional<PaintWorkDecoration> decoration = this.adminService.findDecorationById(id);
        if(decoration == null || !decoration.isPresent()) {
            return new ResponseEntity<>("Failed", HttpStatus.NOT_FOUND);
        } else {
            this.adminService.deleteAPaintWorkById(id);
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
    }

}
