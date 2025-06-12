package com.waysphere.odata.controller;

import com.waysphere.odata.dto.NavigationNodeRequest;
import com.waysphere.odata.dto.NavigationNodeSummaryDTO;
import com.waysphere.odata.model.FloorMap;
import com.waysphere.odata.model.NavigationNode;
import com.waysphere.odata.repository.DigitalFloorFeatureRepository;
import com.waysphere.odata.repository.FloorMapRepository;
import com.waysphere.odata.repository.NavigationNodeRepository;
import com.waysphere.odata.service.PathFindingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/navigation-nodes")
public class NavigationNodeController {

    private final NavigationNodeRepository nodeRepository;
    private final DigitalFloorFeatureRepository featureRepository;
    private final FloorMapRepository floorMapRepository;
    private final PathFindingService pathFindingService;

    public NavigationNodeController(NavigationNodeRepository nodeRepository,
                                    DigitalFloorFeatureRepository featureRepository, FloorMapRepository floorMapRepository, PathFindingService pathFindingService) {
        this.nodeRepository = nodeRepository;
        this.featureRepository = featureRepository;
        this.floorMapRepository = floorMapRepository;
        this.pathFindingService = pathFindingService;
    }

    @PostMapping
    public ResponseEntity<?> createNode(@RequestBody NavigationNodeRequest request) {
        FloorMap floorMap = floorMapRepository.findById(request.getFloorId())
                .orElseThrow(() -> new RuntimeException("Floor not found"));

        NavigationNode node = new NavigationNode();
        node.setFloorMap(floorMap);
        node.setLatitude(request.getLatitude());
        node.setLongitude(request.getLongitude());
        node.setNodeType(request.getNodeType());
        node.setLabel(request.getLabel());
        node.setExitNode(request.isExitNode());

        if (request.isExitNode() && request.getConnectedNodeId() != null) {
            NavigationNode connectedNode = nodeRepository.findById(request.getConnectedNodeId())
                    .orElseThrow(() -> new RuntimeException("Connected node not found"));
            node.setConnectedNode(connectedNode);
        }

        return ResponseEntity.ok(nodeRepository.save(node));
    }
    @GetMapping
    public ResponseEntity<List<NavigationNodeRequest>> getAllNodes() {
        List<NavigationNodeRequest> dtoList = nodeRepository.findAll()
                .stream()
                .map(NavigationNode::toDTO)
                .toList();

        return ResponseEntity.ok(dtoList);
    }
    @GetMapping("/{id}")
    public ResponseEntity<NavigationNodeRequest> getNodeById(@PathVariable Long id) {
        return nodeRepository.findById(id)
                .map(node -> ResponseEntity.ok(node.toDTO()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("floor/poi")
    public ResponseEntity<List<NavigationNodeRequest>> getPOINodesByFloor(@RequestParam String floor) {
        return nodeRepository.findByFloorMap_IdAndNodeType(floor, "POI")
                .stream()
                .map(NavigationNode::toDTO)
                .collect(Collectors.collectingAndThen(Collectors.toList(), ResponseEntity::ok));
    }

    @GetMapping("floor/path")
    public ResponseEntity<List<NavigationNodeRequest>> getPATHNodesByFloor(@RequestParam String floor) {
        return nodeRepository.findByFloorMap_IdAndNodeType(floor, "PATH")
                .stream()
                .map(NavigationNode::toDTO)
                .collect(Collectors.collectingAndThen(Collectors.toList(), ResponseEntity::ok));
    }

    @GetMapping("org/poi")
    public ResponseEntity<List<NavigationNodeSummaryDTO>> getPOINodesByOrgId(@RequestParam Long orgId) {
        List<NavigationNode> poiNodes = nodeRepository.findPOINodesByOrganizationId(orgId);

        List<NavigationNodeSummaryDTO> dtoList = poiNodes.stream()
                .map(node -> new NavigationNodeSummaryDTO(
                        node.getNodeId(),
                        node.getLabel(),
                        node.getFloorMap().getId()
                ))
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/connect")
    public ResponseEntity<?> connectNodes(@RequestBody NavigationNodeRequest request) {
        NavigationNode from = nodeRepository.findById(request.getFromId()).orElseThrow();
        NavigationNode to = nodeRepository.findById(request.getToId()).orElseThrow();

        boolean sameFloor = from.getFloorMap().getId().equals(to.getFloorMap().getId());
        if (!sameFloor) {
            // Cross-floor connections only allowed between exit nodes
            if (!(from.isExitNode() && to.isExitNode())) {
                return ResponseEntity
                        .badRequest()
                        .body("Cross-floor connections must be between exit nodes only.");
            }

            // Optional: enforce explicit connectedNode assignment
            if (!(from.getConnectedNode() != null && from.getConnectedNode().getNodeId().equals(to.getNodeId()) ||
                    to.getConnectedNode() != null && to.getConnectedNode().getNodeId().equals(from.getNodeId()))) {
                return ResponseEntity
                        .badRequest()
                        .body("Exit nodes must reference each other via 'connectedNode'.");
            }
        }

        from.getConnectedNodes().add(to);
        to.getConnectedNodes().add(from); // for bidirectional

        nodeRepository.save(from);
        nodeRepository.save(to);

        return ResponseEntity.ok("Nodes connected");
    }
    @GetMapping("/path")
    public ResponseEntity<List<NavigationNodeRequest>> getPath(@RequestParam Long from, @RequestParam Long to) {
        NavigationNode start = nodeRepository.findById(from).orElseThrow();
        NavigationNode end = nodeRepository.findById(to).orElseThrow();

        List<NavigationNode> path = pathFindingService.findPath(start, end);

        return ResponseEntity.ok(path.stream().map(NavigationNode::toDTO).toList());
    }
}
