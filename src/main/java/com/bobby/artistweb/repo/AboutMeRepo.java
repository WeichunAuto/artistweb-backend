package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.AboutMe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutMeRepo extends JpaRepository<AboutMe, Integer> {
}
