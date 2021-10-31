package com.local.db.controller;


import com.local.db.model.Table;
import com.local.db.model.Type;
import com.local.db.service.BaseService;
import com.local.db.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/base/{dbName}")
public class TableController {
    @Autowired
    private BaseService baseService;

    @Autowired
    private TableService tableService;

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

    @PostMapping("/tables/{tName}/delete")
    public String deleteTable(@PathVariable("dbName") String dbName, @PathVariable("tName") String tName, Model model){
        Table table = tableService.findByName(baseService.findByName(dbName), tName);
        tableService.removeTable(table);

        return "redirect:/base/" + dbName + "/tables";
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
}
