package com.waysphere.odata.repository;

import com.waysphere.odata.model.DigitalFloorFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DigitalFloorFeatureRepository extends JpaRepository<DigitalFloorFeature, Long> {

    @Query(value = """
        SELECT id, ST_AsGeoJSON(geometry) AS geojson, properties
        FROM digital_floor_feature
        WHERE floor_map_id IN 
            (SELECT id FROM floor_map WHERE org_id = :orgId AND id = :floorId)
    """, nativeQuery = true)
    List<Object[]> findFeaturesAsGeoJSON(@Param("orgId") Long orgId, @Param("floorId") String floorId);
}
