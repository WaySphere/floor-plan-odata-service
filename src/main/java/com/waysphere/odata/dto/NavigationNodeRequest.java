package com.waysphere.odata.dto;

import lombok.Data;

@Data
public class NavigationNodeRequest {
    private Long nodeId;
    private String floorId;
    private Double latitude;
    private Double longitude;
    private String nodeType;
    private String label;
    private boolean exitNode;
    private Long connectedNodeId;
    private Long fromId;
    private Long toId;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public Long getConnectedNodeId() {
        return connectedNodeId;
    }

    public void setConnectedNodeId(Long connectedNodeId) {
        this.connectedNodeId = connectedNodeId;
    }
}
