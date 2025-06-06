package com.waysphere.odata.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.waysphere.odata.model.DigitalFloorFeature;
import com.waysphere.odata.model.FloorMap;
import com.waysphere.odata.repository.DigitalFloorFeatureRepository;
import com.waysphere.odata.repository.FloorMapRepository;
import com.waysphere.odata.utils.GeoJsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/floor-features")
public class DigitalFloorFeatureController {

    private final DigitalFloorFeatureRepository repository;
    private final FloorMapRepository floorMapRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DigitalFloorFeatureController(DigitalFloorFeatureRepository repository, FloorMapRepository floorMapRepository) {
        this.repository = repository;
        this.floorMapRepository = floorMapRepository;
    }

    @PostMapping("/{orgId}/{floorId}")
    public ResponseEntity<?> saveFeatureCollection(@PathVariable Long orgId,
                                                   @PathVariable String floorId,
                                                   @RequestBody String geoJson) {
        try {
            FloorMap floorMap = floorMapRepository.findById(floorId)
                    .orElseThrow(() -> new RuntimeException("FloorMap not found for ID: " + floorId));

            List<DigitalFloorFeature> features = GeoJsonUtils.fromGeoJson(geoJson, floorMap);
            repository.saveAll(features);

            return ResponseEntity.ok("FeatureCollection saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{orgId}/{floorId}")
    public ResponseEntity<Map<String, Object>> getFeatures(@PathVariable Long orgId,
                                                           @PathVariable String floorId) {
        List<Object[]> results = repository.findFeaturesAsGeoJSON(orgId, floorId);

        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, Object>> features = results.stream().map(row -> {
            try {
                JsonNode geometryNode = objectMapper.readTree(row[1].toString()); // Convert String to JSON
                JsonNode propertiesNode = objectMapper.readTree(row[2].toString());
                ((ObjectNode)propertiesNode).put("id", row[0].toString());
                return Map.of(
                        "type", "Feature",
                        "geometry", geometryNode,
                        "properties", propertiesNode
                );
            } catch (Exception e) {
                throw new RuntimeException("Error processing JSON", e);
            }
        }).collect(Collectors.toList());

        Map<String, Object> featureCollection = Map.of(
                "type", "FeatureCollection",
                "features", features
        );

        return ResponseEntity.ok(featureCollection);
    }

    @DeleteMapping("/{orgId}/{floorId}")
    public ResponseEntity<?> deleteFeaturesByFloor(
            @PathVariable Long orgId,
            @PathVariable String floorId) {

        int deletedCount = repository.deleteByFloorId(orgId, floorId);

        if (deletedCount > 0) {
            return ResponseEntity.ok("Deleted " + deletedCount + " features for floor: " + floorId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No features found for floor: " + floorId);
        }
    }

    @DeleteMapping("/{orgId}/{floorId}/{featureId}")
    public ResponseEntity<?> deleteFeature(@PathVariable Long orgId,
                                           @PathVariable String floorId,
                                           @PathVariable Long featureId) {
        try {
            if (!repository.existsById(featureId)) {
                return ResponseEntity.badRequest().body("Feature not found");
            }
            repository.deleteById(featureId);
            return ResponseEntity.ok("Feature deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    @PutMapping("/{orgId}/{floorId}/{featureId}")
    public ResponseEntity<?> updateFeature(@PathVariable Long orgId,
                                           @PathVariable String floorId,
                                           @PathVariable Long featureId,
                                           @RequestBody String geoJson) {
        try {
            // Step 1: Fetch the existing feature by ID
            DigitalFloorFeature existingFeature = repository.findById(featureId)
                    .orElseThrow(() -> new RuntimeException("Feature not found for ID: " + featureId));

            // Step 2: Convert new GeoJSON data into a List of features
            List<DigitalFloorFeature> featureList = GeoJsonUtils.fromGeoJson(geoJson, existingFeature.getFloorMap());

            // Step 3: Ensure only one feature is being updated
            if (featureList.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid GeoJSON: No features found");
            }

            DigitalFloorFeature updatedFeature = featureList.get(0); // Since we are updating one feature

            // Step 4: Update existing feature (Preserve ID)
            existingFeature.setGeometry(updatedFeature.getGeometry());  // Update Geometry
            existingFeature.setProperties(updatedFeature.getProperties()); // Update Properties

            // Step 5: Save the updated feature
            repository.save(existingFeature);

            return ResponseEntity.ok("Feature updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/feature/{featureId}/orgid")
    public ResponseEntity<?> getOrgIdByFeatureId(@PathVariable Long featureId) {
        try {
            Long orgId = repository.findOrgIdByFeatureId(featureId);
            if (orgId == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Organization ID not found for feature ID: " + featureId);
            }
            return ResponseEntity.ok(Map.of("orgId", orgId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving orgId: " + e.getMessage());
        }
    }


}
