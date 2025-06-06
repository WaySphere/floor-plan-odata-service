package com.waysphere.odata.repository;

import com.waysphere.odata.model.DigitalFloorFeature;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Transactional
    @Query(value = """
    DELETE FROM digital_floor_feature 
    WHERE floor_map_id IN (
        SELECT id FROM floor_map WHERE org_id = :orgId AND id = :floorId
    )
""", nativeQuery = true)
    int deleteByFloorId(@Param("orgId") Long orgId, @Param("floorId") String floorId);

    @Query(value = """
    SELECT fm.org_id 
    FROM digital_floor_feature dff
    JOIN floor_map fm ON dff.floor_map_id = fm.id
    WHERE dff.id = :featureId
""", nativeQuery = true)
    Long findOrgIdByFeatureId(@Param("featureId") Long featureId);
}
