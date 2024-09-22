package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.UniqueValues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniqueValuesRepo extends JpaRepository<UniqueValues, Integer> {
}
