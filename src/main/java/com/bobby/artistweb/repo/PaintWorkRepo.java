package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.PaintWork;
import com.bobby.artistweb.model.PaintWorkDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PaintWorkRepo extends JpaRepository<PaintWork, Integer> {

    @Query(value = "SELECT a.id, a.title, a.description, a.price, a.status, a.date, a.year, a.dimension_width, a.dimension_height, b.decorationCount FROM paint_work a left join (select paint_work_id, count(*) as decorationCount from paint_work_decoration group by paint_work_id) as b on a.id=b.paint_work_id order by a.date desc", nativeQuery = true)
    List<Object[]> findAllPaintWorksAndDecorationCount();
}
