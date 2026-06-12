package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.model.EndUser;
import com.example.demo.repository.EndUserRepository;

@Service
public class EndUserService {

    @Autowired
    private EndUserRepository repository;

    public List<EndUser> getAll() {
        return repository.findAll()
                .stream()
                .filter(e -> !"PENDING".equals(e.getDeleteStatus()))
                .toList();
    }

    public EndUser save(EndUser data) {

        if (data.getDeleteStatus() == null) {
            data.setDeleteStatus("NONE");
        }

        List<EndUser> existing = repository.findByTagAndEnvironment(data.getTag(), data.getEnvironment());

        for (EndUser e : existing) {

            // ✅ Ignore SAME record (important)
            if (!e.getId().equals(data.getId())) {
                throw new RuntimeException(
                        "Duplicate tag found for environment: " + data.getEnvironment());
            }
        }

        return repository.save(data);
    }

    public EndUser getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<EndUser> findByEnvironment(String env) {
        return repository.findByEnvironment(env)
                .stream()
                .filter(e -> !"PENDING".equals(e.getDeleteStatus()))
                .toList();
    }

    public List<EndUser> findByCountry(String country) {
        return repository.findByCountry(country)
                .stream()
                .filter(e -> !"PENDING".equals(e.getDeleteStatus()))
                .toList();
    }

    public List<EndUser> findByTag(String tag, String env) {
        return repository.findByTagAndEnvironment(tag, env)
                .stream()
                .filter(e -> !"PENDING".equals(e.getDeleteStatus()))
                .toList();
    }

    public List<EndUser> filter(String env, String country) {

        if (env != null && country != null) {
            return repository.findByEnvironmentAndCountry(env, country)
                    .stream()
                    .filter(e -> !"PENDING".equals(e.getDeleteStatus()))
                    .toList();
        }

        if (env != null) {
            return repository.findByEnvironment(env);
        }

        if (country != null) {
            return repository.findByCountry(country);
        }

        return repository.findAll();
    }

    public Page<EndUser> search(String env, String country, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return repository.findByEnvironmentAndCountryAndDeleteStatusNot(
                env,
                country,
                "PENDING",
                pageable);
    }

    public List<EndUser> getPending() {
        return repository.findByDeleteStatus("PENDING");
    }

    public EndUser requestDelete(Long id) {
        EndUser data = getById(id);
        if (data != null) {
            data.setDeleteStatus("PENDING");
            return repository.save(data);
        }
        return null;
    }

    public String approveDelete(Long id) {
        EndUser data = getById(id);
        if (data == null)
            return "Not Found";

        if (!"PENDING".equals(data.getDeleteStatus())) {
            return "Not Pending";
        }

        repository.deleteById(id);
        return "Deleted";
    }

}