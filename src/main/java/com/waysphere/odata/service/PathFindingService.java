package com.waysphere.odata.service;

import com.waysphere.odata.model.NavigationNode;
import com.waysphere.odata.repository.NavigationNodeRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PathFindingService {

    private final NavigationNodeRepository nodeRepository;

    public PathFindingService(NavigationNodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public List<NavigationNode> findPath(NavigationNode start, NavigationNode end) {
        Queue<List<NavigationNode>> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        queue.add(List.of(start));
        visited.add(start.getNodeId());

        while (!queue.isEmpty()) {
            List<NavigationNode> path = queue.poll();
            NavigationNode last = path.get(path.size() - 1);

            if (last.getNodeId().equals(end.getNodeId())) {
                return path;
            }

            for (NavigationNode neighbor : last.getConnectedNodes()) {
                if (!visited.contains(neighbor.getNodeId())) {
                    visited.add(neighbor.getNodeId());
                    List<NavigationNode> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }

            // Handle cross-floor connection via exit node
            if (last.isExitNode() && last.getConnectedNode() != null) {
                NavigationNode exitTarget = last.getConnectedNode();
                if (!visited.contains(exitTarget.getNodeId())) {
                    visited.add(exitTarget.getNodeId());
                    List<NavigationNode> newPath = new ArrayList<>(path);
                    newPath.add(exitTarget);
                    queue.add(newPath);
                }
            }
        }

        return List.of(); // No path found
    }
}
