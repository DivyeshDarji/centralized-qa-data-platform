package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.EndUser;
import com.example.demo.service.EndUserExcelService;
import com.example.demo.service.EndUserService;

@RestController
@RequestMapping("/enduser")
public class EndUserController {

    @Autowired
    private EndUserService service;

    @Autowired
    private EndUserExcelService excelService;

    @GetMapping
    public List<EndUser> getAll() {
        return service.getAll();
    }

    // @PostMapping
    /*
     * public EndUser create(@RequestBody EndUser data) {
     * return service.save(data);
     * }
     */

    @GetMapping("/search")
    public List<EndUser> search(
            @RequestParam(required = false) String environment,
            @RequestParam(required = false) String country) {

        if (environment != null && country != null) {
            return service.filter(environment, country);
        }

        if (environment != null) {
            return service.findByEnvironment(environment);
        }

        if (country != null) {
            return service.findByCountry(country);
        }

        return service.getAll();
    }

    @GetMapping("/pending")
    public List<EndUser> pending() {
        return service.getPending();
    }

    @DeleteMapping("/{id}")
    public EndUser requestDelete(@PathVariable Long id) {
        return service.requestDelete(id);
    }

    @DeleteMapping("/admin/{id}")
    public String approveDelete(@PathVariable Long id) {
        return service.approveDelete(id);
    }

    // ✅ Post API to upload Excel file and save data to DB
    @PostMapping("/upload")
    public String uploadExcel(@RequestParam("file") MultipartFile file) {

        try {
            List<EndUser> list = excelService.parseExcel(file.getInputStream());

            list.forEach(service::save);

            return "EndUser Excel uploaded successfully";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EndUser data) {
        try {
            return ResponseEntity.ok(service.save(data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EndUser data) {

        try {
            EndUser existing = service.getById(id);

            if (existing == null) {
                return ResponseEntity.badRequest().body("Data not found");
            }

            // ✅ UPDATE ONLY PROVIDED FIELDS
            if (data.getEndUserName() != null)
                existing.setEndUserName(data.getEndUserName());

            if (data.getCity() != null)
                existing.setCity(data.getCity());

            if (data.getState() != null)
                existing.setState(data.getState());

            if (data.getPostalCode() != null)
                existing.setPostalCode(data.getPostalCode());

            if (data.getCountry() != null)
                existing.setCountry(data.getCountry());

            if (data.getEmail() != null)
                existing.setEmail(data.getEmail());

            if (data.getPhoneNumber() != null)
                existing.setPhoneNumber(data.getPhoneNumber());

            if (data.getTag() != null)
                existing.setTag(data.getTag());

            // ✅ important fields
            existing.setEnvironment(data.getEnvironment());

            return ResponseEntity.ok(service.save(existing));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}