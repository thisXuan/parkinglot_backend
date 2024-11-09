package com.parkinglot_backend.service.impl;

import com.parkinglot_backend.dataStructure.Node;
import com.parkinglot_backend.dto.NavigationPoint;
import com.parkinglot_backend.entity.Points;
import com.parkinglot_backend.entity.Connection;
import com.parkinglot_backend.mapper.ConnectionMapper;
import com.parkinglot_backend.mapper.PointMapper;
import com.parkinglot_backend.service.NavigationService;
import com.parkinglot_backend.util.Result;
import com.parkinglot_backend.dataStructure.Point;
import com.parkinglot_backend.dataStructure.Graph;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: HeYuxin
 * @CreateTime: 2024-11-07
 * @Description: 实现NavigationService接口，从数据库读取点位信息
 */
@Service
public class NavigationServiceImpl implements NavigationService {

    @Resource
    private PointMapper pointsMapper;
    @Resource
    private ConnectionMapper connectionMapper;

//
//    public NavigationServiceImpl(PointMapper pointsMapper, ConnectionMapper connectionMapper) {
//        this.pointsMapper = pointsMapper;
//        this.connectionMapper = connectionMapper;
//    }

    @Override
    public Result getPath(NavigationPoint navigationPoint) {
        //int startX, int startY, int endX, int endY;
        int startX = navigationPoint.getStartX();
        int startY = navigationPoint.getStartY();
        int endX = navigationPoint.getEndX();
        int endY = navigationPoint.getEndY();
        // 使用PointMapper从数据库获取所有点坐标记录
        List<Points> pointsFromDb = pointsMapper.selectAllCoordinates();

        List<Point> points = pointsFromDb.stream()
                .map(p -> new Point(p.getXCoordinate(), p.getYCoordinate()))
                .collect(Collectors.toList());
        List<Connection> connections = connectionMapper.selectAllConnections();

        Graph graph = buildGraph(points, connections);

        Point startPoint = new Point(startX, startY);
        Point endPoint = new Point(endX, endY);

        List<Point> pathCoordinates = aStar(graph, startPoint, endPoint);
        // 打印路径
        if (pathCoordinates != null && !pathCoordinates.isEmpty()) {
            System.out.println("Path found:");
            for (Point point : pathCoordinates) {
                System.out.println("Point: (" + point.x + ", " + point.y + ")");
            }
        } else {
            System.out.println("No path found.");
        }
        if (pathCoordinates == null || pathCoordinates.isEmpty()) {
            return Result.fail("No path found");
        }
        return Result.ok(pathCoordinates);
    }

    private Graph buildGraph(List<Point> points, List<Connection> connections) {
        Graph graph = new Graph();

        // 添加点到图中
        for (Connection connection : connections) {
            // 根据连接中的点ID分别查询x和y坐标
            int x1 = pointsMapper.selectXById(connection.getPointId1());
            int y1 = pointsMapper.selectYById(connection.getPointId1());
            int x2 = pointsMapper.selectXById(connection.getPointId2());
            int y2 = pointsMapper.selectYById(connection.getPointId2());

            // 创建 Point 对象
            Point point1 = new Point(x1, y1);
            Point point2 = new Point(x2, y2);

            // 添加自环或连接边
            graph.addEdge(point1, point1);  // 自环
            graph.addEdge(point1, point2);  // 添加连接点的边
        }
        return graph;
    }
    // A*算法实现，返回从起点到终点的路径
    private List<Point> aStar(Graph graph, Point start, Point end) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        Map<Point, Node> nodeMap = new HashMap<>();
        Map<Point, Boolean> closedList = new HashMap<>();

        Node startNode = new Node(start);
        startNode.h = heuristic(start, end);
        startNode.f = startNode.h;
        openList.add(startNode);
        nodeMap.put(start, startNode);


        while (!openList.isEmpty()) {
            Node current = openList.poll();
            closedList.put(current.point, true);
            if (current.point.equals(end)) {
                return constructPath(current);
            }

            List<Point> neighbors = graph.getNeighbors(current.point);
            for (Point neighbor : neighbors) {

                if (closedList.containsKey(neighbor)) continue;

                double tentativeG = current.g + current.point.distance(neighbor);
                Node neighborNode = nodeMap.get(neighbor);
                double h = heuristic(neighbor, end);
                double f = tentativeG + h;
                if (neighborNode == null || tentativeG < neighborNode.g) {
                    if (neighborNode == null) {
                        neighborNode = new Node(neighbor);
                        nodeMap.put(neighbor, neighborNode);
                    }

                    neighborNode.updateNode(current, tentativeG, h);
                    openList.add(neighborNode);
                }
            }
        }

        System.out.println("未找到路径.");
        return null; // 没有找到路径
    }


    private static double heuristic(Point a, Point b) {
        return a.distance(b);
    }

    private static List<Point> constructPath(Node node) {
        List<Point> path = new ArrayList<>();
        while (node != null) {
            path.add(0, node.point);
            node = node.parent;
        }
        return path;
    }



}



