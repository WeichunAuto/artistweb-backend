package com.bobby.artistweb.controller;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.PaintWorkDecoration;
import com.bobby.artistweb.model.PaintWorkDecorationDTO;
import com.bobby.artistweb.model.PaintWorkDecorationImageDTO;
import com.bobby.artistweb.service.PaintWorkDecorationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class PaintWorkDecorationController extends BaseController {

    @Autowired
    private PaintWorkDecorationService paintWorkDecorationService;


    @PostMapping("/addDecoration/{paintWorkId}")
    public ResponseEntity<String> addDecoration(@PathVariable int paintWorkId, @RequestPart(value="imageFile") MultipartFile imageFile) {
        try {
            this.paintWorkDecorationService.saveDecoration(paintWorkId, imageFile);
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
        List<PaintWorkDecorationDTO> decorationsList = this.paintWorkDecorationService.fetchDecorationsByPaintWorkId(paintWorkId);
        return new ResponseEntity<>(decorationsList, HttpStatus.OK);
    }

    @GetMapping("/getOptimizedDecorationImage/{id}/image")
    public ResponseEntity<byte[]> getOptimizedDecorationImage(@PathVariable int id) {
        PaintWorkDecorationImageDTO decorationImageDTO = this.paintWorkDecorationService.getOptimizedDecorationImageById(id);
        if (decorationImageDTO != null && decorationImageDTO.getImageData() != null) {

            MediaType mediaType = MediaType.IMAGE_JPEG;

            HttpHeaders headers = new HttpHeaders(); // Set the appropriate content type in the headers
            headers.setContentType(mediaType);
            return new ResponseEntity<>(decorationImageDTO.getImageData(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getOrigionalDecorationImage/{id}/image")
    public ResponseEntity<byte[]> getOrigionalDecorationImage(@PathVariable int id) {
        PaintWorkDecorationImageDTO decorationImageDTO = this.paintWorkDecorationService.getOrigionalDecorationImageById(id);
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
        Optional<PaintWorkDecoration> decoration = this.paintWorkDecorationService.findDecorationById(id);
        if(decoration == null || !decoration.isPresent()) {
            return new ResponseEntity<>("Failed", HttpStatus.NOT_FOUND);
        } else {
            this.paintWorkDecorationService.deleteDecorationById(id);
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
    }
}
