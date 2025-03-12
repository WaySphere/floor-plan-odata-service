package com.waysphere.odata.controller;

import com.waysphere.odata.model.Person;
import com.waysphere.odata.repository.PersonRepo;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class PersonController {

    @Autowired
    private PersonRepo repo;

    @PostMapping("/addPerson")
    public void addPerson(@RequestBody Person person) {
        repo.save(person);
    }
    
    
}
