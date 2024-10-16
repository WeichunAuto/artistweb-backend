package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.AboutMe;
import com.bobby.artistweb.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepo  extends JpaRepository<Topic, Integer> {
}
