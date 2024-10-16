package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.Topic;
import com.bobby.artistweb.model.TopicDTO;
import com.bobby.artistweb.model.TopicImageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TopicRepo  extends JpaRepository<Topic, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT new com.bobby.artistweb.model.TopicDTO(t.id, t.title, t.description) FROM Topic t")
    List<TopicDTO> findAboutMeOnlyNameAndDescription();

    @Transactional(readOnly = true)
    @Query("Select new com.bobby.artistweb.model.TopicImageDTO(t.optimizedImageName, t.optimizedImageType, t.optimizedImageData) FROM Topic t WHERE t.id = :id")
    TopicImageDTO findTopicPhotoById(@Param("id") int id);
}
