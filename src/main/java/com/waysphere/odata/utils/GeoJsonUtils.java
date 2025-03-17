package com.waysphere.odata.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waysphere.odata.model.DigitalFloorFeature;
import com.waysphere.odata.model.FloorMap;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.io.geojson.GeoJsonReader;

import java.util.ArrayList;
import java.util.List;

public class GeoJsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static List<DigitalFloorFeature> fromGeoJson(String geoJson, FloorMap floorMap) throws Exception {
        JsonNode rootNode = objectMapper.readTree(geoJson);

        List<DigitalFloorFeature> featuresList = new ArrayList<>();
        GeoJsonReader reader = new GeoJsonReader(geometryFactory);

        for (JsonNode featureNode : rootNode.get("features")) {
            Geometry geometry = reader.read(objectMapper.writeValueAsString(featureNode.get("geometry")));
            geometry.setSRID(4326); // Important for PostGIS

            System.out.println("WKT Geometry: " + new WKTWriter().write(geometry));
            System.out.println("SRID: " + geometry.getSRID());
            System.out.println("Properties: " + featureNode.get("properties"));

            DigitalFloorFeature feature = new DigitalFloorFeature();
            feature.setFloorMap(floorMap);
            feature.setGeometry(geometry);
            feature.setProperties(featureNode.get("properties").toString());

            featuresList.add(feature);
        }
        return featuresList;
    }
}