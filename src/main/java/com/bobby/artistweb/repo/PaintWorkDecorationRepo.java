package com.bobby.artistweb.repo;


import com.bobby.artistweb.model.PaintWorkDecoration;
import com.bobby.artistweb.model.PaintWorkDecorationDTO;
import com.bobby.artistweb.model.PaintWorkDecorationImageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PaintWorkDecorationRepo extends JpaRepository<PaintWorkDecoration, Long> {

    @Query("Select new com.bobby.artistweb.model.PaintWorkDecorationImageDTO(pd.id, pd.optimizedImageData) FROM PaintWorkDecoration pd WHERE pd.paintWork.id = :id AND pd.cover=true")
    PaintWorkDecorationImageDTO findCoverById(@Param("id") long id);

    @Modifying
    @Query("Delete FROM PaintWorkDecoration pd WHERE pd.paintWork.id = :id")
    void deleteDecorationsByPaintWorkId(@Param("id") long id);

    @Query("Select new com.bobby.artistweb.model.PaintWorkDecorationDTO(pd.id, pd.imageName, pd.imageType, pd.cover) FROM PaintWorkDecoration pd WHERE pd.paintWork.id = :id")
    List<PaintWorkDecorationDTO> findDecorationsByPaintWorkId(@Param("id") long id);

    @Transactional(readOnly = true)
    @Query("Select new com.bobby.artistweb.model.PaintWorkDecorationImageDTO(pd.id, pd.optimizedImageData) FROM PaintWorkDecoration pd WHERE pd.id = :id")
    PaintWorkDecorationImageDTO findOptimizedDecorationImageById(@Param("id") long id);

    @Transactional(readOnly = true)
    @Query("Select new com.bobby.artistweb.model.PaintWorkDecorationImageDTO(pd.id, pd.imageData) FROM PaintWorkDecoration pd WHERE pd.id = :id")
    PaintWorkDecorationImageDTO findOrigionalDecorationImageById(long id);
}
