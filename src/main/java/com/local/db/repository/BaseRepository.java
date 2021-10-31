package com.local.db.repository;

import com.local.db.model.Base;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseRepository extends JpaRepository<Base, Long> {
    List<Base> findAll(Sort sort);

    Base findByNameIgnoreCase(String name);
}
