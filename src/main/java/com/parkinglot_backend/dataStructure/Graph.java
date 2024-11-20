package com.parkinglot_backend.dataStructure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: HeYuxin
 * @CreateTime: 2024-11-07
 * @Description:
 */
public class Graph {
    private Map<Point, List<Point>> adjList = new HashMap<>();

    public void addEdge(Point u, Point v) {
        adjList.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        // If the graph is undirected, also add v -> u
        adjList.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
    }

    public void printEdges() {
        for (Map.Entry<Point, List<Point>> entry : adjList.entrySet()) {
            Point source = entry.getKey();
            List<Point> neighbors = entry.getValue();
            for (Point neighbor : neighbors) {
                System.out.println("Edge: " + source + " -> " + neighbor);
            }
        }
    }

    public List<Point> getNeighbors(Point node) {
        return adjList.getOrDefault(node, new ArrayList<>());
    }
}