package com.local.db.service;

import com.local.db.model.*;
import com.local.db.repository.RowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RowService {
    @Autowired
    private RowRepository rowRepository;

    public Row findById(Table table, Long rowId){
        Row row = rowRepository.getRowByTableAndId(table, rowId);

        if (row == null) {
            throw new RuntimeException("Row Not Found!");
        }
        return row;
    }

    public void addRow(Row row) {
        rowRepository.save(row);
    }

    public void updateRow(Long rowId, Row row) {
        Row rowDb = findById(row.getTable(), rowId);
        rowDb.setValues(row.getValues());

        rowRepository.save(rowDb);
    }

    public void removeRow(Row row) {
        rowRepository.delete(row);
    }

}
