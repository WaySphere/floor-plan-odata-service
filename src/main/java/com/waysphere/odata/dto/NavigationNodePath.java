package com.waysphere.odata.dto;

public class NavigationNodePath {
    private Long nodeId;
    private String floorId;
    private Double latitude;
    private Double longitude;
    private String nodeType;
    private String label;
    private boolean exitNode;
    private Long connectedNodeId;
    private Integer Level;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
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

    public Integer getLevel() {
        return Level;
    }

    public void setLevel(Integer level) {
        Level = level;
    }
}
