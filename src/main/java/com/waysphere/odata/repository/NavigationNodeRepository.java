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

}
