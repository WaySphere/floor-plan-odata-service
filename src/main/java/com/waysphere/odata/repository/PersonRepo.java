package com.waysphere.odata.repository;

import com.waysphere.odata.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface PersonRepo extends JpaRepository<Person, Long> {
    List<Person> findByOrganizationIdAndAdminTrue(Long orgId);
    Optional<Person> findByEmail(String email);
}