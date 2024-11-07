package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.Logo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoRepo extends JpaRepository<Logo, Integer> {
}
