package com.parkinglot_backend.service.impl;

import com.baomidou.mybatisplus.core.assist.ISqlRunner;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.parkinglot_backend.dataStructure.Node;
import com.parkinglot_backend.dto.NavigationPoint;
import com.parkinglot_backend.dto.ResultPointDTO;
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

    @Resource
    private StorePointMapper storePointMapper;

    private List<List<Point>> connectedPoints;

    @Override
    public Result getPath(NavigationPoint navigationPoint,int mode) {
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
            System.out.println("parkingLot1"+parkingLot1);
           // System.out.println(parkingLot1);
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
        Point startPoint = new Point(startPointsFromDb.getId(),startPointsFromDb.getXCoordinate(), startPointsFromDb.getYCoordinate(),
                startPointsFromDb.getFloor(), startPointsFromDb.getIsElevator());
        Point endPoint = new Point(endPointsFromDb.getId(),endPointsFromDb.getXCoordinate(), endPointsFromDb.getYCoordinate(),
                endPointsFromDb.getFloor(), endPointsFromDb.getIsElevator());

        // 使用PointMapper从数据库获取所有点坐标记录
        List<Points> pointsFromDb = pointsMapper.selectAllCoordinates();
        List<ParkingPoint> parkingPointsFromDb = parkingPointMapper.selectAllCoordinates();
        // 将点的ID和对应的Point对象存储在HashMap中
        Map<Integer, Point> pointMap = new HashMap<>();
        Map<Integer, Point> parkingLotpointMap = new HashMap<>();
        for (Points pointFromDb : pointsFromDb) {
            int id = pointFromDb.getId(); // 假设Points类有getId()方法
            Point point = new Point(pointFromDb.getId(),pointFromDb.getXCoordinate(), pointFromDb.getYCoordinate(), pointFromDb.getFloor(), pointFromDb.getIsElevator());
            pointMap.put(id, point);
        }
        for (ParkingPoint e : parkingPointsFromDb){
            int id = e.getId();
            Point point = new Point(e.getId(),e.getXCoordinate(), e.getYCoordinate(), e.getFloor(), e.getIsElevator());
            parkingLotpointMap.put(id, point);
        }

        List<Point> points = pointsFromDb.stream()
                .map(p -> pointMap.get(p.getId())) // 使用pointMap来获取Point对象
                .collect(Collectors.toList());

        List<Point> parkingPoints = parkingPointsFromDb.stream()
                .map(p -> parkingLotpointMap.get(p.getId()))
                .collect(Collectors.toList());

        List<Connection> connections = connectionMapper.selectAllConnections();
        List<Connection> parkingConnects = parkingConnectMapper.selectAllConnections();

        // 打印当前时间1
        long startTime1 = System.currentTimeMillis();
        Graph graph = buildGraph(pointMap, points, connections, mode);
        Graph graph1 = buildGraph(parkingLotpointMap , parkingPoints , parkingConnects, mode);

        Graph graph_2 = buildGraph(pointMap, points, connections, mode);
        Graph graph1_2 = buildGraph(parkingLotpointMap , parkingPoints , parkingConnects, mode);
        Graph mergeGraph = mergeGraphs(graph,graph1);
        Graph mergeGraph_2 = mergeGraphs(graph_2,graph1_2);
        // 调用 aStar 方法，返回 AStarResult 对象
        AStarResult result = aStar(mergeGraph, startPoint, endPoint);
        AStarResult result_2 = aStar(mergeGraph_2, startPoint, endPoint);
// 如果没有找到路径，返回失败结果
        if (mode==1){
            if (result == null || result.getPath().isEmpty()) {
                return Result.fail("未找到路径");
            }
            // 获取路径和连接点信息
            List<Point> pathCoordinates = result.getPath();
            connectedPoints = result.getConnectedPoints();

// 处理路径，仅保留起点和终点所在楼层的路径
            List<Point> filteredPath = new ArrayList<>();
            for (Point point : pathCoordinates) {
                if (point.getFloor().equals(startPoint.getFloor()) || point.getFloor().equals(endPoint.getFloor())) {
                    filteredPath.add(point);
                }
            }

// 打印路径
        if (!filteredPath.isEmpty()) {
            System.out.println("Path found:");
            for (Point point : filteredPath) {
                System.out.println("Point: (" + point.getId() + ", " + point.getX() + ", " + point.getY() + ", " + point.getFloor() + ", " + point.isElevator() + ")");
            }
        } else {
            System.out.println("No path found.");
        }
            List<String> storeNames = getStoreNames(startPoint.getFloor());

            ResultPointDTO resultPointDTO = new ResultPointDTO();
            resultPointDTO.setFilteredPath(filteredPath);
            resultPointDTO.setStoreNames(storeNames);

// 返回最终结果
            return Result.ok(resultPointDTO);

        }else {
            if (result_2 == null || result_2.getPath().isEmpty()) {
                return Result.fail("未找到路径");
            }
            // 获取路径和连接点信息
            List<Point> pathCoordinates = result_2.getPath();
            connectedPoints = result_2.getConnectedPoints();

// 处理路径，仅保留起点和终点所在楼层的路径
            List<Point> filteredPath = new ArrayList<>();
            for (Point point : pathCoordinates) {
                if (point.getFloor().equals(startPoint.getFloor()) || point.getFloor().equals(endPoint.getFloor())) {
                    filteredPath.add(point);
                }
            }

//// 打印路径
//        if (!filteredPath.isEmpty()) {
//            System.out.println("Path found:");
//            for (Point point : filteredPath) {
//                System.out.println("Point: (" + point.getId() + ", " + point.getX() + ", " + point.getY() + ", " + point.getFloor() + ", " + point.isElevator() + ")");
//            }
//        } else {
//            System.out.println("No path found.");
//        }
            List<String> storeNames = getStoreNames(startPoint.getFloor());

            ResultPointDTO resultPointDTO = new ResultPointDTO();
            resultPointDTO.setFilteredPath(filteredPath);
            resultPointDTO.setStoreNames(storeNames);

// 返回最终结果
            return Result.ok(resultPointDTO);

        }




    }

    public List<String> getStoreNames(String startFloor) {
        // 获取 Store_Point 表中的所有 store_id 和 point_id 的映射
        List<StorePoint> storePointList = storePointMapper.getStorePointList();

        // 创建一个 map 来存储 point_id -> store_id 映射
        Map<Integer, Integer> pointToStoreMap = new HashMap<>();
        for (StorePoint storePoint : storePointList) {
            pointToStoreMap.put(storePoint.getPointId(), storePoint.getStoreId());
        }

        // 用于存储处理后的 connectedPoints
        List<List<Point>> processedConnectedPoints = new ArrayList<>();
        // 用于存储 point_id 对应的 store_id 列表
        List<Integer> storeIds = new ArrayList<>();

        boolean flagFloor = true; //换楼层

        // 对每个 connectedPoints 进行处理
        for (List<Point> pointList : connectedPoints) {
            List<Point> filteredPoints = new ArrayList<>();

            // 筛选出在 Store_Point 表中存在的 point_id
            for (Point point : pointList) {
                if (pointToStoreMap.containsKey(point.getId())) {
                    if(flagFloor){
                        if(startFloor.equals("B2")&&startFloor.equals(point.getFloor())){
                            continue;
                        }
                        if(!startFloor.equals(point.getFloor())){
                            System.out.println("换楼层");
                            flagFloor = false;
                            storeIds.add(5000);
                            String newFloor = point.getFloor();
                            //System.out.println(point.getFloor());
                            if(newFloor.equals("B2")){
                                //System.out.println("10000");
                                storeIds.add(10000);
                            }
                        }
                    }

                    // 如果存在对应的 store_id，则保留该 point
                    filteredPoints.add(point);
                    storeIds.add(pointToStoreMap.get(point.getId())); // 将对应的 store_id 存入 storeIds
                }
            }

            // 将筛选后的 point 列表加入到结果中
            if (!filteredPoints.isEmpty()) {
                processedConnectedPoints.add(filteredPoints);
            }
        }

        // 根据 storeIds 获取商铺名列表
        List<String> storeNames = new ArrayList<>();
        Set<String> storeNameSet = new HashSet<>(); // 用于记录已添加的商铺名
        for (Integer storeId : storeIds) {
            if(storeId == 5000){
                storeNames.add("乘坐电梯");
                System.out.println("乘坐电梯");
                continue;
            }
            if(storeId == 10000){
                storeNames.add("到达停车场");
                System.out.println("到达停车场");
                break;
            }
            String storeName = storeMapper.getStoreNameById(storeId);

            if (storeName != null && !storeNameSet.contains(storeName)) {
                if(storeName.equals("Calvin Klein Jeans")||storeName.equals("MINTYGREEN周生生")||storeName.equals("九木杂物社")||storeName.equals("小米之家")){
                    continue;
                }
                storeNames.add(storeName);
                storeNameSet.add(storeName); // 将商铺名加入到集合中，防止重复添加
                System.out.println(storeName);
            }
        }
        // 输出 processedConnectedPoints 内容，查看处理后的数据
//        for (int i = 0; i < processedConnectedPoints.size(); i++) {
//            List<Point> pointList = processedConnectedPoints.get(i);
//            System.out.println("Processed Points List " + (i + 1) + ": ");
//            for (Point point : pointList) {
//                //ids.add(point.getId());
//                System.out.println("Point ID: " + point.getId() + ", Coordinates: (" + point.getX() + ", " + point.getY() + ")");
//
//            }
//        }

        return storeNames;

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
        Point point1Graph = findPointInGraph(graph, 31.23, 67.52, "B1", 1);//529
        Point point1Graph1 = findPointInGraph(graph1, 51.3, 40.69, "B2", 1);
        if (point1Graph != null && point1Graph1 != null) {
            mergedGraph.addEdge(point1Graph, point1Graph1);
        }

        // 跨图连接2: (69.32, 52.74, B1, true) -> (30.46, 17.99, B2, true)
        Point point2Graph = findPointInGraph(graph, 69.32, 52.74, "B1", 1);//531
        Point point2Graph1 = findPointInGraph(graph1, 30.46, 17.99, "B2", 1);
        if (point2Graph != null && point2Graph1 != null) {
            mergedGraph.addEdge(point2Graph, point2Graph1);
        }

        // 跨图连接3: (53.82, 75.75, B1, true) -> (55.65, 26.88, B2, true)
        Point point3Graph = findPointInGraph(graph, 53.82, 75.75, "B1", 1);//532
        Point point3Graph1 = findPointInGraph(graph1, 55.65, 26.88, "B2", 1);
        if (point3Graph != null && point3Graph1 != null) {
            mergedGraph.addEdge(point3Graph, point3Graph1);
        }

        return mergedGraph;
    }

    // 根据坐标和楼层信息查找点
    private Point findPointInGraph(Graph graph, double x, double y, String floor, Integer isElevator) {
        for (Map.Entry<Point, List<Point>> entry : graph.getAdjList().entrySet()) {
            Point point = entry.getKey();
            if (point.getX() == x && point.getY() == y && point.getFloor().equals(floor) && point.isElevator().equals(isElevator)) {
                return point;
            }
        }
        return null;
    }


    private Graph buildGraph(Map<Integer, Point> pointMap, List<Point> points, List<Connection> connections, int mode) {
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
                Integer isElevator1 = point1.isElevator();
                Integer isElevator2 = point2.isElevator();

                // 判断是否可以建立连接
                if (floor1.equals(floor2) || (isElevator2==mode&&(isElevator1.equals(isElevator2)))) {
                    // 如果两个点在同一层，或者它们都与电梯连接，则建立边
                    //graph.addEdge(point1, point1);
                    graph.addEdge(point1, point2);
                }
            }
        }

        return graph;
    }
    // A*算法实现，返回从起点到终点的路径
    private AStarResult aStar(Graph graph, Point start, Point end) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        Map<Point, Node> nodeMap = new HashMap<>();
        Map<Point, Boolean> closedList = new HashMap<>();

        Node startNode = new Node(start);
        startNode.h = heuristic(start, end);
        startNode.f = startNode.h;
        openList.add(startNode);
        nodeMap.put(start, startNode);

        // 用于存储路径和每个点的连接点列表
        List<Point> path = new ArrayList<>();
        List<List<Point>> connectedPoints = new ArrayList<>();

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            closedList.put(current.point, true);

            if (current.point.equals(end)) {
                // 找到路径，构建结果
                path = constructPath(current);

                // 收集路径中每个点的相连点，使用 Set 避免重复连接点
                for (Point point : path) {
                    Set<Point> uniqueNeighbors = new HashSet<>(graph.getNeighbors(point)); // 使用 Set 去重
                    if(!point.getFloor().equals("B2")){
                        connectedPoints.add(new ArrayList<>(uniqueNeighbors)); // 转换回 List 添加到 connectedPoints
                    }
                }

                return new AStarResult(path, connectedPoints);
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

        System.out.println("未找到路径astar.");
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



    public class AStarResult {
        private List<Point> path; // 路径
        private List<List<Point>> connectedPoints; // 路径中每个点的连接点列表

        // 构造函数
        public AStarResult(List<Point> path, List<List<Point>> connectedPoints) {
            this.path = path;
            this.connectedPoints = connectedPoints;
        }

        // 获取路径
        public List<Point> getPath() {
            return path;
        }

        // 获取连接点列表
        public List<List<Point>> getConnectedPoints() {
            return connectedPoints;
        }
    }





}



