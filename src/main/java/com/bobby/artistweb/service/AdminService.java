package com.bobby.artistweb.service;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.PaintWork;
import com.bobby.artistweb.model.PaintWorkDecoration;
import com.bobby.artistweb.model.PaintWorkDecorationImageDTO;
import com.bobby.artistweb.repo.PaintWorkDecorationRepo;
import com.bobby.artistweb.repo.PaintWorkRepo;
import com.bobby.artistweb.utils.ImageCompressor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private PaintWorkRepo paintWorkRepo;

    @Autowired
    private PaintWorkDecorationRepo decorationRepo;

    @Transactional
    public PaintWork addPaintWork(PaintWork paintWork, MultipartFile imageFile) throws IOException, ImageTypeDoesNotSupportException {
        String contentType = imageFile.getContentType();
        if (!contentType.toLowerCase().equals("image/jpeg")) {
            throw new ImageTypeDoesNotSupportException("Please only upload jpeg image files!");
        }

        paintWork.setDate(new Date());
        PaintWork savedPaintWork = paintWorkRepo.save(paintWork);

        PaintWorkDecoration decoration = new PaintWorkDecoration();
        decoration.setImageName(imageFile.getOriginalFilename());
        decoration.setImageType(imageFile.getContentType());
        decoration.setImageData(imageFile.getBytes());

        float compressionQuality = 0.25f;
        byte[] optimizedImageBytes = ImageCompressor.compressAndConvertToJpeg(imageFile, compressionQuality);
        decoration.setOptimizedImageData(optimizedImageBytes);
        decoration.setCover(true);
        decoration.setPaintWork(savedPaintWork);

        this.decorationRepo.save(decoration);

        return savedPaintWork;
    }

    public List<PaintWork> fetchAllPaintWorks() {
        Sort sort = Sort.by("status").ascending().and(Sort.by("date").descending());
        List<PaintWork> paintWorkList = this.paintWorkRepo.findAll(sort);

        return paintWorkList;
    }
    @Transactional
    public PaintWorkDecorationImageDTO getPaintWorkCoverById(int id) {
        return this.decorationRepo.findCoverById(id);
    }

    public Optional<PaintWork> findPaintWorkById(int id) {
        Optional<PaintWork> paintWork = this.paintWorkRepo.findById(id);
        return paintWork;
    }

    @Transactional
    public void deletePaintWorkById(int id) {
        this.decorationRepo.deleteDecorationsByPaintWorkId(id);
        this.paintWorkRepo.deleteById(id);
    }

    public void saveDecoration(int paintWorkId, MultipartFile imageFile) throws ImageTypeDoesNotSupportException, IOException {
        String contentType = imageFile.getContentType();
        if (!contentType.toLowerCase().equals("image/jpeg")) {
            throw new ImageTypeDoesNotSupportException("Please only upload jpeg image files!");
        }

        PaintWorkDecoration decoration = new PaintWorkDecoration();
        decoration.setImageName(imageFile.getOriginalFilename());
        decoration.setImageType(imageFile.getContentType());
        decoration.setImageData(imageFile.getBytes());

        float compressionQuality = 0.25f;
        byte[] optimizedImageBytes = ImageCompressor.compressAndConvertToJpeg(imageFile, compressionQuality);
        decoration.setOptimizedImageData(optimizedImageBytes);
        decoration.setCover(false);

        PaintWork paintWork = new PaintWork(paintWorkId);
        decoration.setPaintWork(paintWork);

        this.decorationRepo.save(decoration);
    }
}
