package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.UniqueValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniqueValuesRepo extends JpaRepository<UniqueValues, Long> {
}
