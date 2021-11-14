package com.local.db.controller;


import com.local.db.model.Base;
import com.local.db.model.Table;
import com.local.db.model.Type;
import com.local.db.repository.TableRepository;
import com.local.db.service.BaseService;
import com.local.db.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/base/{dbName}")
public class TableController {
    @Autowired
    private BaseService baseService;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableRepository tableRepository;

    @GetMapping("/tables/create")
    public String createForm(@PathVariable("dbName") String name, Model model){
        model.addAttribute("base", baseService.findByName(name));
        model.addAttribute("types", Type.values());
        return "tableFormCreate";
    }

    @PostMapping("/tables/create")
    public String createTable(@PathVariable("dbName") String name, @Valid Table table, BindingResult bindingResult,
                              @RequestParam("attribute_name") List<String> columns,
                              @RequestParam("attribute_type") List<Type> types, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("base", baseService.findByName(name));
            model.addAttribute("table", table);
            model.addAttribute("types", Type.values());

            return "tableFormCreate";
        } else {
            try {
                tableService.addTable(table, columns, types);
                return "redirect:/base/" + name + "/tables";
            }
            catch (Exception ex){
                model.addAttribute("nameError", ex.getMessage());
                model.addAttribute("base", baseService.findByName(name));
                model.addAttribute("table", table);
                model.addAttribute("types", Type.values());

                return "tableFormCreate";
            }

        }
    }

    @PostMapping("/tables/add")
    public ResponseEntity<?> addTable(@PathVariable("dbName") String dbName, @RequestBody @Valid Table table, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
        } else {
            try {
                Base base = baseService.findByName(dbName);
                tableService.createTable(base, table);
                return new ResponseEntity<>(tableRepository.getById(table.getId()), HttpStatus.OK);
            }
            catch (Exception ex){
                Map<String, String> map = new HashMap<>();
                map.put("nameError", ex.getMessage());
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PostMapping("/tables/{tName}/delete")
    public String deleteTable(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName, Model model){
        Table table = tableService.findByName(baseService.findByName(dbName), tName);
        tableService.removeTable(table);

        return "redirect:/base/" + dbName + "/tables";
    }

    @PostMapping("/tables/{tName}/remove")
    public ResponseEntity<?> deleteTable(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName){
        try {
            Table table = tableService.findByName(baseService.findByName(dbName), tName);
            tableService.removeTable(table);
            return new ResponseEntity<>("Table was successfully deleted", HttpStatus.OK);

        }
        catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/table/{tName}/rows")
    public String tableDetails(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName, Model model){
        Table table =  tableService.findByName(baseService.findByName(dbName), tName);
        model.addAttribute("table", table);

        return "tableDetails";
    }

    @PostMapping("/table/{tName}/deleteDuplicates")
    public String deleteDuplicates(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName){
        Table table =  tableService.findByName(baseService.findByName(dbName), tName);
        tableService.deleteDuplicateRows(table);

        return "redirect:/base/" + dbName + "/table/" + tName + "/rows";
    }

    @PostMapping("/table/{tName}/deleteDuplicateRows")
    public ResponseEntity<?> deleteDuplicateRows(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName){
        try {
            Table table = tableService.findByName(baseService.findByName(dbName), tName);
            List<Long> ids = tableService.deleteDuplicates(table);
            return new ResponseEntity<>(ids, HttpStatus.OK);
        }
        catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
