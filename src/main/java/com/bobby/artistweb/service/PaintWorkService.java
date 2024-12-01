package com.bobby.artistweb.service;

import com.bobby.artistweb.exception.ImageTypeDoesNotSupportException;
import com.bobby.artistweb.model.*;
import com.bobby.artistweb.repo.PaintWorkDecorationRepo;
import com.bobby.artistweb.repo.PaintWorkRepo;
import com.bobby.artistweb.utils.ImageCompressor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PaintWorkService {
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

    public List<PaintWorkDTO> fetchPaintWorksPagination(int pageSize, int pageNum) {
//        Sort sort = Sort.by("status").ascending().and(Sort.by("date").descending());
        List<Object[]> results = this.paintWorkRepo.findAllPaintWorksAndDecorationCount(pageSize, pageNum);
        List<PaintWorkDTO> paintWorkList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        for (Object[] row : results) {
            PaintWorkDTO dto = new PaintWorkDTO();
            dto.setId((long) row[0]);
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
            dto.setCoverURL("");

            paintWorkList.add(dto);
        }

        return paintWorkList;
    }
    @Transactional
    public PaintWorkDecorationImageDTO getPaintWorkCoverById(long id) {
        return this.decorationRepo.findCoverById(id);
    }

    public Optional<PaintWork> findPaintWorkById(long id) {
        Optional<PaintWork> paintWork = this.paintWorkRepo.findById(id);
        return paintWork;
    }

    @Transactional
    public void deletePaintWorkById(long id) {
        this.decorationRepo.deleteDecorationsByPaintWorkId(id);
        this.paintWorkRepo.deleteById(id);
    }

    public PaintWork updatePaintWork(PaintWorkEditDTO paintWork) {
        Optional<PaintWork> savedPaintWork = this.paintWorkRepo.findById(paintWork.getId());
        if (savedPaintWork.isPresent()) {
            PaintWork existingPaintWork = savedPaintWork.get();

            existingPaintWork.setTitle(paintWork.getTitle());
            existingPaintWork.setDescription(paintWork.getDescription());
            existingPaintWork.setPrice(paintWork.getPrice());
            existingPaintWork.setStatus(paintWork.getStatus());
            existingPaintWork.setDimensionWidth(paintWork.getDimensionWidth());
            existingPaintWork.setDimensionHeight(paintWork.getDimensionHeight());
            this.paintWorkRepo.save(existingPaintWork);
            return existingPaintWork;
        } else {
            return null;
        }
    }

    public int fetchMaxPageNum(int pageSize) {
        int totalRecords = Integer.parseInt(String.valueOf(this.paintWorkRepo.count()));
        if(totalRecords % pageSize == 0) {
            return totalRecords / pageSize;
        } else {
            return (totalRecords / pageSize) +1;
        }
    }
}
