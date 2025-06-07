package com.waysphere.odata.dto;

public class NavigationNodeSummaryDTO {
    private Long nodeId;
    private String label;
    private String floorId;

    public NavigationNodeSummaryDTO(Long nodeId, String label, String floorId) {
        this.nodeId = nodeId;
        this.label = label;
        this.floorId = floorId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }
// Getters and Setters or use Lombok @Data
}
