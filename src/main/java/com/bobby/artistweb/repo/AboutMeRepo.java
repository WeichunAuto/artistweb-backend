package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.AboutMeDTO;
import com.bobby.artistweb.model.AboutMe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AboutMeRepo extends JpaRepository<AboutMe, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT am FROM AboutMe am")
    AboutMe findFirst();

    @Transactional(readOnly = true)
    @Query("SELECT new com.bobby.artistweb.model.AboutMeDTO(am.id, am.name, am.description) FROM AboutMe am")
    AboutMeDTO findAboutMeOnlyNameAndDescription();
}
