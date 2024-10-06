package com.bobby.artistweb.service;

import com.bobby.artistweb.model.PaintWork;
import com.bobby.artistweb.repo.PaintWorkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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

    public List<PaintWork> fetchAllPaintWorks() {
        Sort sort = Sort.by("status").ascending().and(Sort.by("date").descending());
        List<PaintWork> paintWorkList = this.paintWorkRepo.findAll(sort);

        for (PaintWork paintWork : paintWorkList) {
            paintWork.setImageData(new byte[0]);
            paintWork.setImageName("");
            paintWork.setImageType("");
        }

        return paintWorkList;
    }

    public PaintWork getPaintWorkById(int id) {
        return this.paintWorkRepo.findById(id).orElse(new PaintWork(-1));
    }

    public void deletePaintWorkById(int id) {
        this.paintWorkRepo.deleteById(id);
    }
}
