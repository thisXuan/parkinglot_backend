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

   // Map<Integer, Point> pointMap = new HashMap<>();

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

        Integer startId = pointsMapper.selectIdByCoordinates(startX, startY);
        Integer endId = pointsMapper.selectIdByCoordinates(endX, endY);

        if (startId == null || endId == null) {
            return Result.fail("起点或终点坐标未找到");
        }
        // 根据ID查询起点和终点的详细信息
        Points startPointsFromDb = pointsMapper.selectCoordinatesById(startId);
        Points endPointsFromDb = pointsMapper.selectCoordinatesById(endId);

        // 使用查询的坐标、楼层和电梯信息创建Point对象
        Point startPoint = new Point(startPointsFromDb.getXCoordinate(), startPointsFromDb.getYCoordinate(),
                startPointsFromDb.getFloor(), startPointsFromDb.getIsElevator());
        Point endPoint = new Point(endPointsFromDb.getXCoordinate(), endPointsFromDb.getYCoordinate(),
                endPointsFromDb.getFloor(), endPointsFromDb.getIsElevator());

        // 使用PointMapper从数据库获取所有点坐标记录
        List<Points> pointsFromDb = pointsMapper.selectAllCoordinates();
        // 将点的ID和对应的Point对象存储在HashMap中
        Map<Integer, Point> pointMap = new HashMap<>();
        for (Points pointFromDb : pointsFromDb) {
            int id = pointFromDb.getId(); // 假设Points类有getId()方法
            Point point = new Point(pointFromDb.getXCoordinate(), pointFromDb.getYCoordinate(), pointFromDb.getFloor(), pointFromDb.getIsElevator());
            pointMap.put(id, point);
        }

        List<Point> points = pointsFromDb.stream()
                .map(p -> pointMap.get(p.getId())) // 使用pointMap来获取Point对象
                .collect(Collectors.toList());

//        // 打印所有点信息
//        System.out.println("Points from DB:");
//        points.forEach(point -> System.out.println("Point: (" + point.x + ", " + point.y + ")"));


        List<Connection> connections = connectionMapper.selectAllConnections();

//        // 打印所有连接信息
//        System.out.println("\nConnections from DB:");
//        connections.forEach(connection -> System.out.println("Connection: " + connection.getPointId1() + " -> " + connection.getPointId2()));
        // 打印当前时间1
        long startTime1 = System.currentTimeMillis();
        Graph graph = buildGraph(pointMap, points, connections);
        //当前时间1
        long startTime = System.currentTimeMillis();
        long timeDifference1 = startTime - startTime1;
        System.out.println("时间差1: " + timeDifference1 + "毫秒");



        List<Point> pathCoordinates = aStar(graph, startPoint, endPoint);

        // 打印当前时间2
        long endTime = System.currentTimeMillis();
        System.out.println("当前时间2: " + endTime);
        // 计算时间差
        long timeDifference = endTime - startTime;
        System.out.println("时间差: " + timeDifference + "毫秒");

        // 打印路径
        if (pathCoordinates != null && !pathCoordinates.isEmpty()) {
            System.out.println("Path found:");
            for (Point point : pathCoordinates) {
                System.out.println("Point: (" + point.x + ", " + point.y + ", "+ point.floor + ", "+ point.isElevator+ ")");
            }
        } else {
            System.out.println("No path found.");
        }
        if (pathCoordinates == null || pathCoordinates.isEmpty()) {
            return Result.fail("未找到路径");
        }
        return Result.ok(pathCoordinates);
    }

    private Graph buildGraph(Map<Integer, Point> pointMap, List<Point> points, List<Connection> connections) {
        Graph graph = new Graph();

        for (Connection connection : connections) {
            Integer pointId1 = connection.getPointId1();
            Integer pointId2 = connection.getPointId2();

            // 获取两个点的信息
            Point point1 = pointMap.get(pointId1);
            Point point2 = pointMap.get(pointId2);

            if (point1 != null && point2 != null) {
                // 获取点的楼层和电梯信息
                String floor1 = point1.getFloor();
                String floor2 = point2.getFloor();
                boolean isElevator1 = point1.isElevator();
                boolean isElevator2 = point2.isElevator();

                // 判断是否可以建立连接
                if (floor1.equals(floor2) || (isElevator1 && isElevator2)) {
                   // System.out.println("Point: (" + point1.x + ", " + point1.y + point1.floor + point1.isElevator+ ")");
                   // System.out.println("Point: (" + point2.x + ", " + point2.y + point2.floor + point2.isElevator+ ")");
                    // 如果两个点在同一层，或者它们都与电梯连接，则建立边
                    //graph.addEdge(point1, point1);
                    graph.addEdge(point1, point2);
                }
            }
        }
        //System.out.println("Graph edges:");
        //graph.printEdges();

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



