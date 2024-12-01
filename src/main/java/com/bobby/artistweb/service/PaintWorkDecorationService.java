package com.bobby.artistweb.service;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.PaintWork;
import com.bobby.artistweb.model.PaintWorkDecoration;
import com.bobby.artistweb.model.PaintWorkDecorationDTO;
import com.bobby.artistweb.model.PaintWorkDecorationImageDTO;
import com.bobby.artistweb.repo.PaintWorkDecorationRepo;
import com.bobby.artistweb.utils.ImageCompressor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PaintWorkDecorationService {

    @Autowired
    private PaintWorkDecorationRepo paintWorkDecorationRepo;

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

        this.paintWorkDecorationRepo.save(decoration);
    }

    public List<PaintWorkDecorationDTO> fetchDecorationsByPaintWorkId(int paintWorkId) {
        return this.paintWorkDecorationRepo.findDecorationsByPaintWorkId(paintWorkId);
    }

    public PaintWorkDecorationImageDTO getOptimizedDecorationImageById(long id) {
        return this.paintWorkDecorationRepo.findOptimizedDecorationImageById(id);
    }

    public PaintWorkDecorationImageDTO getOrigionalDecorationImageById(long id) {
        return this.paintWorkDecorationRepo.findOrigionalDecorationImageById(id);
    }

    public Optional<PaintWorkDecoration> findDecorationById(long id) {
        Optional<PaintWorkDecoration> decoration = this.paintWorkDecorationRepo.findById(id);
        return decoration;
    }

    public void deleteDecorationById(long id) {
        this.paintWorkDecorationRepo.deleteById(id);
    }
}
