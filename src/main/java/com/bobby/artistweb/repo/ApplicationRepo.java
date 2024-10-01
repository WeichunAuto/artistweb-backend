package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepo extends JpaRepository<Applications, Integer> {

    Applications findByAppName(String appName);
}
