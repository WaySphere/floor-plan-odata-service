package com.waysphere.odata.repository;

import com.waysphere.odata.model.FloorMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FloorMapRepository extends JpaRepository<FloorMap, String> {
//    @Query(value = "select * from floor_map where org_id=:orgId", nativeQuery = true)
    List<FloorMap> findByOrganizationId(Long orgId);
}
