package com.bobby.artistweb.controller;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.PaintWork;
import com.bobby.artistweb.model.PaintWorkDTO;
import com.bobby.artistweb.model.PaintWorkDecorationImageDTO;
import com.bobby.artistweb.model.PaintWorkEditDTO;
import com.bobby.artistweb.service.PaintWorkService;
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
public class PaintWorkController extends BaseController {


    @Autowired
    private PaintWorkService paintWorkService;

    @PostMapping(value="/addPaintWork")
    @ResponseBody
    public ResponseEntity<?> addPaintWork(@RequestPart(value="paintWork") PaintWork paintWork,
                                          @RequestPart MultipartFile imageFile, HttpServletRequest request) {

        PaintWork savedPaintWork = null;
        try {
            savedPaintWork = this.paintWorkService.addPaintWork(paintWork, imageFile);
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

    @PutMapping("/editPaintWork")
    public ResponseEntity<String> editPaintWork(@RequestPart(value="paintWork") PaintWorkEditDTO paintWork) {
        if(this.paintWorkService.updatePaintWork(paintWork) == null) {
            return new ResponseEntity<>("failed", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
    }

    @GetMapping("/fetchPaintWorks/{pageSize}/{pageNum}")
    @ResponseBody
    public ResponseEntity<List<PaintWorkDTO>> fetchPaintWorks(@PathVariable int pageSize, @PathVariable int pageNum) {
        List<PaintWorkDTO> paintWorksList = this.paintWorkService.fetchPaintWorksPagination(pageSize, pageNum);

        return new ResponseEntity<>(paintWorksList, HttpStatus.OK);
    }

    @GetMapping("/fetchMaxPageNum/{pageSize}")
    public ResponseEntity<Integer> fetchMaxPageNum(@PathVariable int pageSize) {
        int maxPageNum = this.paintWorkService.fetchMaxPageNum(pageSize);
        return new ResponseEntity<>(maxPageNum, HttpStatus.OK);
    }

    @GetMapping("/getPaintWorkCover/{id}/image")
    public ResponseEntity<byte[]> getAPaintWork(@PathVariable int id) {
        PaintWorkDecorationImageDTO decorationImageDTO = this.paintWorkService.getPaintWorkCoverById(id);
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
        Optional<PaintWork> paintWork = this.paintWorkService.findPaintWorkById(id);
        if(paintWork == null || !paintWork.isPresent()) {
            return new ResponseEntity<>("Failed", HttpStatus.NOT_FOUND);
        } else {
            this.paintWorkService.deletePaintWorkById(id);
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
    }
}
