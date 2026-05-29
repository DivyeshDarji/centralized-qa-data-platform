package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "test_data")
public class TestData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String fieldName;
    private String fieldValue;

    private String environment; // QA, UAT, PROD
    private String country;

    private String validationType; // valid, invalid, boundary
    private String status; // active, inactive

    //private String deleteStatus; // PENDING, APPROVED
    
    @Column(name = "delete_status")
    private String deleteStatus = "NONE"; //NONE / PENDING / APPROVED

    private String tag; // e.g., login_valid_user
}
