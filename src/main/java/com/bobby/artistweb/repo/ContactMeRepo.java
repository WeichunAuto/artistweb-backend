package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.ContactMe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactMeRepo extends JpaRepository<ContactMe, Long> {
}
