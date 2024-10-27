package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.PaintWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PaintWorkRepo extends JpaRepository<PaintWork, Integer> {

}
