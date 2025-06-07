package com.waysphere.odata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waysphere.odata.dto.NavigationNodeRequest;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "navigation_node")
public class NavigationNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
    private Long nodeId;

    @ManyToOne
    @JoinColumn(name = "floor_id", nullable = false)
    private FloorMap floorMap;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "node_type", nullable = false, length = 20)
    private String nodeType;  // POI or PATH_POINT

    @Column(name = "label", length = 100)
    private String label;

    @Column(name = "exit_node", nullable = false)
    private boolean exitNode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connected_node_id")
    @JsonIgnore
    private NavigationNode connectedNode;  // used if exitNode = true

    @ManyToMany
    @JoinTable(
            name = "node_connections",
            joinColumns = @JoinColumn(name = "from_node_id"),
            inverseJoinColumns = @JoinColumn(name = "to_node_id")
    )
    private List<NavigationNode> connectedNodes = new ArrayList<>();

    public List<NavigationNode> getConnectedNodes() {
        return connectedNodes;
    }

    public void setConnectedNodes(List<NavigationNode> connectedNodes) {
        this.connectedNodes = connectedNodes;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public FloorMap getFloorMap() {
        return floorMap;
    }

    public void setFloorMap(FloorMap floorMap) {
        this.floorMap = floorMap;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isExitNode() {
        return exitNode;
    }

    public void setExitNode(boolean exitNode) {
        this.exitNode = exitNode;
    }

    public NavigationNode getConnectedNode() {
        return connectedNode;
    }

    public void setConnectedNode(NavigationNode connectedNode) {
        this.connectedNode = connectedNode;
    }

    public NavigationNodeRequest toDTO() {
        NavigationNodeRequest request = new NavigationNodeRequest();
        request.setNodeId(this.nodeId);
        request.setFloorId(floorMap.getId());
        request.setLatitude(latitude);
        request.setLongitude(longitude);
        request.setNodeType(nodeType);
        request.setLabel(label);
        request.setExitNode(exitNode);
        if (connectedNode != null) {
            request.setConnectedNodeId(connectedNode.getNodeId());
        }
        return request;
    }
}
