package com.local.db.controller;

import com.local.db.model.*;
import com.local.db.repository.RowRepository;
import com.local.db.service.BaseService;
import com.local.db.service.RowService;
import com.local.db.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/base/{dbName}/table/{tName}/")
public class RowController {
    @Autowired
    private BaseService baseService;

    @Autowired
    private TableService tableService;

    @Autowired
    private RowService rowService;

    @Autowired
    private RowRepository rowRepository;

    @GetMapping("/rows/create")
    public String createForm(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName, Model model){
        Table table = tableService.findByName(baseService.findByName(dbName), tName);

        model.addAttribute("table", table);
        return "rowCreateForm";
    }

    @PostMapping("/rows/create")
    public String createRow(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName,
                            @Valid Row row, Model model) {
        Table table = tableService.findByName(baseService.findByName(dbName), tName);
        Map<String, String> errorsMap = getErrors(table, row);

        if(errorsMap.size() > 0){
            model.addAttribute("table", table);
            model.addAttribute("row", row);
            model.addAttribute("errors", errorsMap);

            return "rowCreateForm";
        } else{
            rowService.addRow(row);
            return "redirect:/base/" + dbName + "/table/" + tName + "/rows";
        }
    }

    @PostMapping("/rows/insert")
    public ResponseEntity<?> insertRow(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName,
                                       @RequestBody @Valid Row row) {
        Table table = tableService.findByName(baseService.findByName(dbName), tName);
        Map<String, String> errorsMap = getErrors(table, row);

        if(errorsMap.size() > 0){
            return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
        } else{
            rowService.insertRow(table, row);
            return new ResponseEntity<>(row, HttpStatus.OK);
        }
    }

    @GetMapping("/rows/{id}/edit")
    public String editForm(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName,
                           @PathVariable("id") Long rowId, Model model) {
        Table table =  tableService.findByName(baseService.findByName(dbName), tName);

        model.addAttribute("table", table);
        model.addAttribute("row", rowService.findById(table, rowId));
        return "rowCreateForm";
    }

    @PostMapping("/rows/{id}/edit")
    public String editRow(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName,
                          @PathVariable("id") Long rowId, @Valid Row row, Model model) {
        Table table =  tableService.findByName(baseService.findByName(dbName), tName);
        Map<String, String> errorsMap = getErrors(table, row);

        if(errorsMap.size() > 0){
            model.addAttribute("base", baseService.findByName(dbName));
            model.addAttribute("table", table);
            model.addAttribute("row", row);
            model.addAttribute("errors", errorsMap);

            return "rowCreateForm";
        } else{
            rowService.updateRow(rowId, row);
            return "redirect:/base/" + dbName + "/table/" + tName + "/rows";
        }
    }

    @PostMapping("/rows/{id}/update")
    public ResponseEntity<?> updateRow(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName,
                          @PathVariable("id") Long rowId, @RequestBody @Valid Row row) {
        Table table =  tableService.findByName(baseService.findByName(dbName), tName);
        Map<String, String> errorsMap = getErrors(table, row);

        if(errorsMap.size() > 0){
            return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
        } else{
            rowService.editRow(rowId, row);
            return new ResponseEntity<>("Row was successful updated", HttpStatus.OK);
        }
    }

    @PostMapping("/rows/{id}/delete")
    public String deleteRow(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName, @PathVariable("id") Long rowId){
        Table table =  tableService.findByName(baseService.findByName(dbName), tName);
        Row row = rowService.findById(table, rowId);
        rowService.removeRow(row);

        return "redirect:/base/" + dbName + "/table/" + tName + "/rows";
    }

    @PostMapping("/rows/{id}/remove")
    public ResponseEntity<?> removeRow(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName, @PathVariable("id") Long rowId){
        try {
            Table table =  tableService.findByName(baseService.findByName(dbName), tName);
            Row row = rowService.findById(table, rowId);
            rowService.removeRow(row);
            return new ResponseEntity<>("Row was successfully deleted", HttpStatus.OK);
        }
        catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    private Map<String, String> getErrors(Table table, Row row){
        Map<String, String> errors = new LinkedHashMap<>();

        for(int i = 0; i < row.getValues().size(); i++){
            String value = row.getValues().get(i);
            String column = table.getAttributes().get(i).getName();
            Type type = table.getAttributes().get(i).getType();
            try {
                if(value.trim().equals("")) {
                    errors.put(column, column + " must not be empty");
                    continue;
                }
                Object obj = TypeManager.parseObjectByType(value, type);
            }
            catch (Exception ex){
                errors.put(column, "expected " + type + " value");
            }
        }
        return errors;
    }
}
