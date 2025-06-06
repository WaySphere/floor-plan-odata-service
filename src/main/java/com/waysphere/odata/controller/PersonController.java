package com.waysphere.odata.controller;

import com.waysphere.odata.model.Organization;
import com.waysphere.odata.model.Person;
import com.waysphere.odata.repository.OrganizationRepository;
import com.waysphere.odata.repository.PersonRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

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

}
