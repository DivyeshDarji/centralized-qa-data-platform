package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TestData;
import com.example.demo.repository.TestDataRepository;

@Service
public class TestDataService {

    @Autowired
    private TestDataRepository repository;

    // ✅ Get all data
    // For now, we will return all data including those with deleteStatus = PENDING.
    // We can later add logic to filter them out if needed.
    public List<TestData> getAll() {
        return repository.findAll()
                .stream()
                .filter(data -> !"PENDING".equals(data.getDeleteStatus()))
                .toList();
    }

    // ✅ Helper method to filter out PENDING deleteStatus (if needed in future)
    private List<TestData> removePending(List<TestData> list) {
        return list.stream()
                .filter(data -> !"PENDING".equals(data.getDeleteStatus()))
                .toList();
    }

    // ✅ Save (Create + Update)
    public TestData save(TestData data) {
        return repository.save(data);
    }

    // ✅ Get by ID
    public TestData getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // ✅ Delete request (soft delete logic later)
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // ✅ Filter (important for your requirement)
    public List<TestData> filter(String env, String country) {
        return removePending(repository.findByEnvironmentAndCountry(env, country));
    }

    // ✅ Search by fieldName + environment
    public List<TestData> searchByFieldName(String fieldName, String env) {
        return removePending(repository.findByFieldNameAndEnvironment(fieldName, env));
    }

    // ✅ Search using tag (BEST USE CASE)
    public List<TestData> searchByTag(String tag, String env) {
        return removePending(repository.findByTagAndEnvironment(tag, env));
    }

    // ✅ Search by environment only
    public List<TestData> findByEnvironment(String env) {
        return removePending(repository.findByEnvironment(env));
    }

    // ✅ Combined filter (fieldName + environment + country)
    public List<TestData> combinedSearch(String fieldName, String env, String country) {
        return removePending(repository.findByFieldNameAndEnvironmentAndCountry(fieldName, env, country));
    }

    // ✅ Get records with deleteStatus = PENDING (for admin review)
    public List<TestData> getPendingDeletes() {
        return repository.findByDeleteStatus("PENDING");
    }

    // ✅ Custom method for filtering later - Country only
    public List<TestData> findByCountry(String country) {
        return removePending(repository.findByCountry(country));
    }

    // ✅ Custom method for filtering later - FieldName only
    public List<TestData> searchByValue(String value) {
        return removePending(repository.findByFieldValue(value));
    }

}