package com.bobby.artistweb.repo;

import com.bobby.artistweb.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepo extends JpaRepository<MenuItem, Long> {

    // find all first level menus.
    List<MenuItem> findByParentNull();

//    List<MenuItem> findByParent(MenuItem parent);
}
