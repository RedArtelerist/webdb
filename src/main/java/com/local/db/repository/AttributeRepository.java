package com.local.db.repository;

import com.local.db.model.Attribute;
import com.local.db.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    List<Attribute> getAttributeByTable(Table table);
}
