package com.waysphere.odata.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waysphere.odata.model.DigitalFloorFeature;
import com.waysphere.odata.model.FloorMap;
import com.waysphere.odata.repository.DigitalFloorFeatureRepository;
import com.waysphere.odata.repository.FloorMapRepository;
import com.waysphere.odata.utils.GeoJsonUtils;
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
}
