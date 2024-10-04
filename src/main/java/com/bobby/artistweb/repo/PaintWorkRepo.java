package com.bobby.artistweb.repo;


import com.bobby.artistweb.model.PaintWork;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Repository
public interface PaintWorkRepo extends JpaRepository<PaintWork, Integer> {

}
