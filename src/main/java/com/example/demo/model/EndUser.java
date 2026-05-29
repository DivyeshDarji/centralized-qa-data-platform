package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "end_user")
public class EndUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endUserName;
    private String marketSegmentType;

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;

    private String city;
    private String state;
    private String postalCode;
    private String country;

    private String contactName;
    private String phoneNumber;
    private String email;

    private String environment; // PROD, DEV, QA
    private String validationType;
    private String tag;

    private String deleteStatus = "NONE"; // default
}