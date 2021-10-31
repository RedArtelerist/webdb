package com.local.db.repository;

import com.local.db.model.Base;
import com.local.db.model.Table;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findByBase(Base base, Sort sort);

    Table findByBaseAndNameIgnoreCase(Base base, String name);
}
