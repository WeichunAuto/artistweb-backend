package com.bobby.artistweb.service;

import com.bobby.artistweb.model.PaintWork;
import com.bobby.artistweb.repo.PaintWorkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
public class AdminService {

    @Autowired
    private PaintWorkRepo paintWorkRepo;

    public PaintWork addPaintWork(PaintWork paintWork, MultipartFile imageFile) throws IOException {
        paintWork.setDate(new Date());
        paintWork.setImageName(imageFile.getOriginalFilename());
        paintWork.setImageType(imageFile.getContentType());
        paintWork.setImageData(imageFile.getBytes());

        return this.paintWorkRepo.save(paintWork);
    }
}
