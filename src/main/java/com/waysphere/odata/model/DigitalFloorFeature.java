package com.waysphere.odata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKBWriter;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;

@Data
@Entity
@Table(name = "digital_floor_feature")
public class DigitalFloorFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "floor_map_id", nullable = false)
    private FloorMap floorMap;

    @JdbcTypeCode(SqlTypes.GEOMETRY)  // Store as PostGIS Geometry
    @Column(columnDefinition = "geometry(Geometry, 4326)")
    @JsonIgnore
    private byte[] geometry;;

    @JdbcTypeCode(SqlTypes.JSON)  // Store as JSONB
    @Column(columnDefinition = "jsonb")
    private String properties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FloorMap getFloorMap() {
        return floorMap;
    }

    public void setFloorMap(FloorMap floorMap) {
        this.floorMap = floorMap;
    }

    public void setGeometry(Geometry geom) {
        geom.setSRID(4326);
        WKBWriter wkbWriter = new WKBWriter(2, true);
        this.geometry = wkbWriter.write(geom);
    }

    public Geometry getGeometry() {
        try {
            WKBReader wkbReader = new WKBReader();
            return wkbReader.read(this.geometry);
        } catch (Exception e) {
            throw new RuntimeException("Error decoding WKB Geometry", e);
        }
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
