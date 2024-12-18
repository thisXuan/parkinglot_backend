package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.core.assist.ISqlRunner;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.parkinglot_backend.dataStructure.Node;
import com.parkinglot_backend.dto.NavigationPoint;
import com.parkinglot_backend.entity.*;
import com.parkinglot_backend.mapper.*;
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
    @Resource
    private StoreMapper storeMapper;
    @Resource
    private ParkingSpotMapper parkingSpotMapper;

    @Resource
    private ParkingPointMapper parkingPointMapper;

    @Resource
    private ParkingConnectMapper parkingConnectMapper;
    // Map<Integer, Point> pointMap = new HashMap<>();

//
//    public NavigationServiceImpl(PointMapper pointsMapper, ConnectionMapper connectionMapper) {
//        this.pointsMapper = pointsMapper;
//        this.connectionMapper = connectionMapper;
//    }

    @Override
    public Result getPath(NavigationPoint navigationPoint) {
        boolean startFlag = false;
        boolean endFlag = false;
        // 前端传入起点name和终点name，查询数据库获得两个点的point id
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("StoreName", navigationPoint.getStartName());
        Store store = storeMapper.selectOne(queryWrapper);
        System.out.println(store);
        Integer start_pointId = null;
        if (store == null) {
            // 如果商铺未找到，从 parking_lot 表中查询
            QueryWrapper<ParkingSpot> parkingLotQueryWrapper = new QueryWrapper<>();
            parkingLotQueryWrapper.eq("spot_name", navigationPoint.getStartName());
            ParkingSpot parkingLot = parkingSpotMapper.selectOne(parkingLotQueryWrapper);

            if (parkingLot == null) {
                return Result.fail("起始点未找到");
            }else {
                startFlag = true;
            }
            start_pointId = parkingLot.getSpotId(); // 假设 ParkingLot 表有 pointId 字段
        } else {
            start_pointId = storeMapper.findPointIdByStoreId(store.getId());
            if (start_pointId == null) {
                return Result.fail("未找到起始点");
            }
        }

        QueryWrapper<Store> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("StoreName", navigationPoint.getEndName());
        Store store1 = storeMapper.selectOne(queryWrapper1);
        Integer end_pointId = null;
        if (store1 == null) {
            // 如果商铺未找到，从 parking_lot 表中查询
            QueryWrapper<ParkingSpot> parkingLotQueryWrapper = new QueryWrapper<>();
            parkingLotQueryWrapper.eq("spot_name", navigationPoint.getEndName());
            ParkingSpot parkingLot1 = parkingSpotMapper.selectOne(parkingLotQueryWrapper);
            System.out.println(parkingLot1);
            if (parkingLot1 == null) {
                return Result.fail("起始点未找到");
            }else {
                endFlag = true;
            }
            end_pointId = parkingLot1.getSpotId(); // 假设 ParkingLot 表有 pointId 字段
        } else {
            end_pointId = storeMapper.findPointIdByStoreId(store1.getId());
            if (end_pointId == null) {
                return Result.fail("未找到起始点");
            }
        }
        // 根据ID查询起点和终点的详细信息
        Points startPointsFromDb = null;
        Points endPointsFromDb = null;
        if (startFlag){
            startPointsFromDb = parkingPointMapper.selectCoordinatesById(start_pointId);
        }else {
            startPointsFromDb = pointsMapper.selectCoordinatesById(start_pointId);
        }

        if (endFlag){
            endPointsFromDb = parkingPointMapper.selectCoordinatesById(end_pointId);
        }else {
            endPointsFromDb = pointsMapper.selectCoordinatesById(end_pointId);
        }


        if(startPointsFromDb == null) {
            return Result.fail("未找到起始点");
        }

        if(endPointsFromDb == null) {
            return Result.fail("未找到终点");
        }

        // 使用查询的坐标、楼层和电梯信息创建Point对象
        Point startPoint = new Point(startPointsFromDb.getXCoordinate(), startPointsFromDb.getYCoordinate(),
                startPointsFromDb.getFloor(), startPointsFromDb.getIsElevator());
        Point endPoint = new Point(endPointsFromDb.getXCoordinate(), endPointsFromDb.getYCoordinate(),
                endPointsFromDb.getFloor(), endPointsFromDb.getIsElevator());

        // 使用PointMapper从数据库获取所有点坐标记录
        List<Points> pointsFromDb = pointsMapper.selectAllCoordinates();
        List<ParkingPoint> parkingPointsFromDb = parkingPointMapper.selectAllCoordinates();
        // 将点的ID和对应的Point对象存储在HashMap中
        Map<Integer, Point> pointMap = new HashMap<>();
        Map<Integer, Point> parkingLotpointMap = new HashMap<>();
        for (Points pointFromDb : pointsFromDb) {
            int id = pointFromDb.getId(); // 假设Points类有getId()方法
            Point point = new Point(pointFromDb.getXCoordinate(), pointFromDb.getYCoordinate(), pointFromDb.getFloor(), pointFromDb.getIsElevator());
            pointMap.put(id, point);
        }
        for (ParkingPoint e : parkingPointsFromDb){
            int id = e.getId();
            Point point = new Point(e.getXCoordinate(), e.getYCoordinate(), e.getFloor(), e.getIsElevator());
            parkingLotpointMap.put(id, point);
        }

        List<Point> points = pointsFromDb.stream()
                .map(p -> pointMap.get(p.getId())) // 使用pointMap来获取Point对象
                .collect(Collectors.toList());

        List<Point> parkingPoints = parkingPointsFromDb.stream()
                .map(p -> parkingLotpointMap.get(p.getId()))
                .collect(Collectors.toList());

//        // 打印所有点信息
//        System.out.println("Points from DB:");
//        points.forEach(point -> System.out.println("Point: (" + point.x + ", " + point.y + ")"));


        List<Connection> connections = connectionMapper.selectAllConnections();
        List<Connection> parkingConnects = parkingConnectMapper.selectAllConnections();
//        // 打印所有连接信息
//        System.out.println("\nConnections from DB:");
//        connections.forEach(connection -> System.out.println("Connection: " + connection.getPointId1() + " -> " + connection.getPointId2()));
        // 打印当前时间1
        long startTime1 = System.currentTimeMillis();
        Graph graph = buildGraph(pointMap, points, connections);
        Graph graph1 = buildGraph(parkingLotpointMap , parkingPoints , parkingConnects);
        Graph mergeGraph = mergeGraphs(graph,graph1);

        //当前时间1
        long startTime = System.currentTimeMillis();
        long timeDifference1 = startTime - startTime1;
        System.out.println("时间差1: " + timeDifference1 + "毫秒");



        List<Point> pathCoordinates = aStar(mergeGraph, startPoint, endPoint);

        // 打印当前时间2
        long endTime = System.currentTimeMillis();
        System.out.println("当前时间2: " + endTime);
        // 计算时间差
        long timeDifference = endTime - startTime;
        System.out.println("时间差: " + timeDifference + "毫秒");

        if (pathCoordinates == null || pathCoordinates.isEmpty()) {
            return Result.fail("未找到路径");
        }

        // 处理一下pathCoordinates，只有起点和终点所在楼层的路径
        List<Point> list = new ArrayList<>();

        for(Point point : pathCoordinates) {
            if(point.getFloor().equals(startPoint.getFloor()) || point.getFloor().equals(endPoint.getFloor())) {
                list.add(point);
            }
        }

        // 打印路径
        if (!list.isEmpty()) {
            System.out.println("Path found:");
            for (Point point : list) {
                System.out.println("Point: (" + point.x + ", " + point.y + ", "+ point.floor + ", "+ point.isElevator+ ")");
            }
        } else {
            System.out.println("No path found.");
        }

        return Result.ok(list);
    }

    public Graph mergeGraphs(Graph graph, Graph graph1) {
        // 创建一个新的 Graph 对象，用于存储合并结果
        Graph mergedGraph = new Graph();

        // 将 graph 中的点和边添加到 mergedGraph
        for (Map.Entry<Point, List<Point>> entry : graph.getAdjList().entrySet()) {
            Point source = entry.getKey();
            for (Point neighbor : entry.getValue()) {
                mergedGraph.addEdge(source, neighbor);
            }
        }

        // 将 graph1 中的点和边添加到 mergedGraph
        for (Map.Entry<Point, List<Point>> entry : graph1.getAdjList().entrySet()) {
            Point source = entry.getKey();
            for (Point neighbor : entry.getValue()) {
                mergedGraph.addEdge(source, neighbor);
            }
        }

        // 跨图连接1: (31.23, 67.52, B1, true) -> (51.3, 40.69, B2, true)
        Point point1Graph = findPointInGraph(graph, 31.23, 67.52, "B1", true);
        Point point1Graph1 = findPointInGraph(graph1, 51.3, 40.69, "B2", true);
        if (point1Graph != null && point1Graph1 != null) {
            mergedGraph.addEdge(point1Graph, point1Graph1);
        }

        // 跨图连接2: (69.32, 52.74, B1, true) -> (30.46, 17.99, B2, true)
        Point point2Graph = findPointInGraph(graph, 69.32, 52.74, "B1", true);
        Point point2Graph1 = findPointInGraph(graph1, 30.46, 17.99, "B2", true);
        if (point2Graph != null && point2Graph1 != null) {
            mergedGraph.addEdge(point2Graph, point2Graph1);
        }

        // 跨图连接3: (53.82, 75.75, B1, true) -> (55.65, 26.88, B2, true)
        Point point3Graph = findPointInGraph(graph, 53.82, 75.75, "B1", true);
        Point point3Graph1 = findPointInGraph(graph1, 55.65, 26.88, "B2", true);
        if (point3Graph != null && point3Graph1 != null) {
            mergedGraph.addEdge(point3Graph, point3Graph1);
        }

        return mergedGraph;
    }

    // 根据坐标和楼层信息查找点
    private Point findPointInGraph(Graph graph, double x, double y, String floor, boolean isElevator) {
        for (Map.Entry<Point, List<Point>> entry : graph.getAdjList().entrySet()) {
            Point point = entry.getKey();
            if (point.getX() == x && point.getY() == y && point.getFloor().equals(floor) && point.isElevator() == isElevator) {
                return point;
            }
        }
        return null;
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



