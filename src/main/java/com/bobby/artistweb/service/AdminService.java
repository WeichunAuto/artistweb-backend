package com.bobby.artistweb.service;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.*;
import com.bobby.artistweb.repo.ForegroundImageRepo;
import com.bobby.artistweb.repo.LogoRepo;
import com.bobby.artistweb.repo.PaintWorkDecorationRepo;
import com.bobby.artistweb.repo.PaintWorkRepo;
import com.bobby.artistweb.utils.ImageCompressor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private PaintWorkRepo paintWorkRepo;

    @Autowired
    private PaintWorkDecorationRepo decorationRepo;

    @Autowired
    private LogoRepo logoRepo;

    @Autowired
    private ForegroundImageRepo foregroundImageRepo;

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

    public List<PaintWorkDTO> fetchAllPaintWorks() {
//        Sort sort = Sort.by("status").ascending().and(Sort.by("date").descending());
        List<Object[]> results = this.paintWorkRepo.findAllPaintWorksAndDecorationCount();
        List<PaintWorkDTO> paintWorkList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        for (Object[] row : results) {
            PaintWorkDTO dto = new PaintWorkDTO();
            dto.setId((int) row[0]);
            dto.setTitle((String) row[1]);
            dto.setDescription((String) row[2]);
            dto.setPrice((int) row[3]);
            dto.setStatus((String) row[4]);

            Date date = (Date) row[5];
            String formattedDate = formatter.format(date);
            dto.setDate(formattedDate);

            dto.setYear((String) row[6]);
            dto.setDimensionWidth((int) row[7]);
            dto.setDimensionHeight((int) row[8]);
            dto.setDecorationCount((long) row[9]);

            paintWorkList.add(dto);
        }

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

    public List<PaintWorkDecorationDTO> fetchDecorationsByPaintWorkId(int paintWorkId) {
        return this.decorationRepo.findDecorationsByPaintWorkId(paintWorkId);
    }

    public PaintWorkDecorationImageDTO getOptimizedDecorationImageById(int id) {
        return this.decorationRepo.findOptimizedDecorationImageById(id);
    }

    public PaintWorkDecorationImageDTO getOrigionalDecorationImageById(int id) {
        return this.decorationRepo.findOrigionalDecorationImageById(id);
    }

    public Optional<PaintWorkDecoration> findDecorationById(int id) {
        Optional<PaintWorkDecoration> decoration = this.decorationRepo.findById(id);
        return decoration;
    }

    public void deleteAPaintWorkById(int id) {
        this.decorationRepo.deleteById(id);
    }


    public void saveLogoImage(MultipartFile logoImage) throws IOException {
        Logo savedLogo = null;
        if(this.logoRepo.findAll().size() == 0) {
            savedLogo = new Logo();
        } else {
            savedLogo = this.logoRepo.findAll().get(0);
        }
        savedLogo.setImageName(logoImage.getOriginalFilename());
        savedLogo.setImageType(logoImage.getContentType());
        savedLogo.setImageData(logoImage.getBytes());
        this.logoRepo.save(savedLogo);
    }

    public Logo fetchSavedLogo() {
        Logo savedLogo = null;
        if(this.logoRepo.findAll().size() != 0) {
            savedLogo = this.logoRepo.findAll().get(0);
        }
        return savedLogo;
    }

    public void saveForegroundImage(MultipartFile foregroundImage) throws IOException {
        ForegroundImage savedForegroundImage = null;
        if(this.foregroundImageRepo.findAll().size() == 0) {
            savedForegroundImage = new ForegroundImage();
        } else {
            savedForegroundImage = this.foregroundImageRepo.findAll().get(0);
        }
        savedForegroundImage.setImageName(foregroundImage.getOriginalFilename());
        savedForegroundImage.setImageType(foregroundImage.getContentType());
        savedForegroundImage.setImageData(foregroundImage.getBytes());
        this.foregroundImageRepo.save(savedForegroundImage);
    }

    public ForegroundImage fetchSavedForegroundImage() {
        ForegroundImage savedForegroundImage = null;
        if(this.foregroundImageRepo.findAll().size() != 0) {
            savedForegroundImage = this.foregroundImageRepo.findAll().get(0);
        }
        return savedForegroundImage;
    }
}
