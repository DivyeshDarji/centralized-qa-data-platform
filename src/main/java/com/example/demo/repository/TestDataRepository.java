package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.TestData;

@Repository
public interface TestDataRepository extends JpaRepository<TestData, Long> {

    // ✅ Custom method for filtering later - EnvironmentAndCountry
    List<TestData> findByEnvironmentAndCountry(String environment, String country);

    // ✅ Custom method for filtering later - FieldNameAndEnvironment
    List<TestData> findByFieldNameAndEnvironment(String fieldName, String environment);

    // ✅ Custom method for filtering later - TagAndEnvironment
    List<TestData> findByTagAndEnvironment(String tag, String environment);

    // ✅ Search by environment only
    List<TestData> findByEnvironment(String environment);

    // ✅ Custom method for filtering later - EnvironmentAndCountryAndFieldName
    List<TestData> findByFieldNameAndEnvironmentAndCountry(String fieldName, String environment, String country);

    // ✅ Method to find by deleteStatus (for soft delete logic)
    List<TestData> findByDeleteStatus(String deleteStatus);

    // ✅ Custom method for filtering later - Country only
    List<TestData> findByCountry(String country);

    // ✅ Custom method for filtering later - FieldName only
    List<TestData> findByFieldValue(String fieldValue);

    // ✅ Custom method for filtering paged results - EnvironmentAndCountry with
    // deleteStatus not PENDING
    Page<TestData> findByEnvironmentAndCountryAndDeleteStatusNot(
            String environment,
            String country,
            String deleteStatus,
            Pageable pageable);
}