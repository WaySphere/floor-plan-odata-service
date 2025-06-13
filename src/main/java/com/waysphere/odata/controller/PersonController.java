package com.waysphere.odata.controller;

import com.waysphere.odata.model.Organization;
import com.waysphere.odata.model.Person;
import com.waysphere.odata.repository.OrganizationRepository;
import com.waysphere.odata.repository.PersonRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;


@RestController
public class PersonController {

    @Autowired
    private PersonRepo repo;
    @Autowired
    private OrganizationRepository organizationRepository;

    @PostMapping("/addPerson/{orgId}")
    public void addPerson(@PathVariable Long orgId, @RequestBody Person person) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Org not found"));
        person.setOrganization(org);
        person.setAdmin(true);  // or false depending on if this person is admin or not

        repo.save(person);
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Long id) {
        Optional<Person> person = repo.findById(id);
        if (person.isPresent()) {
            return ResponseEntity.ok(person.get());
        } else {
            return ResponseEntity.status(404).body("Person not found with id: " + id);
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        // Validate credentials
        Optional<Person> personOpt = repo.findByEmail(email);
        if (personOpt.isPresent()) {
            Person person = personOpt.get();
            if (person.isAdmin() && person.getPassword().equals(password)) {
                return ResponseEntity.ok(Map.of("organizationId", person.getOrganization().getId()));
            } else {
                return ResponseEntity.status(403).body("Access denied. Not an admin or invalid password.");
            }
        } else {
            return ResponseEntity.status(404).body("Person not found with email: " + email);
        }
    }
}
