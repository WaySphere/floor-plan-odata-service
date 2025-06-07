package com.waysphere.odata.controller;

import com.waysphere.odata.model.FloorMap;
import com.waysphere.odata.model.Organization;
import com.waysphere.odata.repository.FloorMapRepository;
import com.waysphere.odata.repository.OrganizationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/floors")
public class FloorMapController {

    private final FloorMapRepository repository;
    private final OrganizationRepository organizationRepository;

    public FloorMapController(FloorMapRepository repository, OrganizationRepository organizationRepository) {
        this.repository = repository;
        this.organizationRepository = organizationRepository;
    }

    // Create a new FloorMap
    @PostMapping
    public ResponseEntity<?> createFloor(@RequestParam String floorId,
                                         @RequestParam Long orgId,
                                         @RequestParam String dataStoreId,
                                         @RequestParam Integer level) {
        // Find Organization
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        // Create FloorMap
        FloorMap floor = new FloorMap();
        floor.setId(floorId);
        floor.setOrganization(organization);
        floor.setDataStoreId(dataStoreId);
        floor.setLevel(level);

        return ResponseEntity.ok(repository.save(floor));
    }

    @GetMapping("/{orgId}")
    public List<FloorMap> getAllFloors(@PathVariable Long orgId) {
        return repository.findByOrganizationId(orgId);
    }
}

