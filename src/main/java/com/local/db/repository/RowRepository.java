package com.local.db.repository;

import com.local.db.model.Row;
import com.local.db.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RowRepository extends JpaRepository<Row, Long> {
    List<Row> getAllByTableOrderById(Table table);

    Row getRowByTableAndId(Table table, Long id);
}
