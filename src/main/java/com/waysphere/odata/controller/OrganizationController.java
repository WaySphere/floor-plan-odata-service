package com.waysphere.odata.controller;

import com.waysphere.odata.model.Organization;
import com.waysphere.odata.repository.OrganizationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationRepository repository;

    public OrganizationController(OrganizationRepository repository) {
        this.repository = repository;
    }

    // Create a new Organization
    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestParam Long id, @RequestParam String name, @RequestParam String domainName) {
        Organization organization = new Organization();
        organization.setId(id);  // Manually set the ID
        organization.setName(name);
        organization.setDomainName(domainName);
        return ResponseEntity.ok(repository.save(organization));
    }

    // Get all Organizations
    @GetMapping
    public List<Organization> getAllOrganizations() {
        return repository.findAll();
    }
}

