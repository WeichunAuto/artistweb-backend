package com.bobby.artistweb.repo;


import com.bobby.artistweb.model.PaintWorkDecoration;
import com.bobby.artistweb.model.PaintWorkDecorationImageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaintWorkDecorationRepo extends JpaRepository<PaintWorkDecoration, Integer> {

    @Query("Select new com.bobby.artistweb.model.PaintWorkDecorationImageDTO(pd.id, pd.imageName, pd.imageType, pd.optimizedImageData) FROM PaintWorkDecoration pd WHERE pd.paintWork.id = :id AND pd.cover=true ")
    PaintWorkDecorationImageDTO findCoverById(@Param("id") int id);

    @Modifying
    @Query("Delete FROM PaintWorkDecoration pd WHERE pd.paintWork.id = :id")
    void deleteDecorationsByPaintWorkId(@Param("id") int id);
}
