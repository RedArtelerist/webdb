package com.local.db.controller;

import com.local.db.model.Base;
import com.local.db.model.Type;
import com.local.db.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BaseController {
    @Autowired
    private BaseService baseService;

    @GetMapping
    public String baseList(Model model){
        model.addAttribute("bases", baseService.findAll());
        return "dbList";
    }

    @PostMapping
    public String createBase(@Valid Base base, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("base", base);
            model.addAttribute("bases", baseService.findAll());

            return "dbList";
        } else {
            if (!baseService.addBase(base)) {
                model.addAttribute("nameError", "Base with the same name exists!");
                model.addAttribute("base", base);
                model.addAttribute("bases", baseService.findAll());

                return "dbList";
            }

            return "redirect:/";
        }
    }

    @PostMapping("/database/create")
    public ResponseEntity<?> addBase(@RequestBody @Valid Base base, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
        } else {
            try {
                Base db = baseService.createBase(base);
                return new ResponseEntity<>(db, HttpStatus.OK);
            }
            catch (Exception ex){
                Map<String, String> map = new HashMap<>();
                map.put("nameError", ex.getMessage());
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
        }
    }


    @PostMapping("/base/{name}/delete")
    public String deleteBase(@PathVariable("name") String name, Model model){
        Base base = baseService.findByName(name);
        baseService.removeBase(base);

        return "redirect:/";
    }

    @PostMapping("/database/{name}/delete")
    public ResponseEntity<?> deleteDatabase(@PathVariable("name") String name){
        try {
            Base base = baseService.findByName(name);
            baseService.removeBase(base);
            return new ResponseEntity<>("Database was successfully deleted", HttpStatus.OK);
        }
        catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/base/{name}/tables")
    public String baseDetails(@PathVariable("name") String name, Model model){
        Base base = baseService.findByName(name);
        model.addAttribute("base", base);
        model.addAttribute("types", Type.values());
        return "dbDetails";
    }
}
