package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.demo.model.TestData;
import com.example.demo.service.ExcelService;
import com.example.demo.service.TestDataService;

@RestController
@RequestMapping("/testdata")
public class TestDataController {

    @Autowired
    private TestDataService service;

    @Autowired
    private ExcelService excelService;

    // ✅ GET all data
    @GetMapping
    public List<TestData> getAll() {
        return service.getAll();
    }

    // ✅ POST: create new data
    @PostMapping
    public TestData create(@RequestBody TestData data) {
        return service.save(data);
    }

    // ✅ PUT: update data
    @PutMapping("/{id}")
    public TestData update(@PathVariable Long id, @RequestBody TestData data) {
        data.setId(id);
        return service.save(data);
    }

    // ✅ DELETE (basic - will improve later)
    /*
     * @DeleteMapping("/{id}")
     * public void delete(@PathVariable Long id) {
     * service.delete(id); // hard delete for now, will change to soft delete later
     * }
     */

    // ✅ SOFT DELETE (will replace hard delete later)
    // When delete is requested, set deleteStatus to PENDING. Admin will later
    // approve and set to APPROVED
    @DeleteMapping("/{id}")
    public TestData requestDelete(@PathVariable Long id) {

        TestData data = service.getById(id);

        if (data == null) {
            throw new RuntimeException("Data not found");
        }

        data.setDeleteStatus("PENDING"); // mark for deletion

        return service.save(data);
    }

    @DeleteMapping("/admin/{id}")
    public String approveDelete(@PathVariable Long id) {

        TestData data = service.getById(id);

        if (data == null) {
            throw new RuntimeException("Data not found");
        }

        if (!"PENDING".equals(data.getDeleteStatus())) {
            return "Delete request not pending";
        }

        service.delete(id); // hard delete the record

        return "Record deleted successfully";
    }

    // ✅ FILTER API
    @GetMapping("/filter")
    public List<TestData> filter(
            @RequestParam("environment") String env,
            @RequestParam String country) {

        return service.filter(env, country);
    }

    // ✅ Post API to upload Excel file and save data to DB
    @PostMapping("/upload")
    public String uploadExcel(@RequestParam("file") MultipartFile file) {

        try {
            List<TestData> list = excelService.parseExcel(file.getInputStream());

            list.forEach(data -> service.save(data));

            return "File uploaded and data saved successfully";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // ✅ Search API with multiple parameters (fieldName, environment, country)
    @GetMapping("/search")
    public List<TestData> search(
            @RequestParam(required = false) String fieldName,
            @RequestParam(required = false) String environment,
            @RequestParam(required = false) String country) {

        // ✅ If only fieldName provided → treat as VALUE search
        if (fieldName != null && environment == null && country == null) {
            return service.searchByValue(fieldName); // ✅ FIX
        }

        if (fieldName != null && environment != null && country != null) {
            return service.combinedSearch(fieldName, environment, country);
        }

        if (fieldName != null && environment != null) {
            return service.searchByFieldName(fieldName, environment);
        }

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

    // ✅ Search using tag (BEST USE CASE)
    @GetMapping("/searchByTag")
    public List<TestData> searchByTag(
            @RequestParam String tag,
            @RequestParam String environment) {

        return service.searchByTag(tag, environment);
    }

    // ✅ Get pending delete requests (for admin review)
    @GetMapping("/pending")
    public List<TestData> getPendingDeletes() {
        return service.getPendingDeletes();
    }

}