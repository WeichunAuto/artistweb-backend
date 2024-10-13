package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.AboutMeDTO;
import com.bobby.artistweb.model.AboutMe;

import com.bobby.artistweb.model.AboutMeImageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Transactional(readOnly = true)
    @Query("Select new com.bobby.artistweb.model.AboutMeImageDTO(am.optimizedImageName, am.optimizedImageType, am.optimizedImageData) FROM AboutMe am WHERE am.id = :id")
    AboutMeImageDTO findAboutMeProfilePhotoById(@Param("id") int id);
}
