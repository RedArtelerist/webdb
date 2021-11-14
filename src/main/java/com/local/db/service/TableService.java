package com.local.db.service;

import com.local.db.model.*;
import com.local.db.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private RowService rowService;

    public Table findByName(Base base, String name){
        Table table = tableRepository.findByBaseAndNameIgnoreCase(base, name);

        if (table == null) {
            throw new RuntimeException("Table Not Found!");
        }
        return table;
    }

    public void addTable(Table table, List<String> columns, List<Type> types) throws Exception {
        Table dbTable = tableRepository.findByBaseAndNameIgnoreCase(table.getBase(), table.getName());
        if (dbTable != null)
            throw new Exception("Table with the same name exists!");

        if(columns.size() != types.size())
            throw new Exception("Error in attributes!");

        List<Attribute> attributes = convertToAttributes(table, columns, types);

        if(!checkAttributes(attributes))
            throw new Exception("Error in attributes!");

        table.setAttributes(attributes);

        tableRepository.save(table);
    }

    public void createTable(Base base, Table table) throws Exception {
        Table dbTable = tableRepository.findByBaseAndNameIgnoreCase(base, table.getName());

        if (dbTable != null)
            throw new Exception("Table with the same name exists!");

        table.setBase(base);
        table.getAttributes().forEach(attribute -> attribute.setTable(table));

        for(Attribute attribute : table.getAttributes()){
            if(attribute.getName().trim().equals("") || attribute.getType() == null)
                throw new Exception("Error in attributes");
        }

        tableRepository.save(table);
    }

    public void removeTable(Table table) {
        tableRepository.delete(table);
    }

    public void deleteDuplicateRows(Table table) {
        List<Row> uniqueRows = new ArrayList<>();

        for(Row row : table.getRows()){
            if(!uniqueRows.contains(row))
                uniqueRows.add(row);
            else {
                rowService.removeRow(row);
            }
        }
    }

    public List<Long> deleteDuplicates(Table table) {
        List<Row> uniqueRows = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for(Row row : table.getRows()){
            if(!uniqueRows.contains(row))
                uniqueRows.add(row);
            else {
                ids.add(row.getId());
                rowService.removeRow(row);
            }
        }
        return ids;
    }

    private List<Attribute> convertToAttributes(Table table, List<String> columns, List<Type> types) {
        List<Attribute> attributes = new ArrayList<>();
        for(int i = 0; i < columns.size(); i++){
            Attribute attribute = new Attribute();
            String name = columns.get(i);
            Type type = types.get(i);

            attribute.setName(name);
            attribute.setType(type);
            attribute.setTable(table);
            attributes.add(attribute);
        }
        return attributes;
    }

    private boolean checkAttributes(List<Attribute> attributes){
        for(Attribute attribute : attributes){
            if(attribute.getName().trim().equals("") || attribute.getName().length() > 20 || attribute.getType() == null)
                return false;
        }
        return true;
    }

    private List<Attribute> convertStringToAttributes(Table table, List<String> columns, List<String> types) throws Exception {
        List<Attribute> attributes = new ArrayList<>();
        for(int i = 0; i < columns.size(); i++){
            Attribute attribute = new Attribute();
            String name = columns.get(i);
            Type type = Type.valueOf(types.get(i));

            if(name.trim().equals(""))
                throw new Exception("Error in attributes");

            attribute.setName(name);
            attribute.setType(type);
            attribute.setTable(table);
            attributes.add(attribute);
        }
        return attributes;
    }

}
