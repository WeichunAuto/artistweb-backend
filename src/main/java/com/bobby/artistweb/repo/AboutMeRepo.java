package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.AboutMe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AboutMeRepo extends JpaRepository<AboutMe, Integer> {
}
