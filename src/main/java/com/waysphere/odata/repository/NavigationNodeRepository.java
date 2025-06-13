package com.waysphere.odata.repository;

import com.waysphere.odata.dto.NavigationNodeRequest;
import com.waysphere.odata.model.NavigationNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NavigationNodeRepository extends JpaRepository<NavigationNode, Long> {
    @Query("""
    SELECT n FROM NavigationNode n 
    WHERE n.nodeType = 'POI' 
    AND n.floorMap.organization.id = :orgId
""")
    List<NavigationNode> findPOINodesByOrganizationId(@Param("orgId") Long orgId);
    List<NavigationNode> findByFloorMap_Id(String floorId);
    List<NavigationNode> findByFloorMap_IdAndNodeType(String floorId, String nodeType);

    @Query(value = """
    SELECT n.node_id
    FROM navigation_node n
    JOIN floor_map f ON n.floor_id = f.id
    WHERE f.level = :level
    ORDER BY (
        POWER(n.latitude - :lat, 2) + POWER(n.longitude - :lng, 2)
    ) ASC
    LIMIT 1
    """, nativeQuery = true)
    Long findNearestNodeId(@Param("lat") double lat,
                           @Param("lng") double lng,
                           @Param("level") int level);

}
