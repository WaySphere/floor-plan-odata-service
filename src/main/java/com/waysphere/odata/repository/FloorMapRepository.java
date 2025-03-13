package com.waysphere.odata.repository;

import com.waysphere.odata.model.FloorMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorMapRepository extends JpaRepository<FloorMap, String> {
}
