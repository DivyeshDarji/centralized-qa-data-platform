package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.EndUser;

public interface EndUserRepository extends JpaRepository<EndUser, Long> {

    List<EndUser> findByEnvironment(String environment);

    List<EndUser> findByCountry(String country);

    List<EndUser> findByTagAndEnvironment(String tag, String environment);

    List<EndUser> findByDeleteStatus(String deleteStatus);

    List<EndUser> findByEnvironmentAndCountry(String environment, String country);

    Page<EndUser> findByEnvironmentAndCountryAndDeleteStatusNot(
            String environment,
            String country,
            String deleteStatus,
            Pageable pageable);

}